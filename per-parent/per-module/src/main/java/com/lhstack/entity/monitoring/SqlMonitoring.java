package com.lhstack.entity.monitoring;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "m_sql")
@Data
@Accessors(chain = true)
public class SqlMonitoring implements Serializable {
    private static final long serialVersionUID = 3403328574071244428L;

    @Id
    private String id;

    private String sql;

    private String parameter;

    @Field("max_execute_time")
    private Long maxExecuteTime;

    @Field("min_execute_time")
    private Long minExecuteTime;


    private Long count;

    private Long average;
}
