package com.lhstack.config.proxy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 封装监控数据
 */
@Data
@Accessors(chain = true)
public class SqlMonitory implements Serializable {

    private static final long serialVersionUID = 3403315574071244428L;

    private String id;

    private String namespace;

    private String sql;

    private String parameter;

    private Long maxExecuteTime;

    private Long minExecuteTime;

    private Long count;

    private Long average;

}
