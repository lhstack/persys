package com.lhstack.filter;

import com.lhstack.config.security.SecurityConst;
import com.lhstack.utils.IpUtils;
import com.lhstack.utils.TokenParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

/**
 * 访问token过滤器，获取token自动登录
 */
@Slf4j
public class AccessTokenFilter implements WebFilter {

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        MultiValueMap<String, HttpCookie> cookies = serverWebExchange.getRequest().getCookies();
        WebSession webSession = serverWebExchange.getSession().block();
        Boolean state = (Boolean) webSession.getAttributes().get(SecurityConst.AUTHENTICATION_STATE_KEY);
        if(cookies.containsKey(SecurityConst.ACCESS_TOKEN) && state == null){
            HttpCookie cookie = cookies.get(SecurityConst.ACCESS_TOKEN).get(0);
            String value = cookie.getValue();
            if(checkTokenIsSecurity(serverWebExchange,value)){
                String v = redisTemplate.opsForValue().get(value).block();
                if(StringUtils.isNotEmpty(v)){
                    storageAccessTokenAsSession(serverWebExchange,value);
                    authentication(v,webSession);
                    log.info("user use token authentication success ,token as {}",value);
                }
            }
        }
        return webFilterChain.filter(serverWebExchange);
    }

    private void storageAccessTokenAsSession(ServerWebExchange serverWebExchange,String value) {
        WebSession block = serverWebExchange.getSession().block();
        block.getAttributes().put(SecurityConst.ACCESS_TOKEN,value);
    }


    /**
     * 检查登录用户是否安全
     * @param serverWebExchange
     * @param value
     * @return
     */
    private boolean checkTokenIsSecurity(ServerWebExchange serverWebExchange,String value) {
        String ip = IpUtils.getIp(serverWebExchange.getRequest());
        String state = redisTemplate.opsForValue().get(SecurityConst.ACCESS_REDIS_TOKEN_SECURITY + ip).block();
        if(StringUtils.isEmpty(state)){
            return false;
        }
        return true;
    }

    //取出token手动登录
    private  void authentication(String v, WebSession webSession) {
        try {
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(TokenParseUtils.parseAuthentication(v));
            webSession.getAttributes().put(SecurityConst.SPRING_SECURITY_CONTEXT,securityContext);
            webSession.getAttributes().put(SecurityConst.AUTHENTICATION_STATE_KEY,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
