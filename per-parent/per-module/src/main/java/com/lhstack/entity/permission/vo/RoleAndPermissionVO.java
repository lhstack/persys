package com.lhstack.entity.permission.vo;

import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoleAndPermissionVO implements Serializable {

    private static final long serialVersionUID = -2647840570436347988L;
    private Role role;

    private List<Permission> permissionList;

}
