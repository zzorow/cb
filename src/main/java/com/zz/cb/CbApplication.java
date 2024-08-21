package com.zz.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.config.EnableStateMachineFactory;

@SpringBootApplication
public class CbApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbApplication.class, args);
    }

}
