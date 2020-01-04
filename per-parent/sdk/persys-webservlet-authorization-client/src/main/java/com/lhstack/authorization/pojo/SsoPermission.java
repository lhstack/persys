package com.lhstack.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SsoPermission {

    private String permissionName;

    private String logogramName;

    private String icon;
}
