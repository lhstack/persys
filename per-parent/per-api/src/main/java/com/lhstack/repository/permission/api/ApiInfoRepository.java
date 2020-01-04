package com.lhstack.repository.permission.api;

import com.lhstack.entity.permission.api.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ApiInfoRepository extends JpaRepository<ApiInfo,Long>, JpaSpecificationExecutor<ApiInfo> {
    ApiInfo findByHandlerMethod(String uniqueMethod);

    @Modifying
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class},readOnly = false,propagation = Propagation.REQUIRED)
    @Query("UPDATE ApiInfo SET authorityType = ?1 , description = ?3 WHERE id = ?2")
    int update(Integer type,Long id,String description);
}
