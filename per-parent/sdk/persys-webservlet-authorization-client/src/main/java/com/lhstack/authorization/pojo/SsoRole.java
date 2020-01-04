package com.lhstack.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SsoRole {

    private String roleName;

    private String icon;

    private String logogramName;
}
