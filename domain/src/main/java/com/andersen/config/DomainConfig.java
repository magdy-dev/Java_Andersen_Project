package com.andersen.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * Configuration class for Spring components in the application.
 * This class enables component scanning for the specified base package.
 */
@Configuration
@ComponentScan(basePackages = "com.andersen")
public class DomainConfig {

}