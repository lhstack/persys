package com.lhstack.service.permission;

import com.github.pagehelper.PageInfo;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.vo.RoleAndPermissionVO;
import com.lhstack.service.IBaseService;

import java.util.List;

public interface IRoleService extends IBaseService<Long, Role> {


    List<Role> findAllByIgnoreRoleNames(String... ignoreRoleNames);

    /**
     * 根据用户id查询关联的角色
     * @param uid
     * @return
     */
    List<Role> findByUid(Long uid);

    /**
     * 查询忽略超级管理员以及拥有相应权限的角色
     * @param page
     * @param size
     * @param isAdmin
     * @param permissions
     * @return
     */
    PageInfo<RoleAndPermissionVO> findAllByIgnoreNameAndNotExistAdmin(Integer page, Integer size, Boolean isAdmin, String... permissions);

    Integer updateRoleAndRelevancePermission(Long[] pids, Role role) throws Exception;

    Role saveRoleAndRelevancePermission(Long[] pids, Role role);

    List<Role> findByRoleNameIn(String... roles);

    List<Role> findAllByIdIn(Long... roleIds);
}
