package com.lhstack.controller.sso;

import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.permission.User;
import com.lhstack.entity.sso.SSOToken;
import com.lhstack.entity.sso.vo.SSOUserVo;
import com.lhstack.service.permission.IUserService;
import com.lhstack.service.sso.ISSOTokenService;
import com.lhstack.utils.PasswordEncoderUtils;
import com.lhstack.utils.SSOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 单点登录服务
 */
@Controller
@RequestMapping("sso")
public class SSOTokenController {


    @Autowired
    private ISSOTokenService ssoTokenService;

    @Autowired
    private SSOUtils ssoUtils;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoderUtils passwordEncoderUtils;

    @PostMapping("/login")
    public String login(SSOUserVo map, WebSession session, ServerHttpRequest request, ServerHttpResponse response) throws IOException {
        String username = map.getUsername();
        String password = map.getPassword();
        String validCode = map.getValidCode();
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(validCode)){
            throw new RuntimeException("登录参数填入有误，请填入正确的参数进行登录");
        }
        String code = (String) session.getAttributes().get("code");
        if(!StringUtils.equals(validCode,code)){
            session.getAttributes().put("infoMsg","验证码输入不正确，请重新输入");
            return "redirect:/sso/login?token=" + session.getAttribute("token") + "&service=" + session.getAttribute("service");
        }
        User user = null;
        if(username.matches("^([0-9A-Za-z\\-_\\.]+)@([0-9a-z]+\\.[a-z]{2,3}(\\.[a-z]{2})?)$")){
            user = userService.findByEmail(username);
        }else{
            user = userService.findByUsername(username);
        }
        if(user == null){
            session.getAttributes().put("infoMsg","用户不存在，请检查用户名是否输入正确");
            return "redirect:/sso/login?token=" + session.getAttribute("token") + "&service=" + session.getAttribute("service");
        }
        if(user.getIsDel()){
            session.getAttributes().put("infoMsg","用户已经被删除，不能使用");
            return "redirect:/sso/login?token=" + session.getAttribute("token") + "&service=" + session.getAttribute("service");
        }
        if(user.getIsLock()){
            session.getAttributes().put("infoMsg","用户已经被锁定，请联系管理员解锁");
            return "redirect:/sso/login?token=" + session.getAttribute("token") + "&service=" + session.getAttribute("service");
        }
        if(!passwordEncoderUtils.matches(user.getSalt(),password,user.getPassword())){
            session.getAttributes().put("infoMsg","用户名密码输入有误");
            return "redirect:/sso/login?token=" + session.getAttribute("token") + "&service=" + session.getAttribute("service");
        }
        session.getAttributes().remove("code");
        session.getAttributes().remove("infoMsg");
        String ssoToken = ssoUtils.generatorAccessToken(user,(SSOToken)session.getAttributes().get("ssoTokenInfo"),request);
        ssoUtils.storeSSOToken(ssoToken,response,(SSOToken)session.getAttributes().get("ssoTokenInfo"));
        String redirect_url = ssoUtils.generatorRedirectUrl((String) session.getAttributes().get("service"),ssoToken);
        return "redirect:" + redirect_url;
    }


    @GetMapping("userInfo")
    @ResponseBody
    public ResponseEntity<LayuiResut<Map<String,Object>>> getUserInfo(@RequestParam("access_token") String accessToken,ServerHttpRequest request){
        if(StringUtils.isEmpty(accessToken)){
            throw new NullPointerException("请携带访问token");
        }
        return ResponseEntity.ok(LayuiResut.buildSuccess(ssoUtils.getUserInfo(accessToken,request)).setMsg("获取用户信息成功"));
    }

    @GetMapping({"/login","/login.html","/login.jsp"})
    public String ssoLogin(@RequestParam(value = "token",required = false) String ssoToken,
                           @RequestParam(value = "service",required = false) String redirectService,
                           WebSession session, ServerHttpRequest request,Model model){

        if(StringUtils.isEmpty(ssoToken)){
            session.getAttributes().put("message","请携带token访问单点登录服务");
            return "redirect:/error_info";
        }
        SSOToken resultSSOToken = ssoTokenService.findBySSoToken(ssoToken);
        if(resultSSOToken == null){
            session.getAttributes().put("message","token不存在，请访问http://admin.lhstack.xyz注册账号添加单点登录服务配置");
            return "redirect:/error_info";
        }
        if(StringUtils.isEmpty(redirectService)){
            String[] services = resultSSOToken.getService();
            if(services.length == 1){
                redirectService = services[0];
            }else{
                session.getAttributes().put("message","请明确指定service访问单点登录服务");
                return "redirect:/error_info";
            }
        }
        List<String> services = Arrays.asList(resultSSOToken.getService());
        if(!services.contains(redirectService)){
            session.getAttributes().put("message","重定向地址service不存在，请访问http://admin.lhstack.xyz查看你的单点服务重定向url是否正确");
            return "redirect:/error_info";
        }
        if(ssoUtils.isLogin(request,ssoToken)){
            String url = ssoUtils.generatorRedirectUrl(redirectService, ssoUtils.getAccessToken(request));
            return "redirect:" + url;
        }
        model.addAttribute("infoMsg",session.getAttribute("infoMsg"));
        session.getAttributes().put("token",ssoToken);
        session.getAttributes().put("service",redirectService);
        session.getAttributes().put("ssoTokenInfo",resultSSOToken);
        return "sso/login";
    }

    @GetMapping({"/registry","/registry.html","/registry.jsp"})
    public String ssoRegistry(WebSession session, Model model,@RequestParam(value = "token",defaultValue = "") String token){
        Map<String, Object> attributes = session.getAttributes();
        if(StringUtils.equals("admin",token)){
            model.addAttribute("token","admin");
            model.addAttribute("service","admin");
        }else{
            model.addAttribute("token",attributes.containsKey("token") ? attributes.remove("token") : "admin");
            model.addAttribute("service",attributes.containsKey("service") ? attributes.remove("service") : "admin");
        }
        return "sso/registry";
    }


    @PostMapping("add")
    @ResponseBody
    public ResponseEntity<LayuiResut<SSOToken>> add(@RequestBody SSOToken ssoToken) throws Exception {
        Authentication authentication = SecurityContextHolder.get();
        if(authentication == null){
            throw new NullPointerException("请先登录");
        }
        User user = (User) authentication.getPrincipal();
        ssoToken.setUserId(user.getId());
        return ResponseEntity.ok(LayuiResut.buildSuccess(ssoTokenService.save(ssoToken)).setMsg("sso应用创建成功"));
    }

    @PutMapping("update")
    @ResponseBody
    public ResponseEntity<LayuiResut<SSOToken>> update(@RequestBody SSOToken ssoToken) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(ssoTokenService.update(ssoToken.getId(),ssoToken)).setMsg("更新应用成功"));
    }

    @DeleteMapping("del/{id}")
    @ResponseBody
    public ResponseEntity<LayuiResut<Boolean>> del(@PathVariable("id") String id) throws Exception {
        ssoTokenService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("删除应用成功"));
    }

    @GetMapping("page")
    @ResponseBody
    public ResponseEntity<LayuiTableResult<SSOToken>> selectPage(@RequestParam(value = "page",defaultValue = "1") Integer page,@RequestParam(value = "size",defaultValue = "10") Integer size) throws Exception {
        LayuiTableResult<SSOToken> layuiTableResult = new LayuiTableResult<>();
        Authentication authentication = SecurityContextHolder.get();
        if(authentication == null){
            throw new NullPointerException("请登录");
        }
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        Page<SSOToken> ssoTokenPage = ssoTokenService.findByUserId(userId,page, size);
        layuiTableResult.setCount(ssoTokenPage.getTotalElements())
                .setCode(200)
                .setMsg("获取应用成功")
                .setData(ssoTokenPage.getContent());
        return ResponseEntity.ok(layuiTableResult);
    }
}
