package com.lhstack.controller.mail;

import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.permission.User;
import com.lhstack.service.mail.IMailService;
import com.lhstack.service.mail.MailConst;
import com.lhstack.service.permission.IUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;

import java.util.Map;

@RestController
@RequestMapping("mail")
public class MailController {

    @Autowired
    private IMailService mailService;


    @Autowired
    private IUserService userService;
    /**
     * 验证验证码
     * @param map
     *          to: 邮箱
     *          valid: 验证码
     *          newMail: 新邮箱
     * @param session
     * @return
     */
    @PostMapping("valid")
    public ResponseEntity<LayuiResut<Object>> valid(@RequestBody Map<String,Object> map, WebSession session){
       if(!map.containsKey("to") || !map.containsKey("valid")){
           throw new NullPointerException("验证信息提交出现错误");
       }
        String valid = (String) session.getAttributes().get(MailConst.MAIL_PREFIX + map.get("to").toString());
       if(StringUtils.equals(valid,map.get("valid").toString())){
           return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("验证通过"));
       }
        return ResponseEntity.ok(LayuiResut.buildError("验证失败",403));
    }


    /**
     * 发送重置密码邮箱
     * @param to
     * @param session
     * @return
     */
    @PostMapping("send/reset")
    public ResponseEntity<LayuiResut<Object>> sendReset(@RequestParam("to") String to, WebSession session){
        User user = userService.findByEmail(to);
        if(ObjectUtils.isEmpty(user))
            throw new RuntimeException("邮箱不存在");
        int random = (int) (Math.random() * 10000);
        session.getAttributes().put(MailConst.MAIL_RESET + to,random);
        mailService.sendSimpleMail("PerSys 重置密码验证码: " + random,"PerSys 重置密码验证",to);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("验证码发送成功，请登陆邮件进行查看"));
    }

    /**
     * 发送验证码
     * @param to 邮箱
     * @param session 存储验证码信息
     * @return
     */
    @PostMapping("send/valid")
    public ResponseEntity<LayuiResut<Object>> sendValid(@RequestParam("to") String to, WebSession session){
        int random = (int) (Math.random() * 10000);
        session.getAttributes().put(MailConst.MAIL_PREFIX + to,random);
        mailService.sendSimpleMail("PerSys 邮件验证码: " + random,"PerSys 邮件验证",to);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("验证码发送成功，请登陆邮件进行查看"));
    }
}
