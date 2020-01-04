package com.lhstack.service.permission.api;

import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.api.ApiInfo;
import com.lhstack.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IApiInfoService extends IBaseService<Long, ApiInfo> {
    ApiInfo findByHandlerMethod(String uniqueMethod);

    Integer updateAuthorityType(Integer type, Long id,String description);

    List<Permission> listPermission(Long apiInfoId);

    List<Role> listRoles(Long apiInfoId);

    void updatePermission(Long[] permissionIds, Long apiId);

    void updateRoles(Long[] roleIds, Long apiId);

    Page<ApiInfo> findByExample(Integer page, Integer size, ApiInfo apiInfo);
}
