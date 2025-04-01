package com.andersen.controller;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.service.booking.BookingService;
import com.andersen.service.exception.BookingServiceException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for handling booking operations related to users.
 * This class provides endpoints for viewing customer bookings,
 * creating new bookings, cancelling bookings, and retrieving available workspaces.
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Constructs a BookingController with the specified BookingService.
     *
     * @param bookingService the BookingService used for managing bookings.
     */
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Retrieves a list of bookings for a specified customer.
     *
     * @param customerId the ID of the customer whose bookings are to be retrieved.
     * @param model the model to hold attributes for the view.
     * @param session the HttpSession to access the current user information.
     * @return the view name for displaying the list of bookings.
     */
    @GetMapping
    public String getCustomerBookings(
            @RequestParam Long customerId,
            Model model,
            HttpSession session) {

        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null || !currentUser.getId().equals(customerId)) {
                model.addAttribute("error", "Unauthorized access");
                return "error";
            }

            List<Booking> bookings = bookingService.getCustomerBookings(customerId);
            model.addAttribute("bookings", bookings);
            return "bookings/list";
        } catch (BookingServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * Displays the form for creating a new booking.
     *
     * @param workspaceId the ID of the workspace to be booked.
     * @param model the model to hold attributes for the view.
     * @param session the HttpSession to access the current user information.
     * @return the view name for the create booking page.
     */
    @GetMapping("/create")
    public String showCreateBookingForm(
            @RequestParam Long workspaceId,
            Model model,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("booking", new Booking());
        model.addAttribute("workspaceId", workspaceId);
        return "bookings/create";
    }

    /**
     * Processes the creation of a new booking.
     *
     * @param booking the Booking object containing booking details.
     * @param workspaceId the ID of the workspace being booked.
     * @param session the HttpSession to access the current user information.
     * @param redirectAttributes attributes to pass for redirection.
     * @return a redirect URL depending on the outcome of the booking creation.
     */
    @PostMapping("/create")
    public String createBooking(
            @ModelAttribute Booking booking,
            @RequestParam Long workspaceId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return "redirect:/login";
            }

            bookingService.createBooking(currentUser, workspaceId,
                    booking.getStartTime(), booking.getEndTime());

            redirectAttributes.addFlashAttribute("success", "Booking created successfully!");
            return "redirect:/bookings?customerId=" + currentUser.getId();
        } catch (BookingServiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addAttribute("workspaceId", workspaceId);
            return "redirect:/bookings/create";
        }
    }

    /**
     * Processes the cancellation of a booking.
     *
     * @param bookingId the ID of the booking to be cancelled.
     * @param userId the ID of the user requesting the cancellation.
     * @param session the HttpSession to access the current user information.
     * @param redirectAttributes attributes to pass for redirection.
     * @return a redirect URL depending on the outcome of the booking cancellation.
     */
    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam Long userId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null || !currentUser.getId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized action");
                return "redirect:/login";
            }

            bookingService.cancelBooking(bookingId, userId);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
            return "redirect:/bookings?customerId=" + userId;
        } catch (BookingServiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings?customerId=" + userId;
        }
    }

    /**
     * Retrieves a list of available workspaces for a given time period.
     *
     * @param startTime the start time of the desired booking period.
     * @param endTime the end time of the desired booking period.
     * @param model the model to hold attributes for the view.
     * @return the view name for displaying available workspaces.
     */
    @GetMapping("/available")
    public String getAvailableWorkspaces(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            Model model) {

        try {
            List<Workspace> availableWorkspaces = bookingService.getAvailableWorkspaces(startTime, endTime);
            model.addAttribute("availableWorkspaces", availableWorkspaces);
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
            return "bookings/available";
        } catch (BookingServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}