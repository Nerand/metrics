package org.example.performancedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = "org.example.performancedemo.repository.jpa")
@EnableMongoRepositories(basePackages = "org.example.performancedemo.repository.mongo")
public class PerformanceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceDemoApplication.class, args);
    }
}