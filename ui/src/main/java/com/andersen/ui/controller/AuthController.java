package com.andersen.ui.controller;

import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.mapper.UserMapper;
import com.andersen.service.auth.AuthService;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RESTful controller for handling authentication-related requests.
 * This class provides endpoints for user authentication, including login, registration, and logout.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs an AuthController with the specified AuthService.
     *
     * @param authService the AuthService to handle authentication operations
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user and establishes a session.
     *
     * @param username the username of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @param request  the HttpServletRequest to access the current session
     * @return a ResponseEntity containing the UserDto if authentication is successful,
     * or UNAUTHORIZED status if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestParam String username,
                                         @RequestParam String password,
                                         HttpServletRequest request) {
        try {
            User user = authService.login(username, password);
            UserDto dto = UserMapper.toDto(user);
            request.getSession().setAttribute("user", dto);
            return ResponseEntity.ok(dto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param username the desired username for the new user
     * @param password the desired password for the new user
     * @param email    the email address of the new user
     * @param fullName the full name of the new user
     * @return a ResponseEntity containing the UserDto of the newly registered user,
     * or BAD_REQUEST status if registration fails
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestParam String username,
                                            @RequestParam String password,
                                            @RequestParam String email,
                                            @RequestParam String fullName) {
        try {
            User user = authService.registerCustomer(username, password, email, fullName);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
        } catch (RegistrationException | AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Logs out the current user and invalidates their session.
     *
     * @param request the HttpServletRequest to access the current session
     * @return a ResponseEntity confirming successful logout or an error message
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String token = session.getId();
                authService.logout(token);
                session.invalidate();
            }
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }
}