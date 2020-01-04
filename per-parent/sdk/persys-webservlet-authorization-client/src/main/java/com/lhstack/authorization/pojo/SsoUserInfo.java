package com.lhstack.authorization.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SsoUserInfo {

    private String username;

    private String nickName;

    private String icon;

    private String email;

    private Date createTime;
}
