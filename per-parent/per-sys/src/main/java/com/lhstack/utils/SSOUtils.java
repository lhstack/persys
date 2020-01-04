package com.lhstack.utils;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.User;
import com.lhstack.entity.sso.SSOToken;
import com.lhstack.service.permission.IPermissionService;
import com.lhstack.service.permission.IRoleService;
import com.lhstack.service.sso.ISSOTokenService;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SSOUtils {

    public static final String SSO_LOGIN_TOKEN_UUID = "SSO_LOGIN_TOKEN_UUID";

    public static final String SSO_REDIS_TOKEN_SPACE = "-SSO_REDIS_TOKEN_SPACE-";

    public static final String SSO_LOGIN_TOKEN_JWT = "SSO_LOGIN_TOKEN_JWT";

    public static final Integer TOKEN_EXPIRE_TIME_MINUTE = 5;

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;


    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ISSOTokenService ssoTokenService;
    /**
     * 判断是否登陆
     * @param request
     * @return
     */
    public boolean isLogin(ServerHttpRequest request,String sso_token) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (!cookies.containsKey(SSO_LOGIN_TOKEN_UUID) && !cookies.containsKey(SSO_LOGIN_TOKEN_JWT))
            return false;
        if(cookies.containsKey(SSO_LOGIN_TOKEN_UUID)){
            HttpCookie cookie = cookies.getFirst(SSO_LOGIN_TOKEN_UUID);
            String token = cookie.getValue();
            try {
                String jsonUser = redisTemplate.opsForValue().get(token + SSO_REDIS_TOKEN_SPACE + IpUtils.getIp(request)).block();
                User user = JSONObject.parseObject(jsonUser, User.class);
                return user != null;
            } catch (Exception e) {
                return false;
            }
        }else{
            HttpCookie cookie = cookies.getFirst(SSO_LOGIN_TOKEN_JWT);
            String token = cookie.getValue();
            SSOToken ssoTokenInfo = ssoTokenService.findBySSoToken(sso_token);
            String jwtSignKey = ssoTokenInfo.getJwtSignKey();
            try{
                Jwt jwt = Jwts.parser().setSigningKey(jwtSignKey).parse(token);
                return true;
            }catch (Exception e){
                return false;
            }
        }
    }

    /**
     * 生成重定向url
     * @param service
     * @param accessToken
     * @return
     */
    public String generatorRedirectUrl(String service, String accessToken) {
        if (StringUtils.contains(service, "?")) {
            return service + "&access_token=" + accessToken;
        } else {
            return service + "?access_token=" + accessToken;
        }
    }

    /**
     * 生成访问token
     * @param user 登陆用户信息
     * @param ssoTokenInfo 后台管理创建的token信息
     * @param request 请求
     * @return
     * @throws IOException
     */
    public String generatorAccessToken(User user, SSOToken ssoTokenInfo, ServerHttpRequest request) throws IOException {
        if (StringUtils.equals("UUID", ssoTokenInfo.getType().toUpperCase())) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            ReactiveValueOperations<String, String> operations = redisTemplate.opsForValue();
            Mono<Boolean> subscribe = operations.set(uuid + SSO_REDIS_TOKEN_SPACE + IpUtils.getIp(request), transformObjectStream(user,ssoTokenInfo.getIsGeneratorPermissionAndRole()), Duration.of(TOKEN_EXPIRE_TIME_MINUTE, ChronoUnit.MINUTES));
            subscribe.subscribe();
            return uuid;
        }else if(StringUtils.equals("JWT",ssoTokenInfo.getType())){
            String jwtToken = generatorJwtToken(ssoTokenInfo,user,ssoTokenInfo.getIsGeneratorPermissionAndRole());
            return jwtToken;
        }
        return null;
    }

   /**
     * 生成jwtToken
     * @param ssoTokenInfo 后台创建的token信息
     * @param user 登陆用户
     * @param isGeneratorPermissionAndRole
     * @return
     */
    private String generatorJwtToken(SSOToken ssoTokenInfo, User user, Boolean isGeneratorPermissionAndRole) {
        User resultUser = new User();
        resultUser.setIcon(user.getIcon())
                .setUsername(user.getUsername())
                .setNickName(user.getNickName())
                .setEmail(user.getEmail())
                .setCreateTime(user.getCreateTime());
        if(!isGeneratorPermissionAndRole){
            return Jwts.builder().signWith(SignatureAlgorithm.HS512, ssoTokenInfo.getJwtSignKey())
                    .addClaims(MapUtil.of("userInfo", resultUser))
                    .setExpiration(DateUtils.addMinutes(new Date(), TOKEN_EXPIRE_TIME_MINUTE)).compact();
        }else{
            List<Role> roles = roleService.findByUid(user.getId());
            Map<String,Permission> permissionHashMap = new HashMap<>();
            List<Permission> permissionList = roles.stream().map(item -> permissionService.findByRid(item.getId()))
                    .flatMap(List::stream)
                    .filter(item -> {
                        if (!permissionHashMap.containsKey(item.getPermissionName())) {
                            permissionHashMap.put(item.getPermissionName(), item);
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map.put("userInfo",resultUser);
            map.put("roles",roles);
            map.put("permissions",permissionList);
            return Jwts.builder().signWith(SignatureAlgorithm.HS512, ssoTokenInfo.getJwtSignKey())
                    .addClaims(map)
                    .setExpiration(DateUtils.addMinutes(new Date(), TOKEN_EXPIRE_TIME_MINUTE)).compact();
        }
    }

    /**
     * 将用户信息转换成json对象流
     * @param user 登陆用户
     * @param isGeneratorPermissionAndRole
     * @return
     * @throws IOException
     */
    private String transformObjectStream(User user, Boolean isGeneratorPermissionAndRole) throws IOException {
        Map<String,Object> map = new HashMap<>();
        User resultUser = new User();
        resultUser.setIcon(user.getIcon())
                .setUsername(user.getUsername())
                .setNickName(user.getNickName())
                .setEmail(user.getEmail())
                .setCreateTime(user.getCreateTime());
        map.put("userInfo",resultUser);
        if(isGeneratorPermissionAndRole){
            List<Role> roles = roleService.findByUid(user.getId());
            Map<String,Permission> permissionHashMap = new HashMap<>();
            List<Permission> permissionList = roles.stream().map(item -> permissionService.findByRid(item.getId()))
                    .flatMap(List::stream)
                    .filter(item -> {
                        if (!permissionHashMap.containsKey(item.getPermissionName())) {
                            permissionHashMap.put(item.getPermissionName(), item);
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
            map.put("roles",roles);
            map.put("permissions",permissionList);
            System.out.println(map);
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * 存储token 到cookie
     * @param ssoToken
     * @param response
     */
    public void storeSSOToken(String token, ServerHttpResponse response,SSOToken ssoToken) {
        if("UUID".equals(ssoToken.getType())){
            ResponseCookie cookie = ResponseCookie.from(SSO_LOGIN_TOKEN_UUID, token).maxAge(Duration.of(TOKEN_EXPIRE_TIME_MINUTE, ChronoUnit.MINUTES))
                    .path("/sso/login").build();
            response.addCookie(cookie);
        }else{
            ResponseCookie cookie = ResponseCookie.from(SSO_LOGIN_TOKEN_JWT, token).maxAge(Duration.of(TOKEN_EXPIRE_TIME_MINUTE, ChronoUnit.MINUTES))
                    .path("/sso/login").build();
            response.addCookie(cookie);
        }
    }

    /**
     * 返回访问token
     * @param request
     * @return
     */
    public String getAccessToken(ServerHttpRequest request) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if(cookies.containsKey(SSO_LOGIN_TOKEN_UUID))
            return cookies.getFirst(SSO_LOGIN_TOKEN_UUID).getValue();
        else
            return cookies.getFirst(SSO_LOGIN_TOKEN_JWT).getValue();
    }

    /**
     * 通过访问token 获取用户信息，jwt token是用户自己解析
     * @param accessToken
     * @param request
     * @return
     */
    public Map<String,Object> getUserInfo(String accessToken,ServerHttpRequest request) {
        String jsonUserStr = redisTemplate.opsForValue().get(accessToken + SSO_REDIS_TOKEN_SPACE + IpUtils.getIp(request)).block();
        if(StringUtils.isEmpty(jsonUserStr)){
            throw new NullPointerException("Token不存在，请先访问http://admin.lhstack.xyz/sso/login?token=${token}&service=${service}获取token");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonUserStr);
        User user = jsonObject.getObject("userInfo", User.class);
        if(user == null){
            throw new NullPointerException("用户不存在，请先访问http://admin.lhstack.xyz/sso/login?token=${token}&service=${service}获取token");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("userInfo",user);
        List<Role> roles = jsonObject.getObject("roles", new TypeReference<List<Role>>() {
        });
        if(null != roles && roles.size() > 0){
            map.put("roles",roles);
        }
        List<Permission> permissions = jsonObject.getObject("permissions", new TypeReference<List<Permission>>() {
        });
        if(null != permissions && permissions.size() > 0){
            map.put("permissions",permissions);
        }
        return map;
    }
}
