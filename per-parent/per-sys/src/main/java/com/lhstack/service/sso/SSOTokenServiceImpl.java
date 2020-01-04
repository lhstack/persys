package com.lhstack.service.sso;

import com.lhstack.aspect.sys.SysLog;
import com.lhstack.entity.sso.SSOToken;
import com.lhstack.repository.sso.SSOTokenRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * SSOToken服务
 */
@Service
public class SSOTokenServiceImpl implements ISSOTokenService {

    @Autowired
    private SSOTokenRepository ssoTokenRepository;

    @Override
    @SysLog
    public SSOToken save(SSOToken entity) throws Exception {
        entity.setCreateTime(new Date())
                .setToken(UUID.randomUUID().toString().replace("-",""));
        return ssoTokenRepository.save(entity);
    }

    @Override
    public SSOToken update(String s, SSOToken entity) throws Exception {
        if(!StringUtils.isNotEmpty(s)){
            throw new NullPointerException("更新id为null");
        }
        entity.setId(s);
        return ssoTokenRepository.save(entity);
    }

    @Override
    public void delete(SSOToken entity) throws Exception {
        ssoTokenRepository.delete(entity);
    }

    @Override
    public void deleteById(String s) throws Exception {
        ssoTokenRepository.deleteById(s);
    }

    @Override
    public SSOToken findById(String s) throws Exception {
        return ssoTokenRepository.findById(s).orElse(null);
    }

    @Override
    public List<SSOToken> findAll() throws Exception {
        return ssoTokenRepository.findAll();
    }

    @Override
    public Page<SSOToken> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return ssoTokenRepository.findAll(PageRequest.of(page - 1,size , Sort.by(Sort.Order.desc("create_time"))));
    }

    @Override
    public Page<SSOToken> findByUserId(Long userId, Integer page, Integer size) {
        if(page < 1){
            page = 1;
        }
        return ssoTokenRepository.findByUserId(userId,PageRequest.of(page - 1,size , Sort.by(Sort.Order.desc("create_time"))));
    }

    @Override
    public SSOToken findBySSoToken(String ssoToken) {
        return ssoTokenRepository.findByToken(ssoToken);
    }
}
