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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The AuthController class is a Spring REST controller that handles authentication-related requests,
 * including login, registration, logout, and admin-specific actions.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs an AuthController with the specified AuthService.
     *
     * @param authService the AuthService to be used for user authentication
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint that returns content accessible only to users with an ADMIN role.
     *
     * @return ResponseEntity containing the admin content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Admin content");
    }

    /**
     * Login endpoint that authenticates a user based on the provided username and password.
     * On successful authentication, a session is created, and user data is stored in the session.
     *
     * @param username the username of the user trying to log in
     * @param password the password of the user trying to log in
     * @param request  the HttpServletRequest used to create the session
     * @return ResponseEntity containing a UserDto if authentication is successful,
     * or an unauthorized response if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestParam String username,
                                         @RequestParam String password,
                                         HttpServletRequest request) {
        try {
            User user = authService.login(username, password);
            UserDto dto = UserMapper.toDto(user);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", dto);
            return ResponseEntity.ok(dto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Registration endpoint that creates a new user with the provided details.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param email    the email of the new user
     * @param fullName the full name of the new user
     * @return ResponseEntity containing a UserDto if registration is successful,
     * or a bad request response if registration fails
     * @throws AuthenticationException if there is an issue with registration
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestParam String username,
                                            @RequestParam String password,
                                            @RequestParam String email,
                                            @RequestParam String fullName)
            {
        try {
            User user = authService.registerCustomer(username, password, email, fullName);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
        } catch (AuthenticationException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataAccessException | RegistrationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Logout endpoint that invalidates the current user session and logs the user out.
     *
     * @param request the HttpServletRequest used to retrieve the session
     * @return ResponseEntity indicating the logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String token = session.getId();
            authService.logout(token);
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}