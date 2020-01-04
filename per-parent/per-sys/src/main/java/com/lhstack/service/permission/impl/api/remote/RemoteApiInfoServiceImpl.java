package com.lhstack.service.permission.impl.api.remote;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.controller.permission.PermissionsQueryConst;
import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.User;
import com.lhstack.entity.permission.api.remote.RemoteApiInfo;
import com.lhstack.entity.permission.api.remote.RemoteApiInfoToPermission;
import com.lhstack.entity.permission.api.remote.RemoteApiInfoToRole;
import com.lhstack.repository.permission.api.remote.RemoteApiInfoRepository;
import com.lhstack.repository.permission.api.remote.RemoteApiInfoToPermissionRepository;
import com.lhstack.repository.permission.api.remote.RemoteApiInfoToRoleRepository;
import com.lhstack.service.permission.IPermissionService;
import com.lhstack.service.permission.IRoleService;
import com.lhstack.service.permission.IUserService;
import com.lhstack.service.permission.api.remote.IRemoteApiInfoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: RemoteApiInfoServiceImpl
 * Description:
 * date: 2019/12/25 14:11
 *
 * @author lhstack
 * @since
 */
@Service
public class RemoteApiInfoServiceImpl implements IRemoteApiInfoService {

    @Autowired
    private RemoteApiInfoRepository remoteApiInfoRepository;

    @Autowired
    private RemoteApiInfoToRoleRepository remoteApiInfoToRoleRepository;

    @Autowired
    private RemoteApiInfoToPermissionRepository remoteApiInfoToPermissionRepository;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserService userService;

    @Value("${persys.client.authority.singingKey}")
    private String singingKey;

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    public RemoteApiInfo save(RemoteApiInfo entity) throws Exception {
        return remoteApiInfoRepository.save(entity);
    }

    /**
    * Description: 根据apiInfoId查询权限
    * date: 2020/1/1 14:01
    * @author lhstack
    * @version
    * @since 1.8
    */
    @Override
    public List<Permission> listPermission(Long apiInfoId) {
        List<Long> ids = remoteApiInfoToPermissionRepository.findByApiInfoId(apiInfoId).stream().map(RemoteApiInfoToPermission::getPermissionId).collect(Collectors.toList());
        Long[] permissionIds = ids.toArray(new Long[ids.size()]);
        return permissionService.findAllByIdIn(permissionIds);
    }

    /**
    * Description: 根据apiInfoId查询角色
    * date: 2020/1/1 14:02
    * @author lhstack
    * @version
    * @since 1.8
    */
    @Override
    public List<Role> listRoles(Long apiInfoId) {
        List<Long> ids = remoteApiInfoToRoleRepository.findByApiInfoId(apiInfoId).stream().map(RemoteApiInfoToRole::getRoleId).collect(Collectors.toList());
        Long[] roleIds = ids.toArray(new Long[ids.size()]);
        return roleService.findAllByIdIn(roleIds);
    }

    /**
     * Description: 给apiId指定的apiInfo绑定权限
     * date: 2020/1/1 14:02
     * @author lhstack
     * @version
     * @since 1.8
     */
    @Override
    @RollbackTransactional
    public void updatePermission(Long[] permissionIds, Long apiId) {
        remoteApiInfoToPermissionRepository.deleteByApiInfoId(apiId);
        List<RemoteApiInfoToPermission> apiInfoToPermissions = Arrays.asList(permissionIds).stream().map(item -> {
            RemoteApiInfoToPermission apiInfoToPermission = new RemoteApiInfoToPermission();
            apiInfoToPermission.setPermissionId(item)
                    .setApiInfoId(apiId);
            return apiInfoToPermission;
        }).collect(Collectors.toList());
        remoteApiInfoToPermissionRepository.saveAll(apiInfoToPermissions);
    }

    /**
     * Description: 给apiId指定的apiInfo绑定角色
     * date: 2020/1/1 14:02
     * @author lhstack
     * @version
     * @since 1.8
     */
    @Override
    @RollbackTransactional
    public void updateRoles(Long[] roleIds, Long apiId) {
        remoteApiInfoToRoleRepository.deleteByApiInfoId(apiId);
        List<RemoteApiInfoToRole> toRoleList = Arrays.asList(roleIds).stream().map(item -> {
            RemoteApiInfoToRole apiInfoToRole = new RemoteApiInfoToRole();
            apiInfoToRole.setRoleId(item)
                    .setApiInfoId(apiId);
            return apiInfoToRole;
        }).collect(Collectors.toList());
        remoteApiInfoToRoleRepository.saveAll(toRoleList);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    public RemoteApiInfo update(Long id, RemoteApiInfo entity) throws Exception {
        entity.setId(id);
        return remoteApiInfoRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    public void delete(RemoteApiInfo entity) throws Exception {
        remoteApiInfoRepository.delete(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    public void deleteById(Long id) throws Exception {
        remoteApiInfoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('findById',@p0)")
    public RemoteApiInfo findById(Long id) throws Exception {
        return remoteApiInfoRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    @Override
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('findAll')")
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<RemoteApiInfo> findAll() throws Exception {
        return remoteApiInfoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('findAllByExample',#p0,#p1,#p2)")
    public Page<RemoteApiInfo> findAllByExample(RemoteApiInfo remoteApiInfo, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }

        if(remoteApiInfo == null){
            remoteApiInfo = new RemoteApiInfo();
        }
        ExampleMatcher matching = ExampleMatcher.matching();
        if (StringUtils.isNotBlank(remoteApiInfo.getRequestMethod())) {
            matching = matching.withMatcher("requestMethod", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matching = matching.withIgnorePaths("requestMethod");
        }
        if (StringUtils.isNotBlank(remoteApiInfo.getPattenUrl())) {
            matching = matching.withMatcher("pattenUrl", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matching = matching.withIgnorePaths("pattenUrl");
        }
        if (StringUtils.isNotBlank(remoteApiInfo.getDescription())) {
            matching = matching.withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matching = matching.withIgnorePaths("description");
        }
        if (remoteApiInfo.getAuthorityType() == null || remoteApiInfo.getAuthorityType() == -1) {
            matching = matching.withIgnorePaths("authorityType");
        }

        if (StringUtils.isNotBlank(remoteApiInfo.getNamespace())) {
            matching = matching.withMatcher("namespace", ExampleMatcher.GenericPropertyMatchers.contains());
        } else {
            matching = matching.withIgnorePaths("namespace");
        }
        Example<RemoteApiInfo> example = Example.of(remoteApiInfo, matching);

        return remoteApiInfoRepository.findAll(example, PageRequest.of(page - 1, size));
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('findAllByNamespace',#p0,#p1,#p2)")
    public Page<RemoteApiInfo> findAllByNamespace(String namespace, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        return remoteApiInfoRepository.findAllByNamespace(namespace, PageRequest.of(page - 1, size));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('findAll',#p0,#p1)")
    public Page<RemoteApiInfo> findAll(Integer page, Integer size) throws Exception {
        if (page < 1) {
            page = 1;
        }
        return remoteApiInfoRepository.findAll(PageRequest.of(page - 1, size));
    }

    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    public void saveClientApiInfo(String c) throws Exception {
        try{
            RemoteApiInfo remoteApiInfo = JSONObject.parseObject(decodeContent(c), RemoteApiInfo.class);
            remoteApiInfoRepository.save(remoteApiInfo);
        }catch (Exception e){

        }
    }

    /**
     * 解密字符串，返回原始json数据
     * @param content
     * @return
     * @throws Exception
     */
    private String decodeContent(String content) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(singingKey.getBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        byte[] bytes = Base64.decodeBase64(content);
        return new String(cipher.doFinal(bytes));
    }

    /**
     *
     * @param uniqueMethodKey
     * @param namespace
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('findByHandlerMethodAndNamespace',#p0,#p1)")
    public RemoteApiInfo findByHandlerMethodAndNamespace(String uniqueMethodKey,String namespace){
        return remoteApiInfoRepository.findByHandlerMethodAndNamespace(uniqueMethodKey,namespace);
    }

    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('checkAuthority',#p0)")
    public void checkAuthority(String c) throws Exception {
        String s = decodeContent(c);
        Map<String, String> map = JSONObject.parseObject(s, new TypeReference<Map<String, String>>() {
        });
        RemoteApiInfo remoteApiInfo = findByHandlerMethodAndNamespace(map.get("uniqueMethodKey"),map.get("namespace"));
        if(ObjectUtils.isEmpty(remoteApiInfo)){
            throw new RuntimeException("NullPointerException");
        }
        User user = userService.findByUsername(map.get("username"));
        switch (remoteApiInfo.getAuthorityType()){
            case PermissionsQueryConst
                    .HAS_ROLE :{
                if(!validRole(remoteApiInfo.getId(),user)){
                    throw new RuntimeException("角色校验失败");
                }
            };break;
            case PermissionsQueryConst.HAS_PERMISSION:{
                if(!validPermission(remoteApiInfo.getId(),user)){
                    throw new RuntimeException("权限校验失败");
                }
            };break;
            case PermissionsQueryConst.AUTHORITY_ANY:{
                if(!validAnyAuthority(remoteApiInfo.getId(),user)){
                    throw new RuntimeException("授权校验失败");
                }
            };break;
            case PermissionsQueryConst.AUTHORITY_ALL:{
                if(!validAllAuthority(remoteApiInfo.getId(),user)){
                    throw new RuntimeException("授权校验失败");
                }
            };break;
        }
    }

    private boolean validAllAuthority(Long id,User user) {
        List<Role> userRoles = getUserRoles(user.getId());
        if(userRoles.stream().anyMatch(item -> item.getRoleName().equals("ADMIN"))){
            return true;
        }
        List<Permission> userPermissions = getUserPermission(userRoles);
        List<Role> authorityRoles = getRoles(id);
        List<Permission> authorityPermission = getPermission(id);
        return equalsAllAuthority(authorityRoles,authorityPermission,userRoles,userPermissions);
    }

    /**
     * 匹配所有满足条件的
     * @param authorityRoles
     * @param authorityPermission
     * @param userRoles
     * @param userPermissions
     * @return
     */
    private boolean equalsAllAuthority(List<Role> authorityRoles, List<Permission> authorityPermission, List<Role> userRoles, List<Permission> userPermissions) {
        List<String> authorities = aggeragationRoleAndPermission(authorityRoles,authorityPermission);
        List<String> userAuthorities = aggeragationRoleAndPermission(userRoles, userPermissions);
        return authorities.size() == 0 || authorities.stream().allMatch(item -> userAuthorities.contains(item));
    }

    /**
     * 聚合role和permission成list<String></>
     * @param roles
     * @param permissions
     * @return
     */
    private List<String> aggeragationRoleAndPermission(List<Role> roles, List<Permission> permissions) {
        List<String> authorityRolesStr = roles.stream().map(item -> "ROLE_" + item.getRoleName()).collect(Collectors.toList());
        List<String> authorityPermissionStr = permissions.stream().map(item -> "PERMISSION_" + item.getPermissionName()).collect(Collectors.toList());
        authorityRolesStr.addAll(authorityPermissionStr);
        return authorityRolesStr;
    }

    private List<Permission> getUserPermission(List<Role> roles) {
        List<Permission> collect = roles.stream().map(item -> permissionService.findByRid(item.getId())).flatMap(List::stream).collect(Collectors.toList());
        return collect;
    }


    private List<Role> getUserRoles(Long id) {
        return roleService.findByUid(id);
    }

    /**
     * 根据apiInfoId获取关联的权限
     * @param apiInfoId
     * @return
     */
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('getPermission',#p0)")
    public List<Permission> getPermission(Long apiInfoId) {
        List<RemoteApiInfoToPermission> list = remoteApiInfoToPermissionRepository.findByApiInfoId(apiInfoId);
        Set<Long> setIds = list.stream().map(RemoteApiInfoToPermission::getPermissionId).collect(Collectors.toSet());
        Long[] ids = new Long[setIds.size()];
        return permissionService.findAllByIdIn(setIds.toArray(ids));
    }

    /**
     * 根据apiInfoId获取关联的角色
     * @param apiInfoId
     * @return
     */
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('getRoles',#p0)")
    public List<Role> getRoles(Long apiInfoId) {
        List<RemoteApiInfoToRole> list = remoteApiInfoToRoleRepository.findByApiInfoId(apiInfoId);
        Set<Long> setIds = list.stream().map(item -> item.getRoleId()).collect(Collectors.toSet());
        Long[] ids = new Long[setIds.size()];
        return roleService.findAllByIdIn(setIds.toArray(ids));
    }

    private boolean validAnyAuthority(Long id,User user) {
        List<Role> userRoles = getUserRoles(user.getId());
        if(userRoles.stream().anyMatch(item -> item.getRoleName().equals("ADMIN"))){
            return true;
        }
        List<Permission> userPermissions = getUserPermission(userRoles);
        List<Role> authorityRoles = getRoles(id);
        List<Permission> authorityPermission = getPermission(id);
        return equalsAnyAuthority(authorityRoles,authorityPermission,userRoles,userPermissions);
    }

    /**
     * 匹配满足一个条件就为true
     * @param authorityRoles
     * @param authorityPermission
     * @param userRoles
     * @param userPermissions
     * @return
     */
    private boolean equalsAnyAuthority(List<Role> authorityRoles, List<Permission> authorityPermission, List<Role> userRoles, List<Permission> userPermissions) {
        List<String> authorities = aggeragationRoleAndPermission(authorityRoles,authorityPermission);
        List<String> userAuthorities = aggeragationRoleAndPermission(userRoles, userPermissions);
        return authorities.size() == 0 || authorities.stream().anyMatch(item -> userAuthorities.contains(item));
    }

    /**
     * hasPermission
     * @param id
     * @param user
     * @return
     */
    private boolean validPermission(Long id,User user) {
        List<Role> userRoles = getUserRoles(user.getId());
        if(userRoles.stream().anyMatch(item -> item.getRoleName().equals("ADMIN"))){
            return true;
        }
        Set<String> uPermissions = getUserPermission(userRoles).stream().map(Permission::getPermissionName).collect(Collectors.toSet());
        List<Permission> authorityPermission = getPermission(id);
        return authorityPermission.size() == 0 || authorityPermission.stream().anyMatch(item -> uPermissions.contains(item.getPermissionName()));
    }

    /**
     * hanRole
     * @param id
     * @param user
     * @return
     */
    private boolean validRole(Long id,User user) {
        List<String> uRoles = getUserRoles(user.getId()).stream().map(item -> item.getRoleName()).collect(Collectors.toList());
        if(uRoles.contains("ADMIN")){
            return true;
        }
        List<Role> roles = getRoles(id);
        return roles.size() == 0 || roles.stream().anyMatch(item -> uRoles.contains(item.getRoleName()));
    }

    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    @Override
    public Integer updateAuthorityType(Integer type, Long id, String description) {
        return remoteApiInfoRepository.updateAuthorityType(type,id,description);
    }

    @Override
    @Cacheable(cacheNames = "remoteApiInfo",key = "@genertorKey.getKey('findAllNamespaces')")
    public List<String> findAllNamespaces() {
        return remoteApiInfoRepository.findAllNamespaces();
    }

    @Override
    @CacheEvict(cacheNames = "remoteApiInfo",allEntries = true)
    @RollbackTransactional
    public void deleteByNamespace(String namespace) {
        remoteApiInfoRepository.deleteByNamespace(namespace);
    }
}
