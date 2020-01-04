package com.lhstack.config.proxy.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * author: hp
 * date: 2020/1/3
 **/
@ConfigurationProperties(prefix = "persys.monitoring")
@Data
@Accessors(chain = true)
public class MonitoringSqlProperties {

    private String monitoringSqlUri = "https://admin.lhstack.xyz/remote/sql/push";

    private String singingKey = "asdf153zdg5212sg";
}
