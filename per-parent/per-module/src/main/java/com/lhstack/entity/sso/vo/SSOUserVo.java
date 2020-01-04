package com.lhstack.entity.sso.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SSOUserVo implements Serializable {

    private static final long serialVersionUID = 4569015712812692997L;
    private String username;

    private String password;

    private String validCode;
}
