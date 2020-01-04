package com.lhstack.aspect.sys;

import java.lang.annotation.*;

@Target(
        ElementType.METHOD
)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {
}
