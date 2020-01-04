package com.lhstack.upload.client.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * author: hp
 * date: 2020/1/3
 **/
@ConfigurationProperties(prefix = "persys.upload")
@Data
@Accessors(chain = true)
public class OSSClientProperties {
    private String token;

    private String baseUrl;

    private OSSClientType ossClientType = OSSClientType.OKHTTP;
}
