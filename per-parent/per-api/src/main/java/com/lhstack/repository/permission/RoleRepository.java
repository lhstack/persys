package com.lhstack.repository.permission;

import com.lhstack.entity.permission.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByIdAndRoleNameNotIn(Long id, String roleName);

    List<Role> findAllByRoleNameNotIn(String... admin);

    List<Role> findByRoleNameIn(String ...roleName);

    List<Role> findAllByIdIn(Long... roleIds);
}
