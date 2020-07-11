package com.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.devops")
public class DevOpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevOpsApplication.class, args);
    }

}
