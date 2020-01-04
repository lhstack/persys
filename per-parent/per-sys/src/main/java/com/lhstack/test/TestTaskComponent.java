package com.lhstack.test;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("test")
public class TestTaskComponent implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello World " + LocalDateTime.now());
    }

    public void run1(String a,Integer b,Double c,Boolean d){
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
    }
}
