package com.andersen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point for the Spring Boot application.
 * This class initializes and configures the Spring application context.
 * It scans for entities and JPA repositories within the specified packages.
 */
@SpringBootApplication
@EntityScan("com.andersen.entity")
@EnableJpaRepositories("com.andersen.repository_JPA")
public class UiApplication {

    /**
     * The main method that serves as the entry point for the application.
     * It starts the Spring Boot application by invoking the run method
     * of the SpringApplication class.
     *
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }
}