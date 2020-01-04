package com.lhstack.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lhstack.entity.permission.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class TokenParseUtils {
    public static Authentication parseAuthentication(String v){
        JSONObject jsonObject = JSONObject.parseObject(v);
        String authorities = JSONObject.toJSONString(jsonObject.get("authorities"));
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = JSONObject.parseObject(authorities, new TypeReference<List<SimpleGrantedAuthority>>() {
        });
        String principal = JSONObject.toJSONString(jsonObject.get("principal"));
        User user = JSONObject.parseObject(principal,User.class);
        return new UsernamePasswordAuthenticationToken(user,null,simpleGrantedAuthorities);
    }
}
