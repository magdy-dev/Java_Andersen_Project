package com.andersen.ui.config;

import com.andersen.service.config.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for the user interface layer of the application.
 * This class scans for components in the 'com.andersen.services' package
 * and imports the ServiceConfig class to integrate service layer configurations.
 */
@Configuration
@ComponentScan(basePackages = "com.andersen.services")
@Import(ServiceConfig.class)
public class UIConfig {
}