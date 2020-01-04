package com.lhstack.upload.client.config;

import com.lhstack.upload.annotation.EnablePersysOss;
import com.lhstack.upload.client.impl.HttpClientOSSClient;
import com.lhstack.upload.client.impl.OkHttpOSSClient;
import com.lhstack.upload.client.properties.OSSClientType;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * author: hp
 * date: 2020/1/3
 **/
public class PerSysOSSConfiguration implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnablePersysOss.class.getName());
        initGlobalConfig(annotationAttributes);
        registryBean(registry,annotationAttributes);
    }

    private void registryBean(BeanDefinitionRegistry registry, Map<String, Object> annotationAttributes) {
        if(annotationAttributes != null){
            OSSClientType ossClientType = (OSSClientType) annotationAttributes.get("type");
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
            AbstractBeanDefinition singleton = beanDefinitionBuilder.setScope("singleton")
                    .getRawBeanDefinition();
            if(ossClientType == OSSClientType.OKHTTP){
                singleton.setBeanClass(OkHttpOSSClient.class);
            }else{
                singleton.setBeanClass(HttpClientOSSClient.class);
            }
            registry.registerBeanDefinition("ossClient",singleton);
        }
    }

    private void initGlobalConfig(Map<String, Object> annotationAttributes) {
        if(annotationAttributes != null){
            String baseUrl = annotationAttributes.get("baseUrl").toString();
            String token = annotationAttributes.get("token").toString();
            GlobalConfig.setTOKEN(token);
            GlobalConfig.setBaseUrl(baseUrl);
        }
    }
}
