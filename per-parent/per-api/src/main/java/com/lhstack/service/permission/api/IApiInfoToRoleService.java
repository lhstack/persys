package com.lhstack.service.permission.api;

import com.lhstack.entity.permission.api.ApiInfoToRole;
import com.lhstack.service.IBaseService;

import java.util.List;

public interface IApiInfoToRoleService extends IBaseService<Long, ApiInfoToRole> {
    List<ApiInfoToRole> findByApiInfoId(Long id);
}
