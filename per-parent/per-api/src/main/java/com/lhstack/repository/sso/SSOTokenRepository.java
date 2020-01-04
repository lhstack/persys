package com.lhstack.repository.sso;

import com.lhstack.entity.sso.SSOToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface SSOTokenRepository extends MongoRepository<SSOToken,String> {
    Page<SSOToken> findByUserId(Long userId, Pageable pageable);

    SSOToken findByToken(String ssoToken);
}
