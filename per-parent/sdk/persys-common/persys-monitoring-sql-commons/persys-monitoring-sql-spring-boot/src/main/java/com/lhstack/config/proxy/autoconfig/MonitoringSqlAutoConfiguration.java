package com.lhstack.config.proxy.autoconfig;

import com.lhstack.config.proxy.ProxyDataSource;
import com.lhstack.config.proxy.properties.GlobalConfigProperties;
import com.lhstack.config.proxy.properties.MonitoringSqlProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * author: hp
 * date: 2020/1/3
 **/
@ConditionalOnProperty(name = "persys.monitoring.enable",matchIfMissing = true)
@EnableConfigurationProperties({MonitoringSqlProperties.class})
public class MonitoringSqlAutoConfiguration implements BeanPostProcessor, Ordered {

    @Autowired
    private MonitoringSqlProperties monitoringSqlProperties;

    @Value("${spring.application.name:default}")
    private String applicationName;

    @PostConstruct
    public void init(){
        GlobalConfigProperties.setSingingKey(monitoringSqlProperties.getSingingKey());
        GlobalConfigProperties.setMonitorySqlUri(monitoringSqlProperties.getMonitoringSqlUri());
        GlobalConfigProperties.setApplicationName(applicationName);
        GlobalConfigProperties.setENABLE(true);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof DataSource){
            return new ProxyDataSource((DataSource) bean);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
