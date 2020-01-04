package com.lhstack.aspect.permission;

import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.controller.permission.PermissionsQueryConst;
import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.api.ApiInfo;
import com.lhstack.entity.permission.api.ApiInfoToPermission;
import com.lhstack.entity.permission.api.ApiInfoToRole;
import com.lhstack.service.permission.IPermissionService;
import com.lhstack.service.permission.IRoleService;
import com.lhstack.service.permission.api.IApiInfoService;
import com.lhstack.service.permission.api.IApiInfoToPermissionService;
import com.lhstack.service.permission.api.IApiInfoToRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Configuration
@Slf4j
public class DynAuthorityAspect {

    @Autowired
    private IApiInfoService apiInfoService;

    @Autowired
    private IApiInfoToRoleService apiInfoToRoleService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IApiInfoToPermissionService apiInfoToPermissionService;

    @Autowired
    private IPermissionService permissionService;

    @Around("execution(* com.lhstack.controller..*(..)) && @annotation(com.lhstack.aspect.permission.DynAuthority)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isAdmin = SecurityContextHolder.getRoles().stream().anyMatch(item -> {
            return StringUtils.equals(item, "ADMIN");
        });
        if(isAdmin){
            return joinPoint.proceed();
        }
        String methodKey = getMethodUniqueName(joinPoint.getSignature(),joinPoint);
        ApiInfo handlerMethod = apiInfoService.findByHandlerMethod(methodKey);
        if(ObjectUtils.isNotEmpty(handlerMethod)){
            validAuthority(handlerMethod);
        }
        return joinPoint.proceed();
    }

    private void validAuthority(ApiInfo apiInfo) throws Exception {
        switch (apiInfo.getAuthorityType()){
            case PermissionsQueryConst
                    .HAS_ROLE :{
                if(!validRole(apiInfo.getId())){
                    throw new RuntimeException("角色校验失败");
                }
            };break;
            case PermissionsQueryConst.HAS_PERMISSION:{
                if(!validPermission(apiInfo.getId())){
                    throw new RuntimeException("权限校验失败");
                }
            };break;
            case PermissionsQueryConst.AUTHORITY_ANY:{
                if(!validAnyAuthority(apiInfo.getId())){
                    throw new RuntimeException("授权校验失败");
                }
            };break;
            case PermissionsQueryConst.AUTHORITY_ALL:{
                if(!validAllAuthority(apiInfo.getId())){
                    throw new RuntimeException("授权校验失败");
                }
            };break;
        }
    }

    private boolean validAllAuthority(Long id) {
        String[] permissions = SecurityContextHolder.getPermissions().toArray(new String[SecurityContextHolder.getPermissions().size()]);
        Map<Long, String> mapPermissions = permissionService.findByPermissionNameIn(permissions).stream().collect(Collectors.toMap(Permission::getId, Permission::getPermissionName));
        List<ApiInfoToPermission> apiInfoToPermissions = apiInfoToPermissionService.findByApiInfoId(id);
        boolean permissionState = apiInfoToPermissions.stream().allMatch(item -> {
            return mapPermissions.containsKey(item.getPermissionId());
        });
        if( apiInfoToPermissions.size() == 0){
            permissionState = true;
        }
        String[] roles = SecurityContextHolder.getRoles().toArray(new String[SecurityContextHolder.getRoles().size()]);
        Map<Long, String> mapRole = roleService.findByRoleNameIn(roles).stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));
        List<ApiInfoToRole> apiInfoToRoleList = apiInfoToRoleService.findByApiInfoId(id);
        boolean roleState = apiInfoToRoleList.stream().allMatch(item -> {
            return mapRole.containsKey(item.getRoleId());
        });
        if( apiInfoToRoleList.size() == 0){
            roleState = true;
        }
        return permissionState && roleState;
    }

    private boolean validAnyAuthority(Long id) {
        return validPermission(id) || validRole(id);
    }

    private boolean validPermission(Long id) {
        String[] permissions = SecurityContextHolder.getPermissions().toArray(new String[SecurityContextHolder.getPermissions().size()]);
        Map<Long, String> mapPermissions = permissionService.findByPermissionNameIn(permissions).stream().collect(Collectors.toMap(Permission::getId, Permission::getPermissionName));
        List<ApiInfoToPermission> apiInfoToPermissions = apiInfoToPermissionService.findByApiInfoId(id);
        return apiInfoToPermissions.stream().anyMatch(item -> {
            return mapPermissions.containsKey(item.getPermissionId());
        }) || apiInfoToPermissions.size() == 0;
    }

    private boolean validRole(Long id) {
        String[] roles = SecurityContextHolder.getRoles().toArray(new String[SecurityContextHolder.getRoles().size()]);
        Map<Long, String> mapRole = roleService.findByRoleNameIn(roles).stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));
        List<ApiInfoToRole> apiInfoToRoleList = apiInfoToRoleService.findByApiInfoId(id);
        return apiInfoToRoleList.stream().anyMatch(item -> {
            return mapRole.containsKey(item.getRoleId());
        }) || apiInfoToRoleList.size() == 0;
    }

    private String getMethodUniqueName(Signature signature,ProceedingJoinPoint proceedingJoinPoint) {
        if(signature instanceof MethodSignature){
            MethodSignature methodSignature = (MethodSignature) signature;
            String uniqueMethod = proceedingJoinPoint.getTarget().getClass().getName() + "." + methodSignature.getName();
            String parameter = Arrays.asList(methodSignature.getParameterNames()).stream().collect(Collectors.joining(","));
            uniqueMethod = uniqueMethod + "(" + parameter + ")" + "---------->" + methodSignature.getReturnType().getName();
            return uniqueMethod;
        }
        return null;
    }

}
