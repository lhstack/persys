package com.lhstack.service.permission.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.aspect.sys.SysLog;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.permission.Permission;
import com.lhstack.mapper.permission.PermissionMapper;
import com.lhstack.repository.permission.PermissionRepository;
import com.lhstack.service.permission.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "permissions",allEntries = true)
    public Permission save(Permission entity) {
        entity.setId(null);
        return permissionRepository.save(entity);
    }

    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "permissions",allEntries = true)
    public Permission update(Long s, Permission entity) {
        entity.setId(s);
        return permissionRepository.save(entity);
    }

    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "permissions",allEntries = true)
    public void delete(Permission entity) {
        permissionRepository.delete(entity);
    }

    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "permissions",allEntries = true)
    public void deleteById(Long s) {
        permissionRepository.deleteById(s);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "permissions",key = "@genertorKey.getKey('findById',#p0)")
    public Permission findById(Long s) {
        return permissionRepository.findById(s).orElse(null);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "permissions",key = "@genertorKey.getKey('findAll')")
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "permissions",key = "@genertorKey.getKey('findAll',#p0,#p1)")
    public Page<Permission> findAll(Integer page, Integer size) {
        if(page < 1){
            page = 1;
        }
        return permissionRepository.findAll(PageRequest.of(page - 1,size));
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "permissions",key = "@genertorKey.getKey('findByRid',#p0)")
    public List<Permission> findByRid(Long id) {
        return permissionMapper.findByRid(id);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "permissions",key = "@genertorKey.getKey('findAllByExistAdminAndPermissions',#p0,#p1)")
    public PageInfo<Permission> findAllByExistAdminAndPermissions(Integer page,Integer size,String... permissions) {
        PageHelper.startPage(page,size);
        return PageInfo.of(permissionMapper.findAllByExistAdminAndRoles(SecurityContextHolder.getRoles().contains("ADMIN"),permissions));
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "permissions",key = "@genertorKey.getKey('findAllByExistAdminPermissions')")
    public List<Permission> findAllByExistAdminPermissions() {
        return permissionMapper.findAllByExistAdminAndRoles(true);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "permissions",key = "@genertorKey.getKey('findByMid',#p0)")
    public List<Permission> findByMid(Long mid) {

        return permissionMapper.findByMid(mid);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List<Permission> findByPermissionNameIn(String... permissions) {
        return permissionRepository.findAllByPermissionNameIn(permissions);
    }

    @Override
    public List<Permission> findAllByIdIn(Long... permissionIds) {
        return permissionRepository.findAllByIdIn(permissionIds);
    }
}
