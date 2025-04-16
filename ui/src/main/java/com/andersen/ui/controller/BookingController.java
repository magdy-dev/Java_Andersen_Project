package com.andersen.ui.controller;

import com.andersen.domain.dto.booking.BookingDto;
import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.mapper.BookingMapper;
import com.andersen.service.auth.AuthService;
import com.andersen.service.booking.BookingService;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.service.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for handling booking-related requests.
 * <p>
 * This controller provides endpoints for creating, retrieving, and canceling bookings.
 * Access to these endpoints is restricted based on user roles.
 * </p>
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final AuthService userService;

    /**
     * Constructs a BookingController with the specified BookingService and AuthService.
     *
     * @param bookingService the service for managing bookings
     * @param userService    the service for managing user authentication
     */
    @Autowired
    public BookingController(BookingService bookingService, AuthService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    /**
     * Creates a new booking for the authenticated customer.
     *
     * @param dto         the booking details
     * @param userDetails the authenticated user's details
     * @return a ResponseEntity containing the created booking or an error message
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto dto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (dto.getWorkspaceId() == null) {
            return ResponseEntity.badRequest().body("Workspace ID is required.");
        }
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            return ResponseEntity.badRequest().body("Start time and end time are required.");
        }
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            return ResponseEntity.badRequest().body("Start time must be before end time.");
        }

        try {
            // Retrieve authenticated customer (from session/JWT)
            User customer = userService.findById(userDetails.getId());
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
            }

            Long workspaceId = dto.getWorkspaceId();
            // Optionally map customer into the dto if needed
            dto.setCustomer(new UserDto(customer.getId()));

            // Create booking. If the start time is in the past, this call will throw a BookingServiceException.
            Booking createdBooking = bookingService.createBooking(customer, workspaceId, dto.getStartTime(), dto.getEndTime());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (BookingServiceException e) {
            // Return 409 Conflict with the message from the exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Booking conflict: " + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Retrieves bookings for a specific user.
     *
     * @param userId the ID of the user whose bookings to retrieve
     * @return a ResponseEntity containing a list of BookingDto objects or an error response
     */
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingsByUser(@PathVariable Long userId) {
        try {
            List<Booking> bookings = bookingService.getCustomerBookings(userId);
            List<BookingDto> dtos = bookings.stream()
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (BookingServiceException | DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cancels a booking by ID.
     *
     * @param bookingId the ID of the booking to
     * @param userId    the ID of the user attempting to cancel the booking
     * @return a ResponseEntity indicating the outcome of the cancellation operation
     */
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @DeleteMapping("/{bookingId}/user/{userId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId,
                                              @PathVariable Long userId) {
        try {
            boolean cancelled = bookingService.cancelBooking(bookingId, userId);
            return cancelled
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (BookingServiceException | DataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves all bookings (admin only).
     *
     * @return a ResponseEntity containing a list of BookingDto objects or an error response
     * @throws com.andersen.service.exception.DataAccessException if there is a database access error
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() throws com.andersen.service.exception.DataAccessException {
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            List<BookingDto> dtos = bookings.stream()
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}