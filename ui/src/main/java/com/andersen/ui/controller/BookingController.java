package com.andersen.ui.controller;

import com.andersen.domain.dto.booking.BookingDto;
import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.mapper.BookingMapper;
import com.andersen.service.auth.AuthService;
import com.andersen.service.booking.BookingService;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.workspace.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RESTful controller for managing bookings.
 * This class provides endpoints for creating, retrieving, and canceling bookings.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final AuthService userService;
    private final WorkspaceService workspaceService;

    /**
     * Constructs a BookingController with the specified services.
     *
     * @param bookingService   the BookingService to be used for bookings
     * @param userService      the AuthService to interact with user data
     * @param workspaceService the WorkspaceService to retrieve workspace data
     */
    @Autowired
    public BookingController(BookingService bookingService,
                             AuthService userService,
                             WorkspaceService workspaceService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.workspaceService = workspaceService;
    }

    /**
     * Creates a new booking based on the provided BookingDto.
     *
     * @param dto the BookingDto containing the details of the booking
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody BookingDto dto) {
        try {
            User customer = userService.findById(dto.getId());
            Optional<Workspace> workspaceOpt = workspaceService.getWorkspaceById(dto.getId());

            if (workspaceOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            bookingService.createBooking(customer, workspaceOpt.get().getId(), dto.getStartTime(), dto.getEndTime());
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (BookingServiceException | WorkspaceServiceException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (DataAccessException | RuntimeException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all bookings for a specific user.
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @return a ResponseEntity containing a list of BookingDto for the specified user
     */
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
     * Cancels a booking for a specified user.
     *
     * @param bookingId the ID of the booking to cancel
     * @param userId    the ID of the user attempting to cancel the booking
     * @return a ResponseEntity indicating the result of the cancellation operation
     */
    @DeleteMapping("/{bookingId}/user/{userId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId, @PathVariable Long userId) {
        try {
            boolean cancelled = bookingService.cancelBooking(bookingId, userId);
            return cancelled ? ResponseEntity.noContent().build()
                    : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (BookingServiceException | DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves all bookings in the system.
     *
     * @return a ResponseEntity containing a list of all BookingDto in the system
     */
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