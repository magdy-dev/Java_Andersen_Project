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
import com.andersen.service.security.CustomUserDetailsService;
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

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final AuthService userService;
    private final WorkspaceService workspaceService;

    @Autowired
    public BookingController(BookingService bookingService,
                             AuthService userService,
                             WorkspaceService workspaceService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.workspaceService = workspaceService;
    }

    /**
     * Creates a new booking. Only customers can create bookings for themselves.
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody BookingDto dto,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            User customer = userService.findById(userDetails.getId());
            Optional<Workspace> workspaceOpt = workspaceService.getWorkspaceById(dto.getWorkspaceId().getId());

            if (workspaceOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            bookingService.createBooking(customer, dto.getWorkspaceId().getId(), dto.getStartTime(), dto.getEndTime());
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (BookingServiceException | WorkspaceServiceException e) {
            return ResponseEntity.badRequest().build();
        } catch (DataAccessException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Customers can retrieve their own bookings. Admins can retrieve anyone's.
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
     * Cancels a booking. Admins can cancel any booking. Customers can cancel their own.
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
     * Only admins can view all bookings.
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
