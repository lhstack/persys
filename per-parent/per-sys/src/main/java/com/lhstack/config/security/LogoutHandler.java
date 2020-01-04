package com.lhstack.config.security;

import com.lhstack.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LogoutHandler {

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;
    /**
     * 清除cookie
     * @param response
     */
    public void clearCookie(ServerHttpResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from(SecurityConst.ACCESS_TOKEN,"")
                .maxAge(0)
                .path("/")
                .build();
        response.addCookie(responseCookie);
    }

    /**
     * 清除redis认证信息
     * @param exchange
     */
    public void clearRedisToken(ServerWebExchange exchange) {
        String value = exchange.getSession().block().getAttribute(SecurityConst.ACCESS_TOKEN);
        if(StringUtils.isNotEmpty(value)){
            String ip = IpUtils.getIp(exchange.getRequest());
            Mono<Boolean> state = redisTemplate.opsForValue().delete(value);
            Mono<Boolean> accessTokenState = redisTemplate.opsForValue().delete(SecurityConst.ACCESS_REDIS_TOKEN_SECURITY + ip);
            log.info("delete accessToken as {} , delete state is {}",value,state.block());
            log.info("delete accessTokenState as {} , delete state is {}",SecurityConst.ACCESS_REDIS_TOKEN_SECURITY + ip,accessTokenState.block());
        }
    }

    /**
     * 清除session认证信息
     * @param session
     */
    public void clearSession(WebSession session) {
        session.getAttributes().remove(SecurityConst.SPRING_SECURITY_CONTEXT);
        session.getAttributes().remove(SecurityConst.AUTHENTICATION_STATE_KEY);
        ReactiveSecurityContextHolder.clearContext();
    }
}
