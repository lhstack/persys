package com.lhstack.repository.permission.api.remote;

import com.lhstack.entity.permission.api.remote.RemoteApiInfoToRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ClassName: RemoteApiInfoToRoleRepository
 * Description:
 * date: 2019/12/25 14:09
 *
 * @author lhstack
 * @since
 */
public interface RemoteApiInfoToRoleRepository extends JpaRepository<RemoteApiInfoToRole,Long> {
    List<RemoteApiInfoToRole> findByApiInfoId(Long id);

    void deleteByApiInfoId(Long apiId);
}
