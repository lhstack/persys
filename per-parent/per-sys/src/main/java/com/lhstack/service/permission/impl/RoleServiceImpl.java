package com.lhstack.service.permission.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.aspect.sys.SysLog;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.controller.excontroller.InsertException;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.vo.RoleAndPermissionVO;
import com.lhstack.mapper.permission.RoleMapper;
import com.lhstack.repository.permission.RoleRepository;
import com.lhstack.service.permission.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;


    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "roles",allEntries = true)
    public Role save(Role entity) {
        entity.setId(null);
        return roleRepository.save(entity);
    }

    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "roles",allEntries = true)
    public Role update(Long s, Role role) {
        role.setId(s);
        return roleRepository.save(role);
    }

    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "roles",allEntries = true)
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Override
    @SysLog
    @RollbackTransactional
    @CacheEvict(cacheNames = "roles",allEntries = true)
    public void deleteById(Long s) {
        roleMapper.deleteByRids(Arrays.asList(s));
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "roles",key = "@genertorKey.getKey('findById',#p0)")
    public Role findById(Long s) {
        return roleRepository.findById(s).orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "roles",key = "@genertorKey.getKey('findAll')")
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "roles",key = "@genertorKey.getKey('findAllByIgnoreRoleNames')")
    public List<Role> findAllByIgnoreRoleNames(String... ignoreRoleNames) {

        return roleMapper.findAllByRoleNameNotIn(SecurityContextHolder.getRoles().contains("ADMIN"),ignoreRoleNames);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "roles",key = "@genertorKey.getKey('findAll',#p0,#p1)")
    public Page<Role> findAll(Integer page, Integer size) {
        if(page < 1){
            page = 1;
        }
        return roleRepository.findAll(PageRequest.of(page - 1,size));
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List<Role> findByUid(Long uid) {
        return roleMapper.findByUid(uid);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "roles",key = "@genertorKey.getKey('findAllByIgnoreNameAndNotExistAdmin',#p0,#p1,#p2)")
    public PageInfo<RoleAndPermissionVO> findAllByIgnoreNameAndNotExistAdmin(Integer page, Integer size, Boolean isAdmin, String... permissions) {
        PageHelper.startPage(page,size);
        PageInfo<RoleAndPermissionVO> permissionVoPageInfo = new PageInfo<>(roleMapper.findAllByIgnoreNameAndNotExistAdmin(isAdmin,permissions));
        return permissionVoPageInfo;
    }

    @Override
    @RollbackTransactional
    @SysLog
    @CacheEvict(cacheNames = "roles",allEntries = true)
    public Integer updateRoleAndRelevancePermission(Long[] pids, Role role) throws Exception{
        int resultCount = roleMapper.updateByPrimaryKey(role);
        if(resultCount < 1){
            throw new InsertException("角色更新失败");
        }
        roleMapper.deleteByRoleAndPids(role.getId());
        roleMapper.insertByRoleAndPids(pids,role.getId());
        return resultCount;
    }

    @Override
    @RollbackTransactional
    @SysLog
    @CacheEvict(cacheNames = "roles",allEntries = true)
    public Role saveRoleAndRelevancePermission(Long[] pids, Role role) {
        if(StringUtils.isEmpty(role.getRoleName()) || StringUtils.isEmpty(role.getLogogramName()) || StringUtils.isEmpty(role.getIcon())){
            throw new NullPointerException("角色不能有空项");
        }
        role.setId(null);
        Role result = roleRepository.save(role);
        if(pids.length > 0){
            roleMapper.insertByRoleAndPids(pids,result.getId());
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List<Role> findByRoleNameIn(String... roles) {
        return roleRepository.findByRoleNameIn(roles);
    }

    @Override
    public List<Role> findAllByIdIn(Long... roleIds) {
        return roleRepository.findAllByIdIn(roleIds);
    }
}
