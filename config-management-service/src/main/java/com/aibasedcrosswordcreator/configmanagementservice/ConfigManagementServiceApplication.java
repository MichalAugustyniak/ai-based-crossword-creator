package com.aibasedcrosswordcreator.configmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ConfigManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigManagementServiceApplication.class, args);
    }

}
