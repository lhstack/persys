package com.lhstack.entity.log;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@Document(collection = "tb_syslog")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 3510121148491263298L;
    @Id
    private String id;

    private String username;

    private String ip;

    private String roles;

    private String permissions;

    @Field(value = "operation_time")
    private Date operationTime;

    private String method;


    private String parameters;

    private String args;

    /**
     * 操作状态
     */
    private Boolean state;

    /**
     * 异常信息
     */
    @Field("ex_msg")
    private String exMsg;
}
