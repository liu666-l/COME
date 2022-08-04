package com.boot.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients(basePackages = {"com.boot"})
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.boot.admin"})
@EnableAutoConfiguration(exclude ={SecurityAutoConfiguration.class})
public class AdminApplictaion {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplictaion.class,args);
    }
}
