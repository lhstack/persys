package com.lhstack.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SsoAuthorityInfo {

    private SsoUserInfo userInfo;

    private List<SsoRole> roleList;

    private List<SsoPermission> permissionList;

}
