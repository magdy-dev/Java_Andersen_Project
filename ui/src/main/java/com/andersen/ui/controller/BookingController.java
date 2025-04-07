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

    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody BookingDto dto) {
        try {
            User customer = userService.findById(dto.getCustomerId());
            Optional<Workspace> workspace = workspaceService.getWorkspaceById(dto.getWorkspaceId());

            bookingService.createBooking(customer, workspace.get().getId(), dto.getStartTime(), dto.getEndTime());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BookingServiceException | DataAccessException | WorkspaceServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

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

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            List<BookingDto> dtos = bookings.stream()
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (com.andersen.service.exception.DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
