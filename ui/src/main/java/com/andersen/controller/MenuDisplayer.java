package com.andersen.controller;

import com.andersen.logger.OutputLogger;

public class MenuDisplayer {
    private static final String MAIN_MENU = """
            \n=== Coworking Space Reservation ===
            1. Admin Login
            2. Customer Login/Register
            3. Exit""";

    private static final String CUSTOMER_AUTH_MENU = """
            \n=== Customer Authentication ===
            1. Login
            2. Register
            3. Back to Main Menu""";

    private static final String ADMIN_MENU = """
            \n=== Admin Menu ===
            1. Add a new coworking space
            2. Remove a coworking space
            3. View all reservations
            4. Back to Main Menu""";

    private static final String CUSTOMER_MENU = """
            \n=== Customer Menu ===
            1. Browse available spaces
            2. Make a reservation
            3. View my reservations
            4. Cancel a reservation
            5. Logout""";

    public static void showMainMenu() {
        OutputLogger.log(MAIN_MENU);
    }

    public static void showCustomerAuthMenu() {
        OutputLogger.log(CUSTOMER_AUTH_MENU);
    }

    public static void showAdminMenu() {
        OutputLogger.log(ADMIN_MENU);
    }

    public static void showCustomerMenu() {
        OutputLogger.log(CUSTOMER_MENU);
    }
}