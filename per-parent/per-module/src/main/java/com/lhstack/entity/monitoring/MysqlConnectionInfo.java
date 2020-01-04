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

@Data
@Accessors(chain = true)
@Document(collection = "monitoring_mysql_connection_info")
@CompoundIndexes(
        @CompoundIndex(name = "uniqueKey",unique = true,def = "{username:1,db_username:1,db_url:1}")
)
public class MysqlConnectionInfo implements Serializable {

    private static final long serialVersionUID = 2562858480605774911L;
    @Id
    private String id;

    private String username;

    @Field("db_url")
    private String dbUrl;

    @Field("db_username")
    private String dbUsername;

    @Field("db_password")
    private String dbPassword;

    @Field("create_time")
    private Date createTime;

    /**
     * 注释信息
     */
    private String description;
}
