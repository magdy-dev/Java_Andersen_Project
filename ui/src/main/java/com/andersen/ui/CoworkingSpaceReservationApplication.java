package com.andersen.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan(basePackages = {
        "com.andersen.ui",
        "com.andersen.service",
        "com.andersen.domain"
})
@EnableJpaRepositories(basePackages = "com.andersen.domain")
@EntityScan(basePackages = "com.andersen.domain")
@SpringBootApplication(scanBasePackages = "com.andersen")
public class CoworkingSpaceReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoworkingSpaceReservationApplication.class, args);
    }
}