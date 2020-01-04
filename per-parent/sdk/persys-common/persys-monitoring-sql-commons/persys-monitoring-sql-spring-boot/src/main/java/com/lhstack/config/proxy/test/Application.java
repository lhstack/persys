package com.lhstack.config.proxy.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;

/**
 * CLASSNAME: ${CLASS_NAME}
 * author: hp
 * date: 2020/1/3
 **/
@SpringBootApplication
public class Application implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        DataSource bean = run.getBean(DataSource.class);
        System.out.println(bean);
    }
}
