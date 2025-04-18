package com.andersen.ui.controller;

import com.andersen.service.dto.userrole.AuthResponseDto;
import com.andersen.service.dto.userrole.LoginRequest;
import com.andersen.service.dto.userrole.RegisterRequest;
import com.andersen.service.dto.userrole.UserDto;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.auth.AuthService;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling authentication-related requests.
 *
 * This controller manages user authentication, including login and registration.
 * It provides endpoints for both admin and regular users.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    /**
     * Constructs an AuthController with the specified AuthService.
     *
     * @param authService the AuthService to handle authentication logic
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Admin-only endpoint that returns content accessible only to users with the ADMIN role.
     *
     * @return ResponseEntity containing admin content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public ResponseEntity<String> adminEndpoint() {
        logger.info("Admin-only endpoint accessed");
        return ResponseEntity.ok("Admin content");
    }

    /**
     * Handles user login requests.
     *
     * @param loginRequest the login request containing username and password
     * @return ResponseEntity containing AuthResponseDto on success, or an error response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for username: {}", loginRequest.getUsername());
        try {
            AuthResponseDto response = authService.login(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );
            logger.info("Login successful for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.warn("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (DataAccessException e) {
            logger.error("Database error during login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Handles user registration requests.
     *
     * @param registerRequest the registration request containing user details
     * @return ResponseEntity containing UserDto on success, or an error response
     * @throws AuthenticationException if registration fails due to authentication issues
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest registerRequest) throws AuthenticationException {
        logger.info("Registering new user: {}", registerRequest.getUsername());
        try {
            User user = authService.registerCustomer(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail(),
                    registerRequest.getFullName()
            );

            // Ensure the user is active (if that's expected behavior)
            user.setActive(true); // optionally move this inside the service

            // Map the User to UserDto
            UserDto userDto = new UserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.isActive(),
                    user.getRole()
            );

            logger.info("User registered successfully: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (RegistrationException e) {
            logger.warn("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            logger.error("Database error during registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}