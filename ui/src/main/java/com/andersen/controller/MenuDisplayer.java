package com.andersen.controller;

public class MenuDisplayer {
    public static void showMainMenu() {
        System.out.println("\n=== Coworking Space Reservation ===");
        System.out.println("1. Admin Login");
        System.out.println("2. Customer Login/Register");
        System.out.println("3. Exit");
    }

    public static void showCustomerAuthMenu() {
        System.out.println("\n=== Customer Authentication ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Back to Main Menu");
    }

    public static void showAdminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Add a new coworking space");
        System.out.println("2. Remove a coworking space");
        System.out.println("3. View all reservations");
        System.out.println("4. Back to Main Menu");
    }

    public static void showCustomerMenu() {
        System.out.println("\n=== Customer Menu ===");
        System.out.println("1. Browse available spaces");
        System.out.println("2. Make a reservation");
        System.out.println("3. View my reservations");
        System.out.println("4. Cancel a reservation");
        System.out.println("5. Logout");
    }
}