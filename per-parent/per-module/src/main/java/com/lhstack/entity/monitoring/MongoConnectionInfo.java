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
@Document("monitoring_mongo_connection_info")
@CompoundIndexes(
        @CompoundIndex(name = "uniqueKey",unique = true,def = "{username:1,mongo_url:1}")
)
public class MongoConnectionInfo implements Serializable {

    private static final long serialVersionUID = 8214230260864290028L;
    @Id
    private String id;

    private String username;

    @Field("mongo_url")
    private String mongoUrl;

    @Field("create_time")
    private Date createTime;
    /**
     * 注释信息
     */
    private String description;
}
