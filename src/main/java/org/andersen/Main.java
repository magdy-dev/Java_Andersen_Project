package org.andersen;


import org.andersen.entity.booking.Booking;
import org.andersen.entity.role.User;
import org.andersen.entity.users.Admin;
import org.andersen.entity.users.Customer;
import org.andersen.entity.workspace.Availability;
import org.andersen.entity.workspace.Workspace;
import org.andersen.service.auth.AuthServiceImp;
import org.andersen.service.booking.BookingServiceImpl;
import org.andersen.service.workspace.WorkspaceServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<User> users = new ArrayList<>();
    private static final List<Workspace> workspaces = new ArrayList<>();
    private static final List<Booking> bookings = new ArrayList<>();

    private static final AuthServiceImp AUTH_SERVICE_IMP = new AuthServiceImp(users);
    private static final WorkspaceServiceImpl WORKSPACE_SERVICE_IMPL = new WorkspaceServiceImpl(workspaces);
    private static final BookingServiceImpl BOOKING_SERVICE_IMPL = new BookingServiceImpl(bookings, WORKSPACE_SERVICE_IMPL);
    private static final Scanner scanner = new Scanner(System.in);

    private static final int SLOT_DURATION_HOURS = 2;

    public static void main(String[] args) {
        initializeSampleData();
        mainMenu();
    }

    private static void initializeSampleData() {
        users.add(new Admin("admin", "admin"));
        Workspace ws1 = new Workspace("Creative Hub", "Open space with whiteboards");
        ws1.getAvailabilities().add(new Availability(
                LocalDate.of(2024, 3, 20),
                LocalTime.of(10, 0),
                5
        ));
        workspaces.add(ws1);
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n=== Coworking Space Reservation ===");
            System.out.println("1. User Registration");
            System.out.println("2. User Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> registerUser();
                    case 2 -> customerLogin();
                    case 3 -> adminLogin();
                    case 4 -> System.exit(0);
                    default -> System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            AUTH_SERVICE_IMP.registerUser(username, password);
            System.out.println("Registration successful!");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    private static void customerLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            Customer customer = AUTH_SERVICE_IMP.loginCustomer(username, password);
            if (customer != null) {
                customerMenu(customer);
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private static void adminLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            Admin admin = AUTH_SERVICE_IMP.loginAdmin(username, password);
            if (admin != null) {
                adminMenu();
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Browse Available Workspaces");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View My Bookings");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                int bookingIndex=Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> browseWorkspaces();
                    case 2 -> makeReservation(customer);
                    case 3 -> BOOKING_SERVICE_IMPL.cancelReservation(customer,bookingIndex);
                    case 4 -> viewMyBookings(customer);
                    case 5 -> { return; }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Manage Workspaces");
            System.out.println("2. View All Bookings");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> manageWorkspaces();
                    case 2 -> viewAllBookings();
                    case 3 -> { return; }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void manageWorkspaces() {
        while (true) {
            System.out.println("\n=== Manage Workspaces ===");
            System.out.println("1. Add Workspace");
            System.out.println("2. Remove Workspace");
            System.out.println("3. Update Workspace");
            System.out.println("4. View Workspaces");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> addWorkspace();
                    case 2 -> removeWorkspace();
                    case 3 -> updateWorkspace();
                    case 4 -> viewWorkspaces();
                    case 5 -> { return; }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addWorkspace() {
        try {
            System.out.print("Enter workspace name: ");
            String name = scanner.nextLine();
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            WORKSPACE_SERVICE_IMPL.addWorkspace(name, description);
            int wsIndex = WORKSPACE_SERVICE_IMPL.getAllWorkspaces().size() - 1;

            while (true) {
                System.out.print("Enter availability date (YYYY-MM-DD) or 'done': ");
                String dateInput = scanner.nextLine();
                if (dateInput.equalsIgnoreCase("done")) break;

                LocalDate date = LocalDate.parse(dateInput);
                System.out.print("Enter time (HH:MM): ");
                LocalTime time = LocalTime.parse(scanner.nextLine());
                System.out.print("Enter capacity: ");
                int capacity = Integer.parseInt(scanner.nextLine());

                WORKSPACE_SERVICE_IMPL.addAvailabilityToWorkspace(
                        wsIndex,
                        new Availability(date, time, capacity)
                );
            }
            System.out.println("Workspace added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding workspace: " + e.getMessage());
        }
    }

    private static void removeWorkspace() {
        try {
            viewWorkspaces();
            System.out.print("Enter workspace number to remove: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            WORKSPACE_SERVICE_IMPL.removeWorkspace(index);
            System.out.println("Workspace removed successfully!");
        } catch (Exception e) {
            System.out.println("Error removing workspace: " + e.getMessage());
        }
    }

    private static void updateWorkspace() {
        try {
            viewWorkspaces();
            System.out.print("Enter workspace number to update: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.print("Enter new name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter new description: ");
            String newDesc = scanner.nextLine();

            WORKSPACE_SERVICE_IMPL.updateWorkspace(index, newName, newDesc);
            System.out.println("Workspace updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating workspace: " + e.getMessage());
        }
    }

    private static void viewWorkspaces() {
        System.out.println("\n=== All Workspaces ===");
        WORKSPACE_SERVICE_IMPL.getAllWorkspaces().forEach(ws -> {
            System.out.println("• " + ws.getName());
            System.out.println("  Description: " + ws.getDescription());
            System.out.println("  Available Slots: " + ws.getAvailabilities().size());
        });
    }

    private static void viewAllBookings() {
        System.out.println("\n=== All Bookings ===");
        BOOKING_SERVICE_IMPL.getAllBookings().forEach(booking -> {
            String workspaceName = booking.getWorkspace() != null
                    ? booking.getWorkspace().getName()
                    : "Deleted Workspace";

            System.out.println("Customer: " + booking.getCustomer().getUserName());
            System.out.println("Workspace: " + workspaceName);
            System.out.println("Date: " + booking.getDate());
            System.out.println("Time: " + booking.getTime());
            System.out.println("Status: " + isValidBooking(booking) + "\n");
        });
    }

    private static void browseWorkspaces() {
        System.out.println("\n=== Available Workspaces ===");
        WORKSPACE_SERVICE_IMPL.getAllWorkspaces().forEach(ws -> {
            System.out.println("• " + ws.getName() + " - " + ws.getDescription());
            ws.getAvailabilities().stream()
                    .filter(avail -> avail.getRemaining() > 0)
                    .forEach(avail -> {
                        System.out.println("  Date: " + avail.getDate());
                        System.out.println("  Time: " + avail.getTime() + " - " + avail.getTime().plusHours(SLOT_DURATION_HOURS));
                        System.out.println("  Available: " + avail.getRemaining() + "/" + avail.getCapacity());
                    });
        });
    }

    private static void makeReservation(Customer customer) {
        try {
            browseWorkspaces();
            System.out.print("Select workspace number: ");
            int wsIdx = Integer.parseInt(scanner.nextLine()) - 1;

            Workspace ws = WORKSPACE_SERVICE_IMPL.getAllWorkspaces().get(wsIdx);
            List<Availability> availabilities = ws.getAvailabilities();

            System.out.println("Available slots:");
            for (int i = 0; i < availabilities.size(); i++) {
                Availability avail = availabilities.get(i);
                if (avail.getRemaining() > 0) {
                    System.out.println((i + 1) + ". Date: " + avail.getDate());
                    System.out.println("   Time: " + avail.getTime() + " - " + avail.getTime().plusHours(SLOT_DURATION_HOURS));
                }
            }

            System.out.print("Select slot number: ");
            int slotIdx = Integer.parseInt(scanner.nextLine()) - 1;

            BOOKING_SERVICE_IMPL.makeReservation(customer, wsIdx, slotIdx);
            System.out.println("Reservation successful!");
        } catch (Exception e) {
            System.out.println("Reservation failed: " + e.getMessage());
        }
    }





    private static void viewMyBookings(Customer customer) {
        try {
            List<Booking> bookings = BOOKING_SERVICE_IMPL.getCustomerBookings(customer);

            if (bookings.isEmpty()) {
                System.out.println("\nYou have no bookings yet.");
                return;
            }

            System.out.println("\n=== My Bookings ===");
            System.out.println("Total bookings: " + bookings.size());
            System.out.println("----------------------------");

            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                System.out.println("Booking #" + (i + 1));
                System.out.println("Workspace: " + booking.getWorkspace().getName());
                System.out.println("Date:      " + booking.getDate());
                System.out.println("Time:      " + booking.getTime());
                System.out.println("Status:    " + (isValidBooking(booking) ? "Confirmed" : "Expired"));
                System.out.println("----------------------------");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving bookings: " + e.getMessage());
        }
    }

    // Helper method for demonstration
    private static boolean isValidBooking(Booking booking) {
        return booking.getDate().isAfter(LocalDate.now().minusDays(1));
    }
}