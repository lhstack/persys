package com.lhstack.mapper.permission;

import com.lhstack.entity.permission.Permission;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> findByRid(Long id);

    List<Permission> findAllByExistAdminAndRoles(@Param("isAdmin") Boolean isAdmin, @Param("permissions") String... permissions);

    void deleteById(Long s);

    List<Permission> findByMid(Long mid);

    void deleteMenuAndPermissionByMid(Long id);

    void insertMenuAndPermissionByMid(@Param("mid") Long id,@Param("pids") Long[] pids);
}
