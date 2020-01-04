package com.lhstack.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
public class IndexController {


    @Value("${admin.authUrl}")
    private String adminUrl;

    @GetMapping("error_info")
    @ResponseBody
    public Object out(WebSession session){
        return session.getAttribute("message");
    }

    @GetMapping({"/","/index","/home"})
    public String index(Model model,@AuthenticationPrincipal Authentication authentication){
        model.addAttribute("admin_url",adminUrl);
        return "index";
    }

    @GetMapping({"login.html","login","login.jsp","login.php","login.action","login.do"})
    public String login(){
        return "login";
    }

    @GetMapping("page/{pageName}")
    public String toPage(@PathVariable("pageName") String pageName){
        return pageName.replace("@","/");
    }


    @GetMapping("code")
    public Mono<Void> generateCode(ServerHttpResponse response, WebSession session){
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(400, 200);
        lineCaptcha.createCode();
        session.getAttributes().put("code",lineCaptcha.getCode());
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_GIF_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(lineCaptcha.getImageBytes())));
    }
}
