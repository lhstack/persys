package com.lhstack.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ClassName: ClientApiInfo
 * Description:
 * date: 2019/12/24 20:41
 *
 * @author lhstack
 * @since
 */
@Data
@Accessors(chain = true)
public class ClientApiInfo {

    private String namespace;

    private String requestMethod;


    private String pattenUrl;

    private String handlerMethod;
    /**
     * 授权类型，
     *  0 --》 HAS_ROLE 验证角色，满足一个条件为true
     *  1 —》 HAS_PERMISSION 验证权限，满足一个条件为true
     *  2 —》 AUTHORITY_ANY 验证权限和角色，满足一个条件为true
     *  3 —》 AUTHORITY_ALL 验证权限和角色，满足所有条件为true
     */
    private Integer authorityType;

    private String description;
}
