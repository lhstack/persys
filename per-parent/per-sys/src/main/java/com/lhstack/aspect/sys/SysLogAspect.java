package com.lhstack.aspect.sys;

import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.log.SysLog;
import com.lhstack.entity.permission.User;
import com.lhstack.service.log.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Configuration
@Slf4j
public class SysLogAspect {

    @Autowired
    private ISysLogService logService;

    @Around("execution(public * *(..)) && @annotation(com.lhstack.aspect.sys.SysLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        List<Object> args = Arrays.asList(point.getArgs());
        Object target = point.getTarget();
        Object aThis = point.getThis();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class returnType = signature.getReturnType();
        String[] parameterNames = signature.getParameterNames();
        Authentication authentication = SecurityContextHolder.get();
        User user = new User();
        user.setUsername("test");
        if(authentication != null){
            user = (User)authentication.getPrincipal();
        }
        Long s = System.currentTimeMillis();
        Object proceed = null;
        SysLog sysLog = new SysLog();
        try{
            String parametersString = Arrays.asList(parameterNames).stream().collect(Collectors.joining(","));
            sysLog.setIp(SecurityContextHolder.getIp())
                    .setMethod(target.getClass().getName() + "." + method.getName() + "(@)".replace("@",parametersString))
                    .setOperationTime(new Date())
                    .setParameters(Arrays.asList(parameterNames).toString())
                    .setUsername(SecurityContextHolder.getUsername())
                    .setPermissions(SecurityContextHolder.getPermissions().toString())
                    .setRoles(SecurityContextHolder.getRoles().toString())
                    .setArgs(args.toString())
                    .setState(true);
            proceed = point.proceed();
            return proceed;
        }catch (Exception e) {
            e.printStackTrace();
            sysLog.setExMsg(e.getMessage());
            sysLog.setState(false);
            throw new RuntimeException(e.getMessage());
        }finally {
            s = System.currentTimeMillis() - s;
            log.info("user is <---> {} ---> access method name as {} , parameter names as {} , args as {} , target as {} , return type as {} , this as {} , result as {} ---> execute date as {} millisecond",
                    user.getUsername(),
                    method.getName(),
                    Arrays.asList(parameterNames),
                    args,target,
                    returnType,
                    aThis,
                    proceed,
                    s);
            logService.save(sysLog);
        }
    }
}
