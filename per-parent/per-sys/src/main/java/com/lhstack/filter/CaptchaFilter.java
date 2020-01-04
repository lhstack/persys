package com.lhstack.filter;

import com.lhstack.entity.layui.LayuiResut;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;


/**
 * 验证码过滤器
 */
public class CaptchaFilter implements WebFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        MultiValueMap<String, String> data = serverWebExchange.getFormData().block();
        String path = serverWebExchange.getRequest().getPath().toString();
        if((path.equals("/login.html") || path.equals("/user/registry")) && serverWebExchange.getRequest().getMethod() == HttpMethod.POST){
            String code = data.getFirst("code");
            WebSession session = serverWebExchange.getSession().block();
            String sessionCode = session.getAttribute("code");
            if(StringUtils.isBlank(code) || !StringUtils.equalsIgnoreCase(code,sessionCode)){
                ServerHttpResponse response = serverWebExchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
                try {
                    LayuiResut<Object> layuiResut = new LayuiResut<>();
                    layuiResut.setCode(403)
                            .setMsg("验证码输入有误");
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(objectMapper.writeValueAsBytes(layuiResut))));
                }  catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return webFilterChain.filter(serverWebExchange);
    }
}
