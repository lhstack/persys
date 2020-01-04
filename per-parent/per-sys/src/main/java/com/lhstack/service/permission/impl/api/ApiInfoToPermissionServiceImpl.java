package com.lhstack.service.permission.impl.api;

import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.entity.permission.api.ApiInfoToPermission;
import com.lhstack.repository.permission.api.ApiInfoToPermissionRepository;
import com.lhstack.service.permission.api.IApiInfoToPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiInfoToPermissionServiceImpl implements IApiInfoToPermissionService {

    @Autowired
    private ApiInfoToPermissionRepository apiInfoToPermissionRepository;

    @Override
    @RollbackTransactional
    public ApiInfoToPermission save(ApiInfoToPermission entity) throws Exception {
        return apiInfoToPermissionRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    public ApiInfoToPermission update(Long id, ApiInfoToPermission entity) throws Exception {
        entity.setId(id);
        return apiInfoToPermissionRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    public void delete(ApiInfoToPermission entity) throws Exception {
        apiInfoToPermissionRepository.delete(entity);
    }

    @Override
    @RollbackTransactional
    public void deleteById(Long id) throws Exception {
        apiInfoToPermissionRepository.deleteById(id);
    }

    @Override
    public ApiInfoToPermission findById(Long id) throws Exception {
        return apiInfoToPermissionRepository.findById(id).orElse(null);
    }

    @Override
    public List<ApiInfoToPermission> findAll() throws Exception {
        return apiInfoToPermissionRepository.findAll();
    }

    @Override
    public Page<ApiInfoToPermission> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return apiInfoToPermissionRepository.findAll(PageRequest.of(page - 1,size));
    }

    @Override
    public List<ApiInfoToPermission> findByApiInfoId(Long id) {
        return apiInfoToPermissionRepository.findByApiInfoId(id);
    }

    @Override
    public void deleteByApiId(Long apiId) {
        apiInfoToPermissionRepository.deleteByApiInfoId(apiId);
    }
}
