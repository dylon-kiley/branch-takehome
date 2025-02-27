package org.dkiley.takehome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TakehomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakehomeApplication.class, args);
    }
}