package com.lhstack.config.security;

public interface SecurityConst {

    static final String ROLE = "ROLE_";
    static final String PERMISSION = "PERMISSION_";

    static final String ACCESS_REDIS_TOKEN_SECURITY = "ACCESS_TOKEN_SECURITY_REDIS_";
    //tokenkey
    static final String ACCESS_TOKEN = "AccessToken";
    //授权信息key
    static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    //保存用户登录状态key
    static final String AUTHENTICATION_STATE_KEY = "AUTHENTICATION_STATE_KEY_";
}
