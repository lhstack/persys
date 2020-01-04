package com.lhstack.aspect.permission;

import java.lang.annotation.*;

/**
 * 动态鉴权
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DynAuthority {
}
