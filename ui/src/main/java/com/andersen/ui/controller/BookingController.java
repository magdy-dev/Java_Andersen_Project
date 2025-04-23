package com.andersen.ui.controller;

import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.commend.BookingCommand;
import com.andersen.service.commend.BookingCommandExecutor;
import com.andersen.service.commend.CreateBookingCommand;
import com.andersen.service.exception.BookingServiceException;
import com.andersen.ui.dto.booking.BookingDto;
import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.role.User;
import com.andersen.ui.mapper.BookingMapper;
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
import java.util.Map;
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
    private final BookingCommandExecutor commandExecutor;
    /**
     * Constructs a BookingController with the specified BookingService and AuthService.
     *
     * @param bookingService the BookingService to handle booking logic
     * @param userService    the AuthService to manage user authentication
     */
    @Autowired
    public BookingController(BookingService bookingService, AuthService userService, BookingCommandExecutor commandExecutor) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.commandExecutor = commandExecutor;
    }


    /**
     * Creates a new booking based on the provided BookingDto.
     *
     * @param dto         the data transfer object containing booking details.
     * @param userDetails the authenticated user's details.
     * @return a ResponseEntity containing the created Booking object.
     *         - Returns 201 CREATED with the booking if successful.
     *         - Returns 400 BAD REQUEST if there is a data access error.
     *         - Returns 500 INTERNAL SERVER ERROR for any unexpected errors.
     * @throws DataAccessException if an error occurs while accessing data.
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDto dto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) throws DataAccessException {
        logger.info("Start CreateBookingCommand for user {}", userDetails.getUsername());
        User customer = userService.findById(userDetails.getId());
        BookingCommand<Booking> command = new CreateBookingCommand(
                bookingService,
                customer,
                dto.getWorkspaceId(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        try {
            Booking booking = commandExecutor.execute(command);
            BookingDto result = BookingMapper.toDto(booking);
            logger.info("Booking created successfully: id={} for user={}",
                    booking.getId(), userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (DataAccessException dae) {
            logger.error("DataAccessException while creating booking for user {}: {}",
                    userDetails.getUsername(), dae.getMessage(), dae);
            // You can return a structured error object or just the message
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", dae.getMessage()));

        } catch (Exception ex) {
            logger.error("Unexpected error while creating booking for user {}: {}",
                    userDetails.getUsername(), ex.getMessage(), ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + ex.getMessage()));
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