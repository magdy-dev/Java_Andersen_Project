package com.andersen.controller;

import com.andersen.service.auth.AuthService;
import com.andersen.entity.role.User;
import com.andersen.service.auth.SessionManager;
import com.andersen.service.excption.AuthenticationException;
import com.andersen.logger.OutputLogger; // Import the OutputLogger
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
                    OutputLogger.log("Exiting application..."); // Logging exit
                    break;
                default:
                    OutputLogger.warn("Invalid choice, please try again."); // Logging warning
            }
        }
    }

    private void handleAdminLogin() {
        OutputLogger.log("Enter admin username: ");
        String username = scanner.nextLine();
        OutputLogger.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(username, password);
            String token = sessionManager.createSession(user);
            admin.setCurrentToken(token);

            if (sessionManager.isAdmin(token)) {
                OutputLogger.log("Admin login successful!");
                admin.start();
            } else {
                OutputLogger.warn("Access denied. Admin privileges required.");
                sessionManager.invalidateSession(token);
            }
        } catch (AuthenticationException e) {
            OutputLogger.error("Login failed: " + e.getMessage()); // Logging error
        }
    }

    private void handleCustomerLogin() {
        OutputLogger.log("Enter username: ");
        String username = scanner.nextLine();
        OutputLogger.log("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = authService.login(username, password);
            String token = sessionManager.createSession(user);
            customer.setCurrentToken(token);
            OutputLogger.log("Login successful!");
            customer.start();
        } catch (AuthenticationException e) {
            OutputLogger.error("Login failed: " + e.getMessage()); // Logging error
        }
    }
}