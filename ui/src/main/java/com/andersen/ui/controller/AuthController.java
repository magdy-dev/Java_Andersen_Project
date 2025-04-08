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
     * @param authService the AuthService to be used by this controller
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param request  the HttpServletRequest used to manage session attributes
     * @return a UserDto representing the logged-in user
     * @throws AuthenticationException if authentication fails
     * @throws DataAccessException     if there is an error accessing the data source
     */
    @PostMapping("/login")
    public UserDto login(@RequestParam String username,
                         @RequestParam String password,
                         HttpServletRequest request) throws AuthenticationException, DataAccessException {
        User user = authService.login(username, password);
        request.getSession().setAttribute("user", UserMapper.toDto(user));
        return UserMapper.toDto(user);
    }

    /**
     * Registers a new user with the specified details.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param email    the email address of the new user
     * @param fullName the full name of the new user
     * @return a UserDto representing the newly registered user
     * @throws RegistrationException   if registration fails
     * @throws AuthenticationException if authentication-related issues occur
     * @throws DataAccessException     if there is an error accessing the data source
     */
    @PostMapping("/register")
    public UserDto register(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String email,
                            @RequestParam String fullName) throws RegistrationException, AuthenticationException, DataAccessException {
        User user = authService.registerCustomer(username, password, email, fullName);
        return UserMapper.toDto(user);
    }

    /**
     * Logs the user out and invalidates the session.
     *
     * @param request the HttpServletRequest used to retrieve the current session
     * @return a message indicating the logout status
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String token = session.getId();
            authService.logout(token);
            session.invalidate();
        }
        return "Logged out successfully";
    }
}