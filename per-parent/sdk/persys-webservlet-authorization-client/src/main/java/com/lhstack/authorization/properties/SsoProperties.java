package com.lhstack.authorization.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * ClassName: SsoProperities
 * Description:
 * date: 2019/12/24 21:35
 *
 * @author lhstack
 * @since
 */
@ConfigurationProperties(prefix = "persys.sso.info")
@Data
@Accessors(chain = true)
public class SsoProperties {

    /**
     * 单点登陆的url
     */
    private String loginUrl;

    /**
     * 授权类型，UUID ,JWT,目前仅支持jwt类型
     */
    private String authorityType = "JWT";


    /**
     * authorityType == JWT 时，此配置就是签名
     */
    private String singingKey;
    /**
     * 获取用户信息的url
     */
    private String userInfoUrl;

    /**
     * persys服务端配置的sso单点登陆的token
     */
    private String ssoToken;

    /**
     * 服务端配置的重定向地址，多个中选择这个服务器的地址
     */
    private String redirectUrl;


    /**
     * 前缀,拦截前缀后面的所有地址
     */
    private String prefix = "/";


    /**
     * 登陆成功重定向url
     */
    private String successUrl = "/";

    /**
     * 忽略地址，不拦截的url
     */
    private Set<String> ingoreUrl;
}
