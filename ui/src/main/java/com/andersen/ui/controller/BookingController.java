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
import com.andersen.service.security.CustomUserDetails;
import com.andersen.service.workspace.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The BookingController class is a Spring REST controller that manages booking-related operations,
 * including creating bookings, retrieving bookings for users, canceling bookings, and retrieving all bookings.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final AuthService userService;
    private final WorkspaceService workspaceService;

    /**
     * Constructs a BookingController with the specified BookingService, AuthService, and WorkspaceService.
     *
     * @param bookingService   the service used to manage bookings
     * @param userService      the service used to access user information
     * @param workspaceService the service used to access workspace information
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
     * Creates a new booking. Only users with the CUSTOMER role can create bookings for themselves.
     *
     * @param dto         the BookingDto containing booking details
     * @param userDetails the currently authenticated user's details
     * @return ResponseEntity indicating the status of the booking creation
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody BookingDto dto,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            User customer = userService.findById(userDetails.getId());
            Long workspaceId = dto.getWorkspaceId().getId();

            Optional<Workspace> workspaceOpt = workspaceService.getWorkspaceById(workspaceId);

            if (workspaceOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            bookingService.createBooking(customer, workspaceId, dto.getStartTime(), dto.getEndTime());
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (BookingServiceException | WorkspaceServiceException e) {
            return ResponseEntity.badRequest().build();
        } catch (DataAccessException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves bookings for a specific user. Customers can retrieve their own bookings, while admins can retrieve any user's bookings.
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @return ResponseEntity containing a list of BookingDto if successful, or a bad request response if an error occurs
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
     * Cancels a booking. Admins can cancel any booking, and customers can cancel their own bookings.
     *
     * @param bookingId the ID of the booking to cancel
     * @param userId    the ID of the user attempting to cancel the booking
     * @return ResponseEntity indicating the status of the cancellation operation
     */
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @DeleteMapping("/{bookingId}/user/{userId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId,
                                              @PathVariable Long userId) {
        try {
            boolean cancelled = bookingService.cancelBooking(bookingId, userId);
            return cancelled ? ResponseEntity.noContent().build()
                    : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (BookingServiceException | DataAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves all bookings. This endpoint is accessible only to users with the ADMIN role.
     *
     * @return ResponseEntity containing a list of BookingDto if successful,
     * or an internal server error response if an error occurs
     * @throws com.andersen.service.exception.DataAccessException if there is an issue accessing booking data
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
