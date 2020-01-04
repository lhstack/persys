package com.lhstack.service.permission.impl.api;

import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.controller.permission.PermissionsQueryConst;
import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.api.ApiInfo;
import com.lhstack.entity.permission.api.ApiInfoToPermission;
import com.lhstack.entity.permission.api.ApiInfoToRole;
import com.lhstack.repository.permission.PermissionRepository;
import com.lhstack.repository.permission.RoleRepository;
import com.lhstack.repository.permission.api.ApiInfoRepository;
import com.lhstack.repository.permission.api.ApiInfoToPermissionRepository;
import com.lhstack.repository.permission.api.ApiInfoToRoleRepository;
import com.lhstack.service.permission.api.IApiInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiInfoServiceImpl implements IApiInfoService {

    @Autowired
    private ApiInfoRepository repository;

    @Autowired
    private ApiInfoToRoleRepository apiInfoToRoleRepository;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApiInfoToPermissionRepository apiInfoToPermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;


    @CacheEvict(cacheNames = "apiInfo",allEntries = true)
    public void save(RequestMappingInfo info, HandlerMethod method){
        String requestMethods = info.getMethodsCondition().getMethods().stream().map(RequestMethod::name).collect(Collectors.joining(","));
        String pattenUrls = info.getPatternsCondition().getPatterns().stream().map(PathPattern::getPatternString).collect(Collectors.joining(","));
        try{
           if(isPresent(method.getMethod())){
               ApiInfo apiInfo = new ApiInfo();
               apiInfo
                       .setHandlerMethod(getUniqueMethod(method))
                       .setPattenUrl(pattenUrls)
                       .setRequestMethod(requestMethods)
                       .setAuthorityType(getApiInfoType(method.getMethod()))
                       .setDescription(getApiDescription(method.getMethod()));
               ApiInfo apiInfoResult = repository.save(apiInfo);
               saveRoleAndPermission(method,apiInfoResult);
           }
        }catch (Exception e){

        }
    }

    /**
     * 判断接口是否需要鉴权
     * @param method
     * @return
     */
    private boolean isPresent(Method method) {
        return AnnotationUtils.findAnnotation(method, DynAuthority.class) != null;
    }

    /**
     * 获取鉴权接口注释
     * @param method
     * @return
     */
    private String getApiDescription(Method method) {
        InitAuthority annotation = AnnotationUtils.findAnnotation(method, InitAuthority.class);
        if(null == annotation){
            return method.getName();
        }
        return annotation.description();
    }

    /**
     * 获取鉴权类型
     * @param method
     * @return
     */
    private int getApiInfoType(Method method) {
        InitAuthority annotation = AnnotationUtils.findAnnotation(method, InitAuthority.class);
        if(null == annotation){
            return PermissionsQueryConst.AUTHORITY_ALL;
        }
        String value = annotation.value();
        if(value.matches("hasRole\\((\\w|,)*\\)")){
            return PermissionsQueryConst.HAS_ROLE;
        }
        if(value.matches("hasPermission\\((\\w|,)*\\)")){
            return PermissionsQueryConst.HAS_PERMISSION;
        }
        if(value.matches("anyAuthority\\((\\w|,)*\\)")){
            return PermissionsQueryConst.AUTHORITY_ANY;
        }
        if(value.matches("allAuthority\\((\\w|,)*\\)")){
            return PermissionsQueryConst.AUTHORITY_ALL;
        }
        return PermissionsQueryConst.AUTHORITY_ALL;
    }

    /**
     * 保存接口鉴权角色和权限
     * @param method
     * @param apiInfoResult
     */
    private void saveRoleAndPermission(HandlerMethod method, ApiInfo apiInfoResult) {
        InitAuthority annotation = AnnotationUtils.findAnnotation(method.getMethod(), InitAuthority.class);
        if(null != annotation){
            String value = annotation.value();
            if(value.matches("hasRole\\((\\w|,)*\\)")){
                String roles = value.substring(8,value.length() - 1);
                saveRole(roles,apiInfoResult.getId());
            }
            if(value.matches("hasPermission\\((\\w|,)*\\)")){
                savePermission(value.substring(14,value.length() - 1),apiInfoResult.getId());
            }
            if(value.matches("anyAuthority\\((\\w|,)*\\)") || value.matches("allAuthority\\((\\w|,)*\\)")){
                String authorities = value.substring(13, value.length() - 1);
                String roles = Arrays.asList(authorities.split(",")).stream().filter(item -> StringUtils.startsWith(item, "ROLE_")).map(item -> StringUtils.substring(item, 5)).collect(Collectors.joining(","));
                saveRole(roles,apiInfoResult.getId());
                String permissions = Arrays.asList(authorities.split(",")).stream().filter(item -> StringUtils.startsWith(item, "PERMISSION_")).map(item -> StringUtils.substring(item, 11)).collect(Collectors.joining(","));
                savePermission(permissions,apiInfoResult.getId());
            }
        }
    }

    /**
     * 关联权限和接口
     * @param value
     * @param id
     */
    private void savePermission(String value,Long id) {
        List<ApiInfoToPermission> apiInfoToPermissions = permissionRepository.findAllByPermissionNameIn(value.split(",")).stream().map(item -> {
            ApiInfoToPermission apiInfoToPermission = new ApiInfoToPermission();
            apiInfoToPermission.setApiInfoId(id)
                    .setPermissionId(item.getId());
            return apiInfoToPermission;
        }).collect(Collectors.toList());
        apiInfoToPermissionRepository.saveAll(apiInfoToPermissions);
    }

    /**
     * 关联角色和接口
     * @param value
     * @param id
     */
    private void saveRole(String value, Long id) {
        try{
            List<ApiInfoToRole> apiInfoToRoleList = roleRepository.findByRoleNameIn(value.split(",")).stream().map(item -> {
                ApiInfoToRole apiInfoToRole = new ApiInfoToRole();
                apiInfoToRole.setApiInfoId(id)
                        .setRoleId(item.getId());
                return apiInfoToRole;
            }).collect(Collectors.toList());
            apiInfoToRoleRepository.saveAll(apiInfoToRoleList);
        }catch (Exception e){

        }
    }

    private String getUniqueMethod(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        String uniqueMethod = handlerMethod.getBeanType().getName() + "." + method.getName();
        String parameter = Arrays.asList(handlerMethod.getMethodParameters()).stream().map(item -> item.getParameter().getName()).collect(Collectors.joining(","));
        uniqueMethod = uniqueMethod + "(" + parameter + ")";
        String returnType = handlerMethod.getReturnType().getParameterType().getName();
        return uniqueMethod + "---------->" + returnType;
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "apiInfo",allEntries = true)
    public ApiInfo save(ApiInfo entity) throws Exception {
        return repository.save(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "apiInfo",allEntries = true)
    public ApiInfo update(Long id, ApiInfo entity) throws Exception {
        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "apiInfo",allEntries = true)
    public void delete(ApiInfo entity) throws Exception {
        repository.delete(entity);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "apiInfo",allEntries = true)
    public void deleteById(Long id) throws Exception {
        repository.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "apiInfo",key = "@genertorKey.getKey('findById',#p0)")
    public ApiInfo findById(Long id) throws Exception {
        return repository.findById(id).orElseThrow(NullPointerException::new);
    }

    @Override
    @Cacheable(cacheNames = "apiInfo",key = "@genertorKey.getKey('findAll')")
    public List<ApiInfo> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "apiInfo",key = "@genertorKey.getKey('findAll',#p0,#p1)")
    public Page<ApiInfo> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return repository.findAll(PageRequest.of(page - 1,size, Sort.by(Sort.Order.asc("id"))));
    }

    @Override
    @Cacheable(cacheNames = "apiInfo",key = "@genertorKey.getKey('findByHandlerMethod',#p0)")
    public ApiInfo findByHandlerMethod(String uniqueMethod) {
        return repository.findByHandlerMethod(uniqueMethod);
    }

    @Override
    @CacheEvict(cacheNames = "apiInfo",allEntries = true)
    public Integer updateAuthorityType(Integer type, Long id,String description) {
        return repository.update(type,id,description);
    }

    @Override
    public List<Permission> listPermission(Long apiInfoId) {
        List<Long> ids = apiInfoToPermissionRepository.findByApiInfoId(apiInfoId).stream().map(ApiInfoToPermission::getPermissionId).collect(Collectors.toList());
        Long[] permissionIds = ids.toArray(new Long[ids.size()]);
        return permissionRepository.findAllByIdIn(permissionIds);
    }

    @Override
    public List<Role> listRoles(Long apiInfoId) {
        List<Long> ids = apiInfoToRoleRepository.findByApiInfoId(apiInfoId).stream().map(ApiInfoToRole::getRoleId).collect(Collectors.toList());
        Long[] roleIds = ids.toArray(new Long[ids.size()]);
        return roleRepository.findAllByIdIn(roleIds);
    }

    @Override
    public void updatePermission(Long[] permissionIds, Long apiId) {
        apiInfoToPermissionRepository.deleteByApiInfoId(apiId);
        List<ApiInfoToPermission> apiInfoToPermissions = Arrays.asList(permissionIds).stream().map(item -> {
            ApiInfoToPermission apiInfoToPermission = new ApiInfoToPermission();
            apiInfoToPermission.setPermissionId(item)
                    .setApiInfoId(apiId);
            return apiInfoToPermission;
        }).collect(Collectors.toList());
        apiInfoToPermissionRepository.saveAll(apiInfoToPermissions);
    }

    @Override
    public void updateRoles(Long[] roleIds, Long apiId) {
        apiInfoToRoleRepository.deleteByApiInfoId(apiId);
        List<ApiInfoToRole> toRoleList = Arrays.asList(roleIds).stream().map(item -> {
            ApiInfoToRole apiInfoToRole = new ApiInfoToRole();
            apiInfoToRole.setRoleId(item)
                    .setApiInfoId(apiId);
            return apiInfoToRole;
        }).collect(Collectors.toList());
        apiInfoToRoleRepository.saveAll(toRoleList);
    }

    @Override
    @Cacheable(cacheNames = "apiInfo",key = "@genertorKey.getKey('findByExample',#p0,#p1,#p2)")
    public Page<ApiInfo> findByExample(Integer page, Integer size, ApiInfo apiInfo) {
        ExampleMatcher matching = ExampleMatcher.matching();
        if(StringUtils.isNotBlank(apiInfo.getRequestMethod())){
            matching = matching.withMatcher("requestMethod", ExampleMatcher.GenericPropertyMatchers.contains());
        }else{
            matching = matching.withIgnorePaths("requestMethod");
        }
        if(StringUtils.isNotBlank(apiInfo.getPattenUrl())){
            matching = matching.withMatcher("pattenUrl",ExampleMatcher.GenericPropertyMatchers.contains());
        }else{
            matching = matching.withIgnorePaths("pattenUrl");
        }
        if(StringUtils.isNotBlank(apiInfo.getDescription())){
            matching = matching.withMatcher("description",ExampleMatcher.GenericPropertyMatchers.contains());
        }else{
            matching = matching.withIgnorePaths("description");
        }
        if(apiInfo.getAuthorityType() == null || apiInfo.getAuthorityType() == -1){
            matching = matching.withIgnorePaths("authorityType");
        }
        Example<ApiInfo> example = Example.of(apiInfo,matching);
        if(page < 1){
            page = 1;
        }
        return repository.findAll(example,PageRequest.of(page - 1,size));
    }
}
