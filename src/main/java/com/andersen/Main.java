package com.andersen;

import com.andersen.controller.MenuController;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.repository.booking.BookingRepositoryImpl; 
import com.andersen.repository.workspace.WorkspaceRepositoryImpl;
import com.andersen.service.booking.BookingService;
import com.andersen.service.booking.BookingServiceImpl;
import com.andersen.service.workspace.WorkspaceService;
import com.andersen.service.workspace.WorkspaceServiceImpl;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws WorkspaceNotFoundException {
        Scanner scanner = new Scanner(System.in);

       
        WorkspaceRepositoryImpl workspaceRepository = new WorkspaceRepositoryImpl();
        WorkspaceService workspaceService = new WorkspaceServiceImpl(workspaceRepository); 


        BookingRepositoryImpl bookingRepository = new BookingRepositoryImpl(); 
        BookingService bookingService = new BookingServiceImpl(bookingRepository); 

        MenuController menuController = new MenuController(workspaceService, bookingService, scanner);
        menuController.mainMenu();


        scanner.close();
    }
}
