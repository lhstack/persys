package com.lhstack.upload.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASSNAME: ${CLASS_NAME}
 * author: hp
 * date: 2020/1/3
 **/
@SpringBootApplication
public class Application implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(bean);
        return bean;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
