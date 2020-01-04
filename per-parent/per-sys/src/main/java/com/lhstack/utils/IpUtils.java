package com.lhstack.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class IpUtils {
    public static String getIp(ServerHttpRequest request){
        if(request == null){
            return "localhost";
        }
        HttpHeaders headers = request.getHeaders();
        if(headers.containsKey("X-Real-IP")){
            return headers.getFirst("X-Real-IP");
        }
        return request.getRemoteAddress().getAddress().getHostAddress();
    }
}
