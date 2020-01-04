package com.lhstack.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class PasswordEncoderUtils {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public String genPass(String salt,String password){
        String newPassword = passwordEncoder.encode(salt + password);
        log.debug("salt as {} <--->  password as {} , generate new password as {},",salt,password,newPassword);
        return newPassword;
    }

    public Boolean matches(String salt,String password,String dbPass){
        log.debug("matches password ---> salt as {},password as {},dbPass as {}",salt,password,dbPass);
        return passwordEncoder.matches(salt + password,dbPass);
    }

    public String salt(){
        String salt = passwordEncoder.encode(UUID.randomUUID().toString());
        log.debug("generate salt as {}",salt);
        return salt;
    }
}
