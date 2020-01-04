package com.lhstack.service.permission.api.remote;

import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.api.remote.RemoteApiInfo;
import com.lhstack.service.IBaseService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: IRemoteApiInfoService
 * Description:
 * date: 2019/12/25 14:10
 *
 * @author lhstack
 * @since
 */
public interface IRemoteApiInfoService extends IBaseService<Long, RemoteApiInfo> {
    List<Permission> listPermission(Long apiInfoId);

    List<Role> listRoles(Long apiInfoId);

    void updatePermission(Long[] permissionIds, Long apiId);

    void updateRoles(Long[] roleIds, Long apiId);

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    Page<RemoteApiInfo> findAllByExample(RemoteApiInfo remoteApiInfo, Integer page, Integer size);

    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    Page<RemoteApiInfo> findAllByNamespace(String namespace, Integer page, Integer size);

    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    Integer updateAuthorityType(Integer type, Long id, String description);

    List<String> findAllNamespaces();

    void deleteByNamespace(String namespace);
}
