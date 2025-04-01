package com.andersen.controller;

import com.andersen.entity.role.User;
import com.andersen.service.auth.AuthService;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for handling user authentication and registration requests.
 * This class provides endpoints for user login, registration, and logout
 * functionalities in a web application.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs an AuthController with the specified AuthService.
     *
     * @param authService the AuthService used for authentication and registration operations.
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Displays the login form to the user.
     *
     * @param redirect the redirect URL to return to after login, if present.
     * @param model the model to hold attributes for the view.
     * @return the view name for the login page.
     */
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "auth/login";
    }

    /**
     * Processes the login form submission.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param redirect the redirect URL to return to after successful login, if present.
     * @param session the HttpSession to store the current user.
     * @param redirectAttributes attributes to pass for redirection.
     * @return a redirect URL depending on the login outcome.
     */
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String redirect,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            User user = authService.login(username, password);
            session.setAttribute("currentUser", user);

            if (redirect != null && !redirect.isBlank()) {
                return "redirect:" + redirect;
            }
            return "redirect:/";

        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addAttribute("redirect", redirect);
            return "redirect:/auth/login";
        }
    }

    /**
     * Displays the registration form to the user.
     *
     * @return the view name for the registration page.
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "auth/register";
    }

    /**
     * Processes the registration form submission.
     *
     * @param username the desired username of the new user.
     * @param password the desired password of the new user.
     * @param email the email address of the new user.
     * @param fullName the full name of the new user.
     * @param session the HttpSession to store the current user.
     * @param redirectAttributes attributes to pass for redirection.
     * @return a redirect URL depending on the registration outcome.
     */
    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String fullName,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            User newUser = authService.registerCustomer(username, password, email, fullName);
            session.setAttribute("currentUser", newUser);
            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/";

        } catch (RegistrationException | AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("fullName", fullName);
            return "redirect:/auth/register";
        }
    }

    /**
     * Processes the logout request.
     *
     * @param session the HttpSession to invalidate.
     * @return the redirect URL to the login page.
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.invalidate();
        return "redirect:/auth/login";
    }
}