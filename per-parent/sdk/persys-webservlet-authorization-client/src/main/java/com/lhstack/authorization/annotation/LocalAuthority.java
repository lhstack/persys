package com.lhstack.authorization.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LocalAuthority {
    /**
     * isLogin,
     * hasRole(ROLES...,...)
     * hasPermission(Permissions...,...)
     * anyAuthority(ROLES,PERMISSIONS...)
     * allAuthority(ROLES,PERMISSIONS...
     * @return
     */
    String value() default "";
}
