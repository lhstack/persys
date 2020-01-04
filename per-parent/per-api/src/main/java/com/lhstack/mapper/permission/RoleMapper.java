package com.lhstack.mapper.permission;

import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.vo.RoleAndPermissionVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    List<Role> findByUid(Long uid);

    Integer deleteByUserRids(List<Long> rids);

    Integer insertByRids(@Param("id") Long id,@Param("rids") List<Long> rids);

    /**
     * 查询是管理员或者不是管理员移除角色的集合
     * @param isAdmin
     * @param permissions
     * @return
     */
    List<RoleAndPermissionVO> findAllByIgnoreNameAndNotExistAdmin(@Param("isAdmin") Boolean isAdmin, @Param("permissions") String... permissions);

    List<String> findAllByIgnoreNameAndNotExistAdminCount(@Param("isAdmin") Boolean isAdmin, @Param("permissions") String... permissions);

    Long deleteByRoleAndPids(@Param("rid") Long rid);

    Long insertByRoleAndPids(@Param("pids") Long[] pids,@Param("rid") Long id);

    void deleteByRids(List<Long> ids);

    List<Role> findAllByRoleNameNotIn(@Param("isAdmin") boolean admin, @Param("roleNames")String[] ignoreRoleNames);

    void deleteByUid(Long id);
}
