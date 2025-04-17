package com.andersen.ui.controller;

import com.andersen.domain.dto.userrole.AuthResponseDto;
import com.andersen.domain.dto.userrole.LoginRequest;
import com.andersen.domain.dto.userrole.RegisterRequest;
import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.auth.AuthService;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling authentication-related requests.
 * <p>
 * This controller provides endpoints for user login and registration,
 * along with an admin-only endpoint that demonstrates role-based access control.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs an AuthController with the specified AuthService.
     *
     * @param authService the service for handling authentication logic
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint accessible only to users with the ADMIN role.
     *
     * @return a ResponseEntity containing admin content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Admin content");
    }

    /**
     * Endpoint for user login.
     *
     * @param loginRequest the login request containing username and password
     * @return a ResponseEntity containing the authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponseDto response = authService.login(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint for user registration.
     *
     * @param registerRequest the registration request containing user details
     * @return a ResponseEntity containing the newly created user data
     * @throws AuthenticationException if registration fails for any reason
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest registerRequest) throws AuthenticationException {
        try {
            User user = authService.registerCustomer(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail(),
                    registerRequest.getFullName()
            );
            // Properly map User to UserDto
            UserDto userDto = new UserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.isActive(),
                    user.getRole()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}