package com.lhstack.controller.excontroller;

import com.lhstack.entity.layui.LayuiResut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionController{


    @ExceptionHandler(AuthenticationServiceException.class)
    public LayuiResut<Object> checkAuthenticationExceptionHandler(AuthenticationServiceException e){
        LayuiResut<Object> layuiResut = new LayuiResut<>();
        layuiResut.setCode(1)
                .setMsg(e.getMessage());
        log.debug("catch exception as {} , exception msg as {} , handler method as {}",e,e.getMessage(),"checkAuthenticationExceptionHandler");
        return layuiResut;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public LayuiResut<String> exceptionHandler(IllegalArgumentException e){
        e.printStackTrace();
        return LayuiResut.buildError(e.getMessage(),500);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public LayuiResut<Map<String,String>> runtimeExceptionHandler(WebExchangeBindException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        StringBuilder msgs = new StringBuilder();
        Map<String,String> map = new HashMap<>();
        fieldErrors.forEach(item ->{
            map.put(item.getField(),item.getDefaultMessage());
            msgs.append(item.getDefaultMessage() + ",");
        });
        String msg = msgs.substring(0, msgs.length() - 1);

        return LayuiResut.buildError(map,msg,500);
    }

    @ExceptionHandler(RegistryException.class)
    public LayuiResut<Object> errExceptionHandler(RegistryException e){
        LayuiResut<Object> layuiResut = new LayuiResut<>();
        layuiResut.setCode(1)
                .setMsg(e.getMessage());
        return layuiResut;
    }

    @ExceptionHandler(RuntimeException.class)
    public LayuiResut<Object> exceptionHandler(RuntimeException e){
        LayuiResut<Object> layuiResut = new LayuiResut<>();
        layuiResut.setCode(500)
                .setMsg(e.getMessage());
        return layuiResut;
    }

    @ExceptionHandler(InsertException.class)
    public LayuiResut<Object> errExceptionHandler(InsertException e){
        LayuiResut<Object> layuiResut = new LayuiResut<>();
        layuiResut.setCode(1)
                .setMsg(e.getMessage());
        return layuiResut;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public LayuiResut<String> dataIntegrityViolationException(DataIntegrityViolationException e){
       return LayuiResut.buildError("服务器出现了异常，可能是数据库已经存在此用户名或者昵称，请修改重试",403);
    }
}
