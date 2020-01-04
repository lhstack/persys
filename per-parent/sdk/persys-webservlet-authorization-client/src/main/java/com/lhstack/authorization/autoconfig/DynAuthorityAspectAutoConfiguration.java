package com.lhstack.authorization.autoconfig;

import com.lhstack.authorization.annotation.LocalAuthority;
import com.lhstack.authorization.client.ApiInfoClient;
import com.lhstack.authorization.pojo.SsoAuthorityInfo;
import com.lhstack.authorization.pojo.SsoPermission;
import com.lhstack.authorization.pojo.SsoRole;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.LoginException;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: DynAuthorityAspectAutoConfiguration
 * Description:
 * date: 2019/12/24 21:10
 *
 * @author lhstack
 * @since
 */
@ConditionalOnExpression("${persys.enable:true}")
@Aspect
public class DynAuthorityAspectAutoConfiguration {


    @Autowired
    private ApiInfoClient apiInfoClient;

    @Around("@annotation(com.lhstack.authorization.annotation.DynAuthority)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        check(joinPoint);
        return joinPoint.proceed();
    }

    @Bean
    public ApiInfoClient apiInfoClient(){
        return new ApiInfoClient();
    }


    private SsoAuthorityInfo getSsoAuthorityInfo(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession();
        return (SsoAuthorityInfo) session.getAttribute(PerSysSsoAutoConfiguration.SSO_USER_INFO_KEY);
    }

    private void check(ProceedingJoinPoint joinPoint) throws Exception {
        SsoAuthorityInfo authorityInfo = getSsoAuthorityInfo();
        if(ObjectUtils.isEmpty(authorityInfo)){
            throw new LoginException("No Authority");
        }
        String uniqueMethodKey = getMethodUniqueName(joinPoint.getSignature(),joinPoint);
        apiInfoClient.checkRemoteAuthority(uniqueMethodKey,authorityInfo.getUserInfo().getUsername());
    }

    private String getMethodUniqueName(Signature signature, ProceedingJoinPoint proceedingJoinPoint) {
        if(signature instanceof MethodSignature){
            MethodSignature methodSignature = (MethodSignature) signature;
            String uniqueMethod = proceedingJoinPoint.getTarget().getClass().getName() + "." + methodSignature.getName();
            String parameter = Arrays.asList(methodSignature.getParameterNames()).stream().collect(Collectors.joining(","));
            uniqueMethod = uniqueMethod + "(" + parameter + ")" + "---------->" + methodSignature.getReturnType().getName();
            return uniqueMethod;
        }
        return null;
    }

    @Around("@annotation(localAuthority)")
    public Object localAuthority(ProceedingJoinPoint joinPoint, LocalAuthority localAuthority) throws Throwable {
        validAuthority(localAuthority);
        return joinPoint.proceed();
    }

    private void validAuthority(LocalAuthority localAuthority) throws Exception {
        SsoAuthorityInfo authorityInfo = getSsoAuthorityInfo();
        if(ObjectUtils.isEmpty(authorityInfo)){
            throw new LoginException("No Authority");
        }
        String value = localAuthority.value();
        validHasRole(value,authorityInfo);
        validHasPermission(value,authorityInfo);
        validAnyAuthority(value,authorityInfo);
        validAllAuthority(value,authorityInfo);
        validIsLogin(value,authorityInfo);
    }

    private void validIsLogin(String value, SsoAuthorityInfo authorityInfo) throws AuthenticationException {
        if(value.toLowerCase().equals("isLogin")){
            if(ObjectUtils.isEmpty(authorityInfo) || ObjectUtils.isEmpty(authorityInfo.getUserInfo())){
                throw new AuthenticationException("Please Login");
            }
        }
    }

    private void validAllAuthority(String value, SsoAuthorityInfo authorityInfo) throws AuthenticationException {
        if(value.matches("allAuthority\\((\\w|,)*\\)")){
            String[] anyAuthorities = value.replace("allAuthority", "").replace("(", "").replace(")", "").split(",");
            HashSet<String> set = new HashSet<>(Arrays.asList(anyAuthorities));
            List<String> roles = authorityInfo.getRoleList().stream().map(item -> "ROLE_" + item.getRoleName()).collect(Collectors.toList());
            List<String> permissionList = authorityInfo.getPermissionList().stream().map(item -> "PERMISSION_" + item.getPermissionName()).collect(Collectors.toList());
            roles.addAll(permissionList);
            Boolean flag = roles.stream().allMatch(item -> set.contains(item));
            if(!flag){
                throw new AuthenticationException(String.format("No Roles and Permission [%s]",set.stream().map(String::toString).collect(Collectors.joining(","))));
            }
        }
    }

    private void validAnyAuthority(String value, SsoAuthorityInfo authorityInfo) throws AuthenticationException {
        if(value.matches("anyAuthority\\((\\w|,)*\\)")){
            String[] anyAuthorities = value.replace("anyAuthority", "").replace("(", "").replace(")", "").split(",");
            HashSet<String> set = new HashSet<>(Arrays.asList(anyAuthorities));
            List<String> roles = authorityInfo.getRoleList().stream().map(item -> "ROLE_" + item.getRoleName()).collect(Collectors.toList());
            List<String> permissionList = authorityInfo.getPermissionList().stream().map(item -> "PERMISSION_" + item.getPermissionName()).collect(Collectors.toList());
            roles.addAll(permissionList);
            Boolean flag = roles.stream().anyMatch(item -> set.contains(item));
            if(!flag){
                throw new AuthenticationException(String.format("No Roles or Permission [%s]",set.stream().map(String::toString).collect(Collectors.joining(","))));
            }
        }

    }

    private void validHasPermission(String value, SsoAuthorityInfo authorityInfo) throws AuthenticationException {
        if(value.matches("hasPermission\\((\\w|,)*\\)")){
            String[] hasPermissions = value.replace("hasPermission", "").replace("(", "").replace(")", "").split(",");
            HashSet<String> set = new HashSet<>(Arrays.asList(hasPermissions));
            Boolean flag = authorityInfo.getPermissionList().stream().anyMatch(item ->set.contains(item.getPermissionName()));
            if(!flag){
                throw new AuthenticationException(String.format("No Permission [%s]",authorityInfo.getPermissionList().stream().map(SsoPermission::getPermissionName).collect(Collectors.joining(","))));
            }
        }
    }

    private void validHasRole(String value,SsoAuthorityInfo authorityInfo) throws AuthenticationException {
        if(value.matches("hasRole\\((\\w|,)*\\)")){
            String[] hasRoles = value.replace("hasRole", "").replace("(", "").replace(")", "").split(",");
            HashSet<String> set = new HashSet<>(Arrays.asList(hasRoles));
            Boolean flag = authorityInfo.getRoleList().stream().anyMatch(item ->set.contains(item.getRoleName()));
            if(!flag){
                throw new AuthenticationException(String.format("No Roles [%s]",authorityInfo.getRoleList().stream().map(SsoRole::getRoleName).collect(Collectors.joining(","))));
            }
        }
    }
}
