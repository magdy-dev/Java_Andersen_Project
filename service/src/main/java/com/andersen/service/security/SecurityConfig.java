package com.andersen.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static javax.management.Query.and;

/**
 * SecurityConfig is a configuration class that sets up Spring Security for the application.
 * It defines the security filter chain, authentication manager, and method security for handling
 * security filters and authentication throughout the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Defines a SecurityFilterChain that configures the security settings for HTTP requests.
     * It specifies which endpoints are publicly accessible and which require authentication.
     *
     * @param http The HttpSecurity object used to configure security interceptors.
     * @return The configured SecurityFilterChain.
     * @throws Exception If there is an error during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());



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