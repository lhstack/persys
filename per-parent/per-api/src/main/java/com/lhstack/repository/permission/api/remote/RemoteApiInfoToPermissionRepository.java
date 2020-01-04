package com.lhstack.repository.permission.api.remote;

import com.lhstack.entity.permission.api.remote.RemoteApiInfoToPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ClassName: RemoteApiInfoToPermissionRepository
 * Description:
 * date: 2019/12/25 14:08
 *
 * @author lhstack
 * @since
 */
public interface RemoteApiInfoToPermissionRepository extends JpaRepository<RemoteApiInfoToPermission,Long> {
    List<RemoteApiInfoToPermission> findByApiInfoId(Long id);

    void deleteByApiInfoId(Long apiId);
}
