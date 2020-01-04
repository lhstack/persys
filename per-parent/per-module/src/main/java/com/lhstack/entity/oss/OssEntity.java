package com.lhstack.entity.oss;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Set;

@Document("p_oss_entity")
@Data
@Accessors(chain = true)
@CompoundIndexes(
        @CompoundIndex(name = "uniqueKey",unique = true,def = "{access_key_id:1,access_key_secret:1,end_point:1,bucket_name:1,schema:1,type:1}")
)
public class OssEntity implements Serializable {

    private static final long serialVersionUID = -6977562236523926228L;
    @Id
    private String id;

    @Field("access_key_id")
    private String accessKeyId;

    @Field("access_key_secret")
    private String accessKeySecret;

    @Field("end_point")
    private String endPoint;

    @Field("bucket_name")
    private String bucketName;

    /**
     * 标识是https还是http
     */
    private String schema;


    /**
     * 目前只支持阿里云和七牛云类型
     * ALI_YUN,QINIU_YUN
     */
    private String type;
    /**
     * 上传文件携带的token
     */
    private String token;

    @Field("ip_white_list")
    private Set<String> ipWhiteList;

}
