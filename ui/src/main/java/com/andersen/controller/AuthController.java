package com.andersen.controller;

import com.andersen.entity.role.User;
import com.andersen.service.auth.AuthService;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling authentication-related operations such as login and registration.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Displays the login form.
     *
     * @return the name of the view to display the login form.
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Return the view for the login form
    }

    /**
     * Handles the login process.
     *
     * @param username the username provided by the user.
     * @param password the password provided by the user.
     * @param model the model to add attributes for the view.
     * @return a redirect to the home page if login is successful;
     *         otherwise returns to the login form with an error message.
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            User user = authService.login(username, password);
            model.addAttribute("user", user);
            return "redirect:/home"; // Redirect to the home page after successful login
        } catch (AuthenticationException e) {
            model.addAttribute("error", e.getMessage());
            return "login"; // Return to the login form with an error message
        }
    }

    /**
     * Displays the registration form.
     *
     * @return the name of the view to display the registration form.
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // Return the view for the registration form
    }

    /**
     * Handles the registration process.
     *
     * @param username the username provided by the user.
     * @param password the password provided by the user.
     * @param email the email provided by the user.
     * @param fullName the full name of the user.
     * @param model the model to add attributes for the view.
     * @return a redirect to the home page if registration is successful;
     *         otherwise returns to the registration form with an error message.
     */
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String email, @RequestParam String fullName, Model model) {
        try {
            User newUser = authService.registerCustomer(username, password, email, fullName);
            model.addAttribute("user", newUser);
            return "redirect:/home"; // Redirect to the home page after successful registration
        } catch (RegistrationException | AuthenticationException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Return to the registration form with an error message
        }
    }

    /**
     * Handles the logout process.
     *
     * @param token the token associated with the user session.
     * @return a redirect to the login page after logging out.
     */
    @PostMapping("/logout")
    public String logout(@RequestParam String token) {
        authService.logout(token);
        return "redirect:/auth/login"; // Redirect to the login page after logout
    }
}