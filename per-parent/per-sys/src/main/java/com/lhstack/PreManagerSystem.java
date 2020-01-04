package com.lhstack;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
* Description: PerSys启动类
* date: 2019/12/24 20:18
* @author lhstack
* @version 0.0.1
* @since 1.8
*/
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.lhstack.mapper")
@EnableCaching
public class PreManagerSystem implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty("proxy.observer.class","com.lhstack.config.monitoring.SqlConsumerObServer");
        SpringApplication.run(PreManagerSystem.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
