package com.lhstack.repository.permission.api.remote;

import com.lhstack.entity.permission.api.remote.RemoteApiInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: RemoteApiInfoRepotiroty
 * Description:
 * date: 2019/12/25 14:07
 *
 * @author lhstack
 * @since
 */
public interface RemoteApiInfoRepository extends JpaRepository<RemoteApiInfo,Long> {
    Page<RemoteApiInfo> findAllByNamespace(String namespace, Pageable pageable);

    RemoteApiInfo findByHandlerMethodAndNamespace(String uniqueMethodKey, String namespace);

    @Modifying
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class},readOnly = false,propagation = Propagation.REQUIRED)
    @Query("UPDATE RemoteApiInfo SET authorityType = ?1 , description = ?3 WHERE id = ?2")
    Integer updateAuthorityType(Integer type, Long id, String description);

    @Query("SELECT DISTINCT namespace FROM RemoteApiInfo ")
    List<String> findAllNamespaces();

    void deleteByNamespace(String namespace);
}
