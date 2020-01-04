package com.lhstack.repository.permission.api;

import com.lhstack.entity.permission.api.ApiInfoToPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApiInfoToPermissionRepository extends JpaRepository<ApiInfoToPermission,Long> {
    List<ApiInfoToPermission> findByApiInfoId(Long id);

    @Transactional(readOnly = false,rollbackFor = {Exception.class,RuntimeException.class},propagation = Propagation.REQUIRED)
    void deleteByApiInfoId(Long apiId);
}
