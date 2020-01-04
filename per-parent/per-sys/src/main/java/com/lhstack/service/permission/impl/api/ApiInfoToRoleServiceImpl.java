package com.lhstack.service.permission.impl.api;

import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.entity.permission.api.ApiInfoToRole;
import com.lhstack.repository.permission.api.ApiInfoToRoleRepository;
import com.lhstack.service.permission.api.IApiInfoToRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiInfoToRoleServiceImpl implements IApiInfoToRoleService {

    @Autowired
    private ApiInfoToRoleRepository roleRepository;

    @Override
    @RollbackTransactional
    public ApiInfoToRole save(ApiInfoToRole entity) throws Exception {
        return roleRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    public ApiInfoToRole update(Long id, ApiInfoToRole entity) throws Exception {
        entity.setId(id);
        return roleRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    public void delete(ApiInfoToRole entity) throws Exception {
        roleRepository.delete(entity);
    }

    @Override
    @RollbackTransactional
    public void deleteById(Long id) throws Exception {
        roleRepository.deleteById(id);
    }

    @Override
    public ApiInfoToRole findById(Long id) throws Exception {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<ApiInfoToRole> findAll() throws Exception {
        return roleRepository.findAll();
    }

    @Override
    public Page<ApiInfoToRole> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return roleRepository.findAll(PageRequest.of(page - 1,size));
    }

    @Override
    public List<ApiInfoToRole> findByApiInfoId(Long id){
        return roleRepository.findByApiInfoId(id);
    }
}
