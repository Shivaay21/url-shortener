package com.example.urlshortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.urlshortner.repository")
@EntityScan(basePackages = "com.example.urlshortner.entity")
@EnableAsync
@EnableScheduling
public class UrlShortnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortnerApplication.class, args);
    }

}
