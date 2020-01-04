package com.lhstack.aspect.permission;

import java.lang.annotation.*;

/**
 * 项目初始化权限
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface InitAuthority {
    String value() default "";

    String description() default "";
}
