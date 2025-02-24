package org.andersen;

import org.andersen.controller.MenuController;
import org.andersen.exception.WorkspaceNotFoundException;
import org.andersen.repository.booking.BookingRepositoryImpl; // Ensure you have the correct import
import org.andersen.repository.workspace.WorkspaceRepositoryImpl;
import org.andersen.service.booking.BookingService;
import org.andersen.service.booking.BookingServiceImpl;
import org.andersen.service.workspace.WorkspaceService;
import org.andersen.service.workspace.WorkspaceServiceImpl;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws WorkspaceNotFoundException {
        Scanner scanner = new Scanner(System.in);

        // instances of the repositories
        WorkspaceRepositoryImpl workspaceRepository = new WorkspaceRepositoryImpl();
        WorkspaceService workspaceService = new WorkspaceServiceImpl(workspaceRepository); // Pass repository to service

        // instance of BookingRepository
        BookingRepositoryImpl bookingRepository = new BookingRepositoryImpl(); // Replace with actual implementation
        BookingService bookingService = new BookingServiceImpl(bookingRepository); // Pass the repository to the service

        // menu controller
        MenuController menuController = new MenuController(workspaceService, bookingService, scanner);
        menuController.mainMenu();

        // scanner when done
        scanner.close();
    }
}