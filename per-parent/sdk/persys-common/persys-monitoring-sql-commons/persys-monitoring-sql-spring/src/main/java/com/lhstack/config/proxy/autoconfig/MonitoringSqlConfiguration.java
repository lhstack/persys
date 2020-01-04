package com.lhstack.config.proxy.autoconfig;

import com.lhstack.config.proxy.ProxyDataSource;
import com.lhstack.config.proxy.annotation.EnableMonitoringSql;
import com.lhstack.config.proxy.properties.GlobalConfigProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Map;

/**
 * CLASSNAME: ${CLASS_NAME}
 * author: hp
 * date: 2020/1/3
 **/
public class MonitoringSqlConfiguration implements ImportAware, BeanPostProcessor, Ordered {
    @Override
    public void setImportMetadata(AnnotationMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableMonitoringSql.class.getName());
        if (annotationAttributes != null) {
            GlobalConfigProperties.setApplicationName(annotationAttributes.get("applicationName").toString());
            GlobalConfigProperties.setENABLE((Boolean) annotationAttributes.get("enable"));
            GlobalConfigProperties.setMonitorySqlUri(annotationAttributes.get("monitoringSqlUri").toString());
            GlobalConfigProperties.setSingingKey(annotationAttributes.get("singingKey").toString());
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            if(GlobalConfigProperties.getENABLE()){
                return new ProxyDataSource((DataSource) bean);
            }
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
