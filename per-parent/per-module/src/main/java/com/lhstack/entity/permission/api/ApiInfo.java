package com.lhstack.entity.permission.api;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "tb_api_info",indexes = {
        @Index(name = "uniqueKey",columnList = "request_method,patten_url,handler_method",unique = true)
})
public class ApiInfo implements Serializable {

    private static final long serialVersionUID = -7900167016808439708L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_method")
    private String requestMethod;


    @Column(name = "patten_url")
    private String pattenUrl;

    @Column(name = "handler_method")
    private String handlerMethod;


    /**
     * 授权类型，
     *  0 --》 HAS_ROLE 验证角色，满足一个条件为true
     *  1 —》 HAS_PERMISSION 验证权限，满足一个条件为true
     *  2 —》 AUTHORITY_ANY 验证权限和角色，满足一个条件为true
     *  3 —》 AUTHORITY_ALL 验证权限和角色，满足所有条件为true
     */
    @Column(name = "authority_type",length = 1)
    private Integer authorityType;

    private String description;
}
