package com.lhstack.repository.permission;

import com.lhstack.entity.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission,Long> {

    List<Permission> findAllByPermissionNameIn(String ... permissionName);

    List<Permission> findAllByIdIn(Long... permissionIds);
}
