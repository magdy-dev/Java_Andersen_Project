package com.andersen.service.config;

import com.andersen.domain.config.DomainConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
/**
 * Configuration class for service components in the application.
 * This class enables component scanning for the specified base package
 * and imports the DomainConfig configuration class to include its bean definitions.
 */
@Configuration
@ComponentScan(basePackages = "com.andersen.domain")
@Import(DomainConfig.class)
public class ServiceConfig {

}