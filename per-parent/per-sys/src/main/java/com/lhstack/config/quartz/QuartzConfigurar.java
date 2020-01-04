package com.lhstack.config.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzConfigurar {


    @Value("${scheduler.scheduler_mysql_url}")
    private String scheduler_mysql_url;

    @Value("${scheduler.scheduler_mysql_password}")
    private String scheduler_mysql_password;

    private String scheduler_mysql_driver="com.mysql.cj.jdbc.Driver";

    @Value("${scheduler.scheduler_mysql_username}")
    private String scheduler_mysql_username;


    @Bean
    public SchedulerFactory schedulerFactory() throws SchedulerException, IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("quartz.properties"));
        properties.put("org.quartz.dataSource.myQuartz.driver",scheduler_mysql_driver);
        properties.put("org.quartz.dataSource.myQuartz.URL",scheduler_mysql_url);
        properties.put("org.quartz.dataSource.myQuartz.user",scheduler_mysql_username);
        properties.put("org.quartz.dataSource.myQuartz.password",scheduler_mysql_password);

        return new StdSchedulerFactory(properties);
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException, IOException {
        Scheduler scheduler = schedulerFactory().getScheduler();
        scheduler.start();
        return scheduler;
    }
}
