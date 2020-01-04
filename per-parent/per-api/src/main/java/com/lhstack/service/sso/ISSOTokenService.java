package com.lhstack.service.sso;

import com.lhstack.entity.sso.SSOToken;
import com.lhstack.service.IBaseService;
import org.springframework.data.domain.Page;

public interface ISSOTokenService extends IBaseService<String, SSOToken> {
    Page<SSOToken> findByUserId(Long userId, Integer page, Integer size);

    SSOToken findBySSoToken(String ssoToken);
}
