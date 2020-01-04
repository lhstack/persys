package com.lhstack.config.redis;

import com.lhstack.config.security.holder.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class GenertorKey {

    public String getKey(Object ...args){
        String key = Arrays.asList(args).stream().map(Object::toString)
                .collect(Collectors.toList()).toString();
        return key + "::" + SecurityContextHolder.getUsername();
    }
}
