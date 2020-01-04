package com.lhstack.annotation;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Transactional(rollbackFor = {Exception.class,RuntimeException.class},readOnly = false,propagation = Propagation.REQUIRED)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RollbackTransactional {
}
