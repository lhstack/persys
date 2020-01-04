package com.lhstack.entity.monitoring;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Document("monitoring_redis_connection_info")
@CompoundIndexes(
        @CompoundIndex(name = "uniqueKey",unique = true,def = "{username:1,db_host:1,db_port:1,database:1}")
)
@Data
@Accessors(chain = true)
public class RedisConnectionInfo implements Serializable {

    private static final long serialVersionUID = -3713493478534641722L;
    @Id
    private String id;

    private String username;

    @Field("db_host")
    private String dbHost;

    @Field("db_port")
    private Integer dbPort;

    private int database;

    @Field("db_password")
    private String dbPassword;

    @Field("create_time")
    private Date createTime;

    /**
     * 注释信息
     */
    private String description;
}
