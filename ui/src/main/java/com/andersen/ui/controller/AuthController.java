package com.andersen.ui.controller;


import com.andersen.domain.entity.role.User;
import com.andersen.domain.mapper.UserMapper;
import com.andersen.service.auth.AuthService;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import com.andersen.domain.exception.DataAccessException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login page
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    // Login POST
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request,
                        Model model) {
        try {
            User user = authService.login(username, password);
            request.getSession().setAttribute("user", UserMapper.toDto(user));
            return "redirect:/dashboard";
        } catch (AuthenticationException | DataAccessException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    // Registration page
    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }

    // Registration POST
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String fullName,
                           Model model) {
        try {
            User user = authService.registerCustomer(username, password, email, fullName);
            model.addAttribute("success", "Registration successful. Please log in.");
            return "redirect:/auth/login";
        } catch (RegistrationException | DataAccessException | AuthenticationException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String token = session.getId();
            authService.logout(token);
            session.invalidate();
        }
        return "redirect:/auth/login?logout=true";
    }
}
