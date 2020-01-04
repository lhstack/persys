package com.lhstack.upload.client.autoconfig;

import com.lhstack.upload.client.OSSClient;
import com.lhstack.upload.client.config.GlobalConfig;
import com.lhstack.upload.client.properties.*;
import com.lhstack.upload.factory.OSSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * author: hp
 * date: 2020/1/3
 **/
@ConditionalOnProperty(name = "persys.upload.enable",matchIfMissing = true)
@EnableConfigurationProperties({OSSClientProperties.class})
public class UploadAutoConfiguration {

    @Autowired
    private OSSClientProperties ossClientProperties;

    @ConditionalOnMissingBean
    @Bean
    public OSSClient ossClient(){
        GlobalConfig.setBaseUrl(ossClientProperties.getBaseUrl());
        GlobalConfig.setTOKEN(ossClientProperties.getToken());
        return ossClientProperties.getOssClientType() == OSSClientType.OKHTTP ? OSSClientFactory.createDefaultOSSClient() : OSSClientFactory.getSelectTypeOSSClient("httpclient");
    }
}
