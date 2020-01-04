package com.lhstack.entity.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserExampleDTO implements Serializable {

    private static final long serialVersionUID = 4176999896016357808L;

    private String search;

    private Date startTime;

    private Date endTime;

    private Long userId;

    private Integer page;

    private Integer limit;

    private Boolean isAdmin;

    /**
     * 查询返回集合忽略拥有roleName的角色
     */
    private List<String> ignoreRoleNames;

    /**
     * 查询返回集合忽略拥有PermissionName的权限
     */
    private List<String> ignorePermissionNames;

}
