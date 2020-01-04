package com.lhstack.upload.annotation;

import com.lhstack.upload.client.config.PerSysOSSConfiguration;
import com.lhstack.upload.client.properties.OSSClientType;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * author: hp
 * date: 2020/1/3
 **/
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(PerSysOSSConfiguration.class)
public @interface EnablePersysOss {

    /**
     * 设置oss客户端baseURL
     * @return
     */
    String baseUrl() default "";

    /**
     * 设置oss服务端申请的token
     * @return
     */
    String token() default "";

    /**
     * 客户端类型
     * @return
     */
    OSSClientType type() default OSSClientType.OKHTTP;
}
