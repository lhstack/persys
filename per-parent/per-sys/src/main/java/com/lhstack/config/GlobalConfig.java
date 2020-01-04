package com.lhstack.config;

import com.lhstack.service.permission.impl.api.ApiInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Configuration
public class GlobalConfig {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private ApiInfoServiceImpl apiInfoService;

    @Bean
    public ApplicationRunner applicationRunner(){
        return (applicationArguments) ->{
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
            handlerMethods.entrySet().forEach(item ->{
                apiInfoService.save(item.getKey(),item.getValue());
            });
        };
    }
}
