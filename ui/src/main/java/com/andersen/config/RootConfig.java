package com.andersen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Root configuration to load both AppConfig and UIConfig.
 */
@Configuration
@Import({AppConfig.class, UIConfig.class})
public class RootConfig {
}
