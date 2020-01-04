package com.lhstack.repository.permission.api;

import com.lhstack.entity.permission.api.ApiInfoToRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApiInfoToRoleRepository extends JpaRepository<ApiInfoToRole,Long> {
    List<ApiInfoToRole> findByApiInfoId(Long id);

    @Transactional(readOnly = false,rollbackFor = {Exception.class,RuntimeException.class},propagation = Propagation.REQUIRED)
    void deleteByApiInfoId(Long apiId);
}
