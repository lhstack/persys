package com.lhstack.controller.permission;

import com.github.pagehelper.PageInfo;
import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.config.security.LogoutHandler;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.controller.excontroller.RegistryException;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.User;
import com.lhstack.entity.permission.UserExampleDTO;
import com.lhstack.service.mail.IMailService;
import com.lhstack.service.mail.MailConst;
import com.lhstack.service.permission.IRoleService;
import com.lhstack.service.permission.IUserService;
import com.lhstack.utils.PasswordEncoderUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    public final static String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMailService mailService;

    @Autowired
    private PasswordEncoderUtils passwordEncoderUtils;

    @Autowired
    private LogoutHandler logoutHandler;


    @PostMapping("/pass/reset/valid")
    public ResponseEntity<LayuiResut<Boolean>> validResetPass(@RequestParam(value = "to",required = true) String to,
                                                              @RequestParam(value = "validCode",required = true) String validCode,
                                                              WebSession session){
        String valid = session.getAttribute(MailConst.MAIL_RESET + to).toString();
        if(!StringUtils.equals(valid,validCode)){
            throw new RuntimeException("验证码不正确");
        }
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }

    @PutMapping("update/info")
    public ResponseEntity<LayuiResut<Boolean>> updateUserInfo(@RequestBody User user) throws Exception {
        User principal = (User) SecurityContextHolder.get().getPrincipal();
        User result = userService.findByUsername(principal.getUsername());
        result.setNickName(user.getNickName())
                .setIcon(user.getIcon());
        userService.update(result.getId(), result);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("用户信息更新成功"));
    }

    /**
     * @param map     validCode 验证码，邮箱收到的验证码
     *                oldPass 原始密码
     *                newPass 新密码
     * @param session
     * @return
     */
    @PostMapping("editPass")
    public ResponseEntity<LayuiResut<Object>> changePass(@RequestBody Map<String, Object> map,
                                                         WebSession session,
                                                         ServerWebExchange exchange) throws Exception {
        if (StringUtils.isEmpty(map.get("validCode").toString())
                || StringUtils.isEmpty(map.get("oldPass").toString())
                || StringUtils.isEmpty(map.get("newPass").toString())) {
            throw new NullPointerException("请传入正确的参数");
        }
        User principal = (User) SecurityContextHolder.get().getPrincipal();
        String code = (String) session.getAttributes().get(MailConst.EDIT_MAIL_PREFIX + principal.getEmail());
        if (!StringUtils.equals(code, map.get("validCode").toString())) {
            throw new NullPointerException("验证码不正确,请重新输入");
        }
        User user = userService.findByUsername(principal.getUsername());
        String oldPass = (String) map.get("oldPass");
        String newPass = (String) map.get("newPass");
        Boolean matches = passwordEncoderUtils.matches(user.getSalt(), oldPass, user.getPassword());
        if (!matches) {
            throw new NullPointerException("原始密码输入有误，请重新输入");
        }
        String password = passwordEncoderUtils.genPass(user.getSalt(), newPass);
        user.setPassword(password);
        userService.update(user.getId(), user);
        logout(exchange, session);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("密码修改成功，请重新登录"));
    }

    /**
     * 退出登录
     *
     * @param exchange
     */
    private void logout(ServerWebExchange exchange, WebSession session) {
        logoutHandler.clearRedisToken(exchange);
        logoutHandler.clearSession(session);
        logoutHandler.clearCookie(exchange.getResponse());
    }

    /**
     * @param map     validCode 验证码
     *                newEmail 新邮箱
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("changeEmail")
    public ResponseEntity<LayuiResut<User>> changeEmail(@RequestBody Map<String, Object> map, WebSession session) throws Exception {
        if (StringUtils.isEmpty(map.get("validCode").toString()) || StringUtils.isEmpty(map.get("newEmail").toString())) {
            throw new NullPointerException("请传入正确的参数");
        }
        String validCode = map.get("validCode").toString();
        String newEmail = map.get("newEmail").toString();
        String code = (String) session.getAttributes().get(MailConst.EDIT_MAIL_PREFIX + newEmail);
        if (!StringUtils.equals(validCode, code)) {
            throw new NullPointerException("验证码不正确,请重新输入");
        }
        session.getAttributes().remove(MailConst.EDIT_MAIL_PREFIX + newEmail);
        User principal = (User) SecurityContextHolder.get().getPrincipal();
        User user = userService.findByUsername(principal.getUsername());
        user.setEmail(newEmail);
        userService.update(user.getId(), user);
        user.setSalt(null)
                .setPassword(null);
        return ResponseEntity.ok(LayuiResut.buildSuccess(user).setMsg("更新邮箱成功"));
    }


    @PostMapping("reset/pass")
    public ResponseEntity<LayuiResut<Boolean>> reset(@RequestBody Map<String,String> info,WebSession session) throws Exception {
        System.out.println(info);
        if(isExistsField(info,"email") && isExistsField(info,"validCode") && isExistsField(info,"newPass")){
            String email = info.get("email");
            String validCode = info.get("validCode");
            String valid = session.getAttribute(MailConst.MAIL_RESET + email).toString();
            if(!StringUtils.equals(valid,validCode)){
                throw new RuntimeException("验证码不正确，请重新输入");
            }
            User user = userService.findByEmail(email);
            user.setSalt(passwordEncoderUtils.salt())
                    .setPassword(passwordEncoderUtils.genPass(user.getSalt(),info.get("newPass")));
            session.getAttributes().remove(MailConst.MAIL_RESET + email);
            return ResponseEntity.ok(LayuiResut.buildSuccess(userService.update(user.getId(),user) != null));
        }
        throw new RuntimeException("提交数据不合法");
    }

    private boolean isExistsField(Map<String, String> info, String field) {
        return StringUtils.isNotBlank(info.get(field));
    }

    /**
     * 修改密码，发送验证码
     *
     * @param to
     * @param session
     * @return
     */
    @PostMapping("changePassValid")
    public ResponseEntity<LayuiResut<Object>> sendPassValid(@RequestParam("to") String to, WebSession session) throws Exception {
        int random = (int) (Math.random() * 100000);
        session.getAttributes().put(MailConst.EDIT_MAIL_PREFIX + to, random + "");
        mailService.sendHtmlMail(MailConst.VALID_CODE_HTML_TEMPLATE.replace("@", random + ""), "PerSys邮箱验证", to);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("验证码发送成功，请登陆邮箱进行查看"));
    }

    /**
     * 修改邮箱，发送验证码
     *
     * @param to
     * @param session
     * @return
     */
    @PostMapping("changeSendValid")
    public ResponseEntity<LayuiResut<Object>> sendValid(@RequestParam("to") String to, WebSession session) throws Exception {
        User user = userService.findByEmail(to);
        if (user != null) {
            return ResponseEntity.ok(LayuiResut.buildError("邮箱已存在，请重新更换邮箱进行绑定", 403));
        }
        int random = (int) (Math.random() * 100000);
        session.getAttributes().put(MailConst.EDIT_MAIL_PREFIX + to, random + "");
        mailService.sendHtmlMail(MailConst.VALID_CODE_HTML_TEMPLATE.replace("@", random + ""), "PerSys邮箱验证", to);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("验证码发送成功，请登陆邮箱进行查看"));
    }

    /**
     * 复杂查询
     *
     * @param examples
     * @return
     */
    @PostMapping("page")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_QUERY)")
    @DynAuthority
    public Mono<LayuiTableResult<User>> findAll(UserExampleDTO examples) {
        LayuiTableResult<User> layuiTableResult = new LayuiTableResult<>();
        initExample(examples);
        PageInfo<User> userPageInfo = userService.findByNotExistThisAndExample(examples);
        layuiTableResult.setCount(userPageInfo.getTotal())
                .setMsg("获取列表成功")
                .setCode(0)
                .setData(userPageInfo.getList());
        return Mono.just(layuiTableResult);
    }

    /**
     * 初始化findByNotExistThisAndExample查询条件
     *
     * @param example
     */
    private void initExample(UserExampleDTO example) {
        Authentication authentication = SecurityContextHolder.get();
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new NullPointerException("请先登录");
        }
        example.setUserId(user.getId());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        example.setIsAdmin(authorities.stream().anyMatch(item -> ((GrantedAuthority) item).getAuthority().equals(ROLE_ADMIN)));
        example.setIgnoreRoleNames(Arrays.asList("ADMIN"));
        example.setIgnorePermissionNames(Arrays.asList("ADMIN_USER_QUERY"));
    }


    @DeleteMapping("del/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_DELETE)")
    public Mono<ResponseEntity<LayuiResut<Object>>> del(@PathVariable("id") Long id) {
        Long count = userService.deleteByIdAndRole(id);
        if (count > 0) {
            return Mono.just(ResponseEntity.ok(LayuiResut.buildSuccess(null)
                    .setMsg("删除成功")
            ));
        }
        return Mono.just(ResponseEntity.badRequest()
                .body(LayuiResut.buildSuccess(null)
                        .setMsg("删除失败")
                        .setCode(403)));
    }

    @DeleteMapping("del/all/{ids}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_DELETE)")
    public Mono<ResponseEntity<LayuiResut<Object>>> delAll(@PathVariable("ids") List<Long> ids) {
        Long count = userService.deleteByIdsAndRole(ids);
        return Mono.just(ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("删除成功")));
    }


    @GetMapping("role/{uid}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_QUERY)")
    public Mono<ResponseEntity<LayuiResut<List<Role>>>> findByUidRole(@PathVariable("uid") Long uid) {
        LayuiResut<List<Role>> layuiResut = LayuiResut
                .buildSuccess(roleService.findByUid(uid))
                .setMsg("查询角色成功")
                .setCode(0);
        return Mono.just(ResponseEntity.ok(layuiResut));
    }

    @GetMapping("roles")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_QUERY)")
    public Mono<ResponseEntity<LayuiResut<List<Role>>>> findByRoles() {
        LayuiResut<List<Role>> layuiResut = LayuiResut
                .buildSuccess(roleService.findAllByIgnoreRoleNames("ADMIN", "USER_ADMIN"))
                .setMsg("查询角色成功")
                .setCode(0);
        return Mono.just(ResponseEntity.ok(layuiResut));
    }

    @PostMapping("update")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_UPDATE)")
    public Mono<ResponseEntity<LayuiResut<User>>> updateUser(User user, @RequestParam(value = "rids", required = false) List<Long> rids) {
        User result = userService.updateAndRole(user, rids);
        return Mono.just(ResponseEntity.ok(LayuiResut.buildSuccess(result).setMsg("更新用户信息成功")));
    }

    @PostMapping("add")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_ADD)")
    public Mono<ResponseEntity<LayuiResut<User>>> addUser(User user, @RequestParam(value = "rids", required = false) List<Long> rids) {
        return Mono.just(ResponseEntity.ok(LayuiResut.buildSuccess(userService.saveUserAndRole(user, rids)).setMsg("添加用户成功")));
    }

    /**
     * 注册用户
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("reg")
    public Mono<ResponseEntity<LayuiResut<User>>> registry(@RequestBody User user,
                                                           @RequestParam("code") String code,
                                                           WebSession session) {
        String validCode = (String) session.getAttributes().get(MailConst.EDIT_MAIL_PREFIX + user.getEmail());

        if (!StringUtils.equals(code, validCode)) {
            throw new RegistryException("验证码不正确");
        }

        String username = user.getUsername();
        User u = userService.findByUsername(username);
        if (u != null) {
            throw new RegistryException("用户名已存在");
        }
        User u1 = userService.findByNickName(user.getNickName());
        if (u1 != null) {
            throw new RegistryException("用户名已存在");
        }
        User result = userService.registry(user);
        session.getAttributes().remove(MailConst.MAIL_PREFIX + user.getEmail());
        return Mono.just(ResponseEntity.ok(LayuiResut.buildSuccess(result)
                .setMsg("注册用户成功")));
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("info")
    public Mono<ResponseEntity<LayuiResut<User>>> loadUserInfo() {
        User user = (User) SecurityContextHolder.get().getPrincipal();
        User result = userService.findByUsername(user.getUsername());
        result.setPassword(null)
                .setSalt(null);
        return Mono.just(ResponseEntity.ok(LayuiResut.buildSuccess(result).setMsg("获取用户信息成功")));
    }
}
