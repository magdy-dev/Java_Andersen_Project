package com.andersen.controller;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.service.booking.BookingService;
import com.andersen.service.exception.BookingServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Controller for managing bookings within the workspace application.
 * This controller handles requests related to bookings, including:
 * <ul>
 *     <li>Retrieving bookings for a specific customer</li>
 *     <li>Displaying the booking creation form</li>
 *     <li>Creating a new booking</li>
 *     <li>Cancelling existing bookings</li>
 *     <li>Retrieving available workspaces for specified time slots</li>
 * </ul>
 * Each method manages user interactions and communicates with the BookingService
 * to perform operations related to booking management.
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Retrieves the list of bookings for a specific customer.
     *
     * @param customerId the ID of the customer for whom to retrieve bookings.
     * @param model the model to add attributes for the view.
     * @return the name of the view that displays the list of bookings, or an error view if an exception occurs.
     */
    @GetMapping
    public String getCustomerBookings(@RequestParam Long customerId, Model model) {
        try {
            List<Booking> bookings = bookingService.getCustomerBookings(customerId);
            model.addAttribute("bookings", bookings);
            return "bookingList"; // Return the view for listing bookings
        } catch (BookingServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Return to an error view
        }
    }

    /**
     * Displays the form for creating a new booking.
     *
     * @param model the model to add attributes for the view.
     * @return the name of the view to display the booking creation form.
     */
    @GetMapping("/create")
    public String showCreateBookingForm(Model model) {
        model.addAttribute("booking", new Booking()); // Prepare a new Booking object for the form
        return "createBooking"; // Return the view for creating a booking
    }

    /**
     * Handles the creation of a new booking.
     *
     * @param booking the booking details provided by the user.
     * @param workspaceId the ID of the workspace to be booked.
     * @param model the model to add attributes for the view.
     * @return a redirect to the list of bookings if the creation is successful;
     *         otherwise returns to the booking creation form with an error message.
     */
    @PostMapping("/create")
    public String createBooking(@ModelAttribute Booking booking, @RequestParam Long workspaceId, Model model) {
        try {
            User currentUser = getCurrentUser(); // Implement this method to get the logged-in user
            bookingService.createBooking(currentUser, workspaceId, booking.getStartTime(), booking.getEndTime());
            return "redirect:/bookings?customerId=" + currentUser.getId(); // Redirect to the list of bookings
        } catch (BookingServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "createBooking"; // Return to the form with an error message
        }
    }

    /**
     * Cancels an existing booking.
     *
     * @param bookingId the ID of the booking to cancel.
     * @param userId the ID of the user canceling the booking.
     * @param model the model to add attributes for the view.
     * @return a redirect to the list of bookings if the cancellation is successful;
     *         otherwise returns to an error view with an error message.
     */
    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId, @RequestParam Long userId, Model model) {
        try {
            bookingService.cancelBooking(bookingId, userId);
            return "redirect:/bookings?customerId=" + userId; // Redirect to the list of bookings
        } catch (BookingServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Return to an error view
        }
    }
    /**
     * Retrieves available workspaces within a specified time range.
     *
     * @param startTime the start time of the booking period.
     * @param endTime the end time of the booking period.
     * @param model the model to add attributes for the view.
     * @return the name of the view that displays available workspaces, or an error view if an exception occurs.
     */
    @GetMapping("/available")
    public String getAvailableWorkspaces(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime, Model model) {
        try {
            List<Workspace> availableWorkspaces = bookingService.getAvailableWorkspaces(startTime, endTime);
            model.addAttribute("availableWorkspaces", availableWorkspaces);
            return "availableWorkspaces"; // Return the view for displaying available workspaces
        } catch (BookingServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Return to an error view
        }
    }

    /**
     * A placeholder method to retrieve the currently logged-in user.
     * This needs to be implemented to get the actual user information.
     *
     * @return the currently logged-in user.
     */
    private User getCurrentUser () {
        // Implement logic to retrieve the currently logged-in user
        return new User(); // Replace with actual user retrieval logic
    }
}