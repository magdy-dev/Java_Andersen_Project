package com.andersen.logger.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for logging components in the application.
 * This class enables component scanning for the specified base package,
 * allowing Spring to automatically discover and configure logging-related components.
 */
@Configuration
@ComponentScan(basePackages = "com.andersen")
public class LoggerConfig {

}