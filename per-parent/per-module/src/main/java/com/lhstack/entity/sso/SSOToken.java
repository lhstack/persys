package com.lhstack.entity.sso;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@Document(collection = "tb_sso_token")
@CompoundIndexes(
        @CompoundIndex(name = "uniqueKey",unique = true,def = "{user_id:1,app_name:1}")
)
public class SSOToken implements Serializable {

    private static final long serialVersionUID = 5024944939973480529L;
    @Id
    private String id;

    /**
     * 用户id
     */
    @Field("user_id")
    private Long userId;


    /**
     * 应用名称
     */
    @Field(value = "app_name")
    @Indexed(unique = true)
    private String appName;

    /**
     * 应用注释
     */
    @Field("app_description")
    private String appDescription;


    /**
     * 应用首页
     */
    private String index;

    /**
     * 登录成功回调urls
     */
    private String[] service;

    /**
     * token生成类型 : UUID ,JWT
     */
    private String type;

    /**
     * type == JWT生效  jwtToken 生成时签名 Key
     */
    @Field("jwt_sign_key")
    private String jwtSignKey;


    @Field("create_time")
    private Date createTime;


    /**
     * 是否在token创建成功时生成权限和角色信息
     */
    @Field("is_generator_permission_role")
    private Boolean isGeneratorPermissionAndRole;

    /**
     * 单点登录访问token
     */
    private String token;
}
