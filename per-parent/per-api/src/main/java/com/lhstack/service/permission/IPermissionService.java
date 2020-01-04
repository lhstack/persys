package com.lhstack.service.permission;

import com.github.pagehelper.PageInfo;
import com.lhstack.entity.permission.Permission;
import com.lhstack.service.IBaseService;

import java.util.List;

public interface IPermissionService extends IBaseService<Long, Permission> {
    /**
     * 根据角色id获取关联的权限
     * @param id
     * @return
     */
    List<Permission> findByRid(Long id);

    /**
     * 查询排除管理员以及permissions的权限
     * @param permissions
     * @return
     */
    PageInfo<Permission> findAllByExistAdminAndPermissions(Integer page, Integer size, String... permissions);

    List<Permission> findAllByExistAdminPermissions();

    List<Permission> findByMid(Long mid);

    List<Permission> findByPermissionNameIn(String... permissions);

    List<Permission> findAllByIdIn(Long... permissionIds);
}
