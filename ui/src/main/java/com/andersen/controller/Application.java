package com.andersen.controller;

import com.andersen.service.auth.AuthService;
import com.andersen.service.excption.AuthenticationException;

import java.util.Scanner;

public class Application {
    private final Scanner scanner;
    private final AuthService authService;
    private final Admin admin;
    private final Customer customer;
    private final MenuDisplayer menuDisplayer;

    public Application(Scanner scanner,
                       AuthService authService,
                       Admin admin,
                       Customer customer,
                       MenuDisplayer menuDisplayer) {
        this.scanner = scanner;
        this.authService = authService;
        this.admin = admin;
        this.customer = customer;
        this.menuDisplayer = menuDisplayer;
    }



    public void start() {
        while (true) {
            menuDisplayer.showMainMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> handleAdminFlow();
                case 2 -> handleCustomerFlow();
                case 3 -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleAdminFlow() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            authService.login(username, password);
            if (authService.isAdmin()) {
                admin.start();
            } else {
                System.out.println("Access denied. Not an admin account.");
            }
        } catch (Exception | AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void handleCustomerFlow() {
        menuDisplayer.showCustomerAuthMenu();
        int choice = readIntInput("Enter your choice: ");

        if (choice == 1) {
            handleCustomerLogin();
        } else if (choice == 2) {
            handleCustomerRegistration();
        } else {
            System.out.println("Returning to main menu.");
        }
    }

    private void handleCustomerLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            authService.login(username, password);
            customer.start();
        } catch (Exception | AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void handleCustomerRegistration() {
        System.out.print("Choose username: ");
        String username = scanner.nextLine();
        System.out.print("Choose password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        try {
            authService.registerCustomer(username, password, email, fullName);
            customer.start();
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private int readIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}