package com.zz.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.statemachine.config.EnableStateMachineFactory;

@SpringBootApplication
@EnableScheduling
public class CbApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbApplication.class, args);
    }
}
