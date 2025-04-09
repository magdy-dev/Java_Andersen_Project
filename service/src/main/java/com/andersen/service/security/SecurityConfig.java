package com.andersen.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig is a configuration class that sets up Spring Security for the application.
 * It defines the security filter chain and authentication manager for handling security filters and authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines a SecurityFilterChain that configures the security settings for HTTP requests.
     *
     * @param http The HttpSecurity object used to configure security interceptors.
     * @return The configured SecurityFilterChain.
     * @throws Exception If there is an error during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disables CSRF protection for API endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll() // Allow unauthenticated access to login and register endpoints
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin().disable() // Disables form login as we will use JSON-based authentication
                .httpBasic().disable() // Disables HTTP Basic Authentication
                .logout().logoutUrl("/api/auth/logout"); // Defines the logout URL

        return http.build();
    }

    /**
     * Provides an AuthenticationManager bean for handling authentication.
     *
     * @param authenticationConfiguration The AuthenticationConfiguration object used to retrieve the authentication manager.
     * @return The configured AuthenticationManager.
     * @throws Exception If there is an error during authentication manager creation.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}