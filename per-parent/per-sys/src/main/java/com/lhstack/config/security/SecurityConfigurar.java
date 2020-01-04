package com.lhstack.config.security;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.config.security.manager.AuthenticationManager;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.permission.User;
import com.lhstack.filter.AccessTokenFilter;
import com.lhstack.filter.CaptchaFilter;
import com.lhstack.utils.IpUtils;
import com.lhstack.utils.TokenParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(proxyTargetClass = true)
@Slf4j
public class SecurityConfigurar {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;


    @Value("${ignore.get}")
    private String[] ignoreGetUrls;

    @Value("${ignore.post}")
    private String[] ignorePostUrls;

    @Value("${ignore.delete}")
    private String[] ingoreDeleteUrls;

    @Autowired
    private LogoutHandler logoutHandler;


    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(){
        return new AuthenticationManager();
    }

    @Bean
    public WebFilter securityContextHolder(){
        return new SecurityContextHolder();
    }

    @Bean
    public AccessTokenFilter accessTokenFilter(){
        return new AccessTokenFilter();
    }

    @Bean
    public CaptchaFilter captchaFilter(){
        return new CaptchaFilter();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http){
        http.addFilterAt(accessTokenFilter(),SecurityWebFiltersOrder.FORM_LOGIN);
        http.addFilterAt(captchaFilter(), SecurityWebFiltersOrder.FORM_LOGIN);
        ServerHttpSecurity.FormLoginSpec formLoginSpec = http.formLogin()
                .loginPage("/login.html")
                .authenticationManager(reactiveAuthenticationManager());
        initFormLogin(formLoginSpec);
        return http.authorizeExchange()
                .pathMatchers(HttpMethod.GET,ignoreGetUrls)
                .permitAll()
                .pathMatchers(HttpMethod.POST,ignorePostUrls)
                .permitAll()
                .pathMatchers(HttpMethod.DELETE,ingoreDeleteUrls)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .logout()
                .logoutSuccessHandler((webFilterExchange, authentication) -> {
                    WebSession webSession = webFilterExchange.getExchange().getSession().block();
                    ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                    logoutHandler.clearRedisToken(webFilterExchange.getExchange());
                    logoutHandler.clearCookie(response);
                    logoutHandler.clearSession(webSession);
                    LayuiResut<Object> layuiResut = new LayuiResut<>();
                    layuiResut.setCode(200)
                            .setMsg("退出登录成功");
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_UTF8_VALUE);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(JSONObject.toJSONBytes(layuiResut))));
                })
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()//允许iframe访问
                .disable()
                .and()
                .build();
    }



    /**
     * 初始化formLogin
     * @param formLoginSpec
     */
    private void initFormLogin(ServerHttpSecurity.FormLoginSpec formLoginSpec) {
        formLoginSpec.authenticationSuccessHandler((webFilterExchange, authentication) -> {
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            return writeInfo(webFilterExchange,response,authentication);
        });
    }

    /**
     * 响应登录成功信息
     * @param webFilterExchange
     * @param response
     * @param authentication
     * @return
     */
    private Mono<Void> writeInfo(WebFilterExchange webFilterExchange,ServerHttpResponse response, Authentication authentication) {
        String token = null;
        try {
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatusCode(HttpStatus.OK);
            Map<String,Object> map = new HashMap<>();
            map.put("state",true);
            map.put("status",200);
            map.put("msg","登录成功");
            token = getToken(webFilterExchange,authentication);
            WebSession session = webFilterExchange.getExchange().getSession().block();
            //删除失效验证码
            session.getAttributes().remove("code");
            session.getAttributes().put(SecurityConst.ACCESS_TOKEN,token);
            session.getAttributes().put(SecurityConst.AUTHENTICATION_STATE_KEY,true);
            storageToken(token,authentication,0,webFilterExchange);
            writeAccessToken(token,response);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(objectMapper.writeValueAsBytes(map))));
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    /**
     * 解析token
     * @param webFilterExchange
     * @param authentication
     * @return
     */
    private String getToken(WebFilterExchange webFilterExchange,Authentication authentication) {
        String token = redisTemplate.opsForValue().get(getAccessRedisTokenSecurity(webFilterExchange)).block();
        if(StringUtils.isNotEmpty(token)){
            String authenticationStr = redisTemplate.opsForValue().get(token).block();
            if(StringUtils.isNotEmpty(authenticationStr) && authenticationEq(authentication, TokenParseUtils.parseAuthentication(authenticationStr))){
                return token;
            }
        }
        if(StringUtils.isNotEmpty(token)){
            //删除失效token
            redisTemplate.opsForValue().delete(token).subscribe();
        }
        return UUID.randomUUID().toString();
    }

    /**
     * 匹配当前登录用户与token存储用户是否相等，相等就复用token，不等就删除老token
     * @param currentAuthentication 当前授权用户
     * @param oldAuthentication 之前授权用户
     * @return
     */
    private boolean authenticationEq(Authentication currentAuthentication, Authentication oldAuthentication) {
        User currentUser = (User) currentAuthentication.getPrincipal();
        User oldUser = (User) oldAuthentication.getPrincipal();
        if(userEq(currentUser,oldUser)){
            //匹配成功，说明是同一个用户,同时信息没有发生改变
            return true;
        }
        return false;
    }

    private boolean userEq(User currentUser, User oldUser) {
        if(StringUtils.equals(currentUser.getUsername(),oldUser.getUsername())
        && StringUtils.equals(currentUser.getNickName(),oldUser.getNickName())
        && StringUtils.equals(currentUser.getIcon(),oldUser.getIcon())
        && StringUtils.equals(currentUser.getEmail(),oldUser.getEmail())){
            return true;
        }
        return false;
    }

    /**
     * 写入token到cookie
     * @param token
     * @param response 通过webFilterExchange获取
     */
    private void writeAccessToken(String token,ServerHttpResponse response) {
        ResponseCookie cookie = ResponseCookie
                .from(SecurityConst.ACCESS_TOKEN, token)
                .maxAge(Duration.of(7,ChronoUnit.DAYS))
                .path("/")
                .build();
        response.addCookie(cookie);
    }

    /**
     *  存储token到redis
     * @param token token 通过getToken(WebFilterExchange webFilterExchange,Authentication authentication)获取
     * @param authentication 用户授权信息
     * @param count 重试次数
     * @param webFilterExchange
     * @throws Exception
     */
    private void storageToken(String token,Authentication authentication,Integer count,WebFilterExchange webFilterExchange) throws Exception {
        /**
         * 重试次数3次
         */
        if(count >= 3){
            throw new RedisConnectionFailureException(String.format("redis token write failure ， token %s , authentication %s ",token,authentication));
        }
        Boolean state = redisTemplate.opsForValue().set(token, JSONObject.toJSONString(authentication), Duration.of(7, ChronoUnit.DAYS)).block();
        if(!state){
            log.info("set token count as {},token as {},authentication as {} ",count,token,authentication);
            storageToken(token,authentication,count + 1,webFilterExchange);
        }
        User user = (User) authentication.getPrincipal();
        log.info("store token is success,token as {},ip as {},username as {}",token,IpUtils.getIp(webFilterExchange.getExchange().getRequest()),user.getUsername());
        storageTokenSecurityKey(getAccessRedisTokenSecurity(webFilterExchange),token,0);
    }

    /**
     * 返回与用户token绑定的ipKey
     * @param webFilterExchange web过滤器交换机，通过webFilter或者其他方式获取
     * @return
     */
    private String getAccessRedisTokenSecurity(WebFilterExchange webFilterExchange){
        String ip = IpUtils.getIp(webFilterExchange.getExchange().getRequest());
        return SecurityConst.ACCESS_REDIS_TOKEN_SECURITY + ip;
    }

    /**
     * 存储用户登录ip，下一次带token过来时验证ip是否合法
     * @param access_redis_token_security 访问token ip key 通过 getAccessRedisTokenSecurity(WebFilterExchange webFilterExchange)获得
     * @param token token 通过cookie解析获得
     * @param count 重试次数,递归调用
     */
    private void storageTokenSecurityKey(String access_redis_token_security,String token,Integer count) {
        if(count >= 3){
            throw new RedisConnectionFailureException(String.format("redis token security key write failure ， token security key ---> %s , token ---> %s ",access_redis_token_security,token));
        }

        Boolean state = redisTemplate.opsForValue().set(access_redis_token_security, token,Duration.of(7,ChronoUnit.DAYS)).block();
        if(!state){
            storageTokenSecurityKey(access_redis_token_security,token,count + 1);
        }
    }

}
