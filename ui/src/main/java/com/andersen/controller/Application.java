package com.andersen.controller;

import com.andersen.service.auth.AuthService;
import com.andersen.entity.role.User;
import com.andersen.service.Security.SessionManager;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.logger.logger.Out_put_Logger; // Import the OutputLogger
import java.util.Scanner;

public class Application {
    private final AuthService authService;
    private final SessionManager sessionManager;
    private final Scanner scanner;
    private final Admin admin;
    private final Customer customer;

    public Application(Scanner scanner,
                       AuthService authService,
                       Admin admin,
                       Customer customer,
                       SessionManager sessionManager) {
        this.scanner = scanner;
        this.authService = authService;
        this.admin = admin;
        this.customer = customer;
        this.sessionManager = sessionManager;
    }

    public void start() {
        boolean running = true;
        while (running) {
            MenuDisplayer.showMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleAdminLogin();
                    break;
                case "2":
                    handleCustomerLogin();
                    break;
                case "3":
                    running = false;
                    Out_put_Logger.log("Exiting application..."); // Logging exit
                    break;
                default:
                    Out_put_Logger.warn("Invalid choice, please try again."); // Logging warning
            }
        }
    }

    private void handleAdminLogin() {
        Out_put_Logger.log("Enter admin username: ");
        String username = scanner.nextLine();
        Out_put_Logger.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(username, password);
            String token = sessionManager.createSession(user);
            admin.setCurrentToken(token);

            if (sessionManager.isAdmin(token)) {
                Out_put_Logger.log("Admin login successful!");
                admin.start();
            } else {
                Out_put_Logger.warn("Access denied. Admin privileges required.");
                sessionManager.invalidateSession(token);
            }
        } catch (AuthenticationException e) {
            Out_put_Logger.error("Login failed: " + e.getMessage()); // Logging error
        }
    }

    private void handleCustomerLogin() {
        Out_put_Logger.log("Enter username: ");
        String username = scanner.nextLine();
        Out_put_Logger.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(username, password);
            String token = sessionManager.createSession(user);
            customer.setCurrentToken(token);
            Out_put_Logger.log("Login successful!");
            customer.start();
        } catch (AuthenticationException e) {
            Out_put_Logger.error("Login failed: " + e.getMessage()); // Logging error
        }
    }
}