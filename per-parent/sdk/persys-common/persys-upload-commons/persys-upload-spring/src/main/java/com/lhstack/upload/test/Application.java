package com.lhstack.upload.test;

import com.lhstack.upload.annotation.EnablePersysOss;
import com.lhstack.upload.client.OSSClient;
import com.lhstack.upload.client.properties.OSSClientType;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * author: hp
 * date: 2020/1/3
 **/
@Configuration
@EnablePersysOss(baseUrl = "https://admin.lhstack.xyz",token = "8aff853a58140f2749b01742xz969ed8421",type = OSSClientType.HTTPCLIENT)
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Application.class);
        OSSClient bean = annotationConfigApplicationContext.getBean(OSSClient.class);
        System.out.println(bean);
        annotationConfigApplicationContext.start();
    }
}
