package com.andersen.service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration class for service components in the application.
 * This class enables component scanning for the specified base package
 * and imports the DomainConfig configuration class to include its bean definitions.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.andersen.repository_JPA")
@EntityScan(basePackages = "com.andersen.entity")
public class ServiceConfig {

}