package com.andersen.ui.controller;

import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.service.dto.booking.BookingDto;
import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.service.mapper.BookingMapper;
import com.andersen.service.auth.AuthService;
import com.andersen.service.booking.BookingService;
import com.andersen.service.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing bookings.
 *
 * This controller provides endpoints for creating, retrieving, and canceling bookings.
 * Access to certain methods is restricted based on user roles (ADMIN, CUSTOMER).
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final AuthService userService;

    /**
     * Constructs a BookingController with the specified BookingService and AuthService.
     *
     * @param bookingService the BookingService to handle booking logic
     * @param userService    the AuthService to manage user authentication
     */
    @Autowired
    public BookingController(BookingService bookingService, AuthService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    /**
     * Creates a new booking for the authenticated user.
     *
     * @param dto         the BookingDto containing booking details
     * @param userDetails the authenticated user's details
     * @return ResponseEntity containing the created BookingDto
     * @throws BookingServiceException if there is an error in the booking service
     * @throws DataAccessException      if there is a database access error
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto dto,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) throws BookingServiceException, DataAccessException {
        logger.info("Request to create booking received for user: {}", userDetails.getUsername());
        try {
            User customer = userService.findById(userDetails.getId());
            Booking created = bookingService.createBooking(customer, dto.getWorkspaceId(), dto.getStartTime(), dto.getEndTime());
            logger.info("Booking created successfully for user: {}", userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(BookingMapper.toDto(created));
        } catch (Exception e) {
            logger.error("Error occurred while creating booking for user: {}", userDetails.getUsername(), e);
            throw e;
        }
    }

    /**
     * Retrieves all bookings for a specific user.
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @return ResponseEntity containing a list of BookingDto for the specified user
     * @throws BookingServiceException if there is an error in the booking service
     * @throws DataAccessException      if there is a database access error
     */
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingsByUser(@PathVariable Long userId) throws BookingServiceException, DataAccessException {
        logger.info("Request to get bookings for user ID: {}", userId);
        try {
            List<Booking> bookings = bookingService.getCustomerBookings(userId);
            List<BookingDto> dtos = bookings.stream()
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
            logger.info("Successfully retrieved bookings for user ID: {}", userId);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving bookings for user ID: {}", userId, e);
            throw e;
        }
    }

    /**
     * Cancels a booking for a specific user.
     *
     * @param bookingId the ID of the booking to be cancelled
     * @param userId    the ID of the user requesting the cancellation
     * @return ResponseEntity with no content if successful, or a status indicating failure
     * @throws BookingServiceException if there is an error in the booking service
     * @throws DataAccessException      if there is a database access error
     */
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @DeleteMapping("/{bookingId}/user/{userId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId,
                                              @PathVariable Long userId) throws BookingServiceException, DataAccessException {
        logger.info("Request to cancel booking ID: {} for user ID: {}", bookingId, userId);
        try {
            boolean cancelled = bookingService.cancelBooking(bookingId, userId);
            if (cancelled) {
                logger.info("Booking ID: {} cancelled successfully for user ID: {}", bookingId, userId);
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Booking ID: {} could not be cancelled for user ID: {}", bookingId, userId);
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
        } catch (Exception e) {
            logger.error("Error occurred while cancelling booking ID: {} for user ID: {}", bookingId, userId, e);
            throw e;
        }
    }

    /**
     * Retrieves all bookings in the system.
     *
     * @return ResponseEntity containing a list of all BookingDto
     * @throws DataAccessException if there is a database access error
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() throws DataAccessException, com.andersen.service.exception.DataAccessException {
        logger.info("Request to get all bookings received");
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            List<BookingDto> dtos = bookings.stream()
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
            logger.info("Successfully retrieved all bookings");
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all bookings", e);
            throw e;
        }
    }
}