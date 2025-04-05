package com.andersen.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the user interface layer of the application.
 * This class scans for components in the 'com.andersen.controller' and 'com.andersen.domain.repository_Criteria' packages.
 */
@ComponentScan
@Configuration
@ComponentScan(basePackages = {
        "com.andersen.controller",
        "com.andersen.domain.repository_Criteria.user",
        "com.andersen.domain.repository_Criteria.booking",
        "com.andersen.domain.repository_Criteria.workspace"
})
public class UIConfig {
}
