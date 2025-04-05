package com.andersen.controller;

import com.andersen.logger.logger.OutputLogger;

/**
 * Class responsible for displaying menus in the application.
 */

public class MenuDisplayer {
    private static final String MAIN_MENU = """
            \n=== Coworking Space Reservation ===
            1. Admin Login
            2. Customer Login/Register
            3. Exit""";

    private static final String CUSTOMER_MENU = """
            \n=== Customer Menu ===
            1. Browse available spaces
            2. Make a reservation
            3. View my reservations
            4. Cancel a reservation
            5. Logout""";

    private static final String ADMIN_MENU = """
            \n=== Admin Menu ===
            1. Add a new coworking space
            2. Remove a coworking space
            3. View all reservations
            4. Back to Main Menu""";

    /**
     * Displays the main menu.
     */
    public static void showMainMenu() {
        OutputLogger.log(MAIN_MENU);
    }

    /**
     * Displays the customer menu.
     */
    public static void showCustomerMenu() {
        OutputLogger.log(CUSTOMER_MENU);
    }

    /**
     * Displays the admin menu.
     */
    public static void showAdminMenu() {
        OutputLogger.log(ADMIN_MENU);
    }
}