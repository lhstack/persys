package com.lhstack.config.proxy.annotation;

import com.lhstack.config.proxy.autoconfig.MonitoringSqlConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * author: hp
 * date: 2020/1/3
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MonitoringSqlConfiguration.class)
public @interface EnableMonitoringSql {

    boolean enable() default true;
    /**
     * 设置应用名称
     * @return
     */
    String applicationName() default "default";

    /**
     * 设置签名
     * @return
     */
    String singingKey() default "asdf153zdg5212sg";

    /**
     * 设置监控数据提交地址
     * @return
     */
    String monitoringSqlUri() default "https://admin.lhstack.xyz/remote/sql/push";
}
