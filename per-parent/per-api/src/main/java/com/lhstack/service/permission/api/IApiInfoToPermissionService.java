package com.lhstack.service.permission.api;

import com.lhstack.entity.permission.api.ApiInfoToPermission;
import com.lhstack.service.IBaseService;

import java.util.List;

public interface IApiInfoToPermissionService extends IBaseService<Long, ApiInfoToPermission> {
    List<ApiInfoToPermission> findByApiInfoId(Long id);

    void deleteByApiId(Long apiId);
}
