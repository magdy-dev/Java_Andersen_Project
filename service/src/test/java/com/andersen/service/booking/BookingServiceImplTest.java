package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import com.andersen.repository.booking.BookingRepositoryEntityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    private BookingServiceImpl bookingService; // Service under test
    private BookingRepositoryEntityImpl bookingRepository; // Mock for the booking repository
    private Customer customer; // Customer entity for testing
    private Workspace workspace; // Workspace entity for testing
    private Booking booking; // Booking entity for testing

    // Setup method to initialize the test environment
    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        bookingRepository = mock(BookingRepositoryEntityImpl.class); // Create a mock repository
        bookingService = new BookingServiceImpl(bookingRepository); // Initialize the service with the mock

        // Create a customer and workspace for testing
        customer = new Customer("john_doe", "securePassword123");
        workspace = new Workspace(1, "Cozy Office", "Comfortable workspace.");
        booking = new Booking(1, customer, workspace, LocalTime.of(9, 0), LocalTime.of(11, 0));

        // Initialize the customer's bookings list
        customer.setBookings(new ArrayList<>());
    }

    // Test for creating a booking
    @Test
    void createBooking() {
        // Mock the ID generation for the booking
        when(bookingRepository.generateId()).thenReturn(1L);

        // Call the method to create a booking
        Booking newBooking = bookingService.createBooking(customer, workspace, LocalTime.of(9, 0), LocalTime.of(11, 0));

        // Assertions to verify the booking was created correctly
        assertNotNull(newBooking, "New booking should not be null.");
        assertEquals(customer, newBooking.getCustomer(), "Customer should match.");
        assertEquals(workspace, newBooking.getWorkspace(), "Workspace should match.");
        assertEquals(LocalTime.of(9, 0), newBooking.getStartTime(), "Start time should match.");
        assertEquals(LocalTime.of(11, 0), newBooking.getEndTime(), "End time should match.");
    }

    // Test for making a reservation
    @Test
    void makeReservation() {
        // Mock the ID generation for the booking
        when(bookingRepository.generateId()).thenReturn(1L);

        // Create a new booking
        Booking newBooking = bookingService.createBooking(customer, workspace, LocalTime.of(9, 0), LocalTime.of(11, 0));
        // Make a reservation for the customer
        bookingService.makeReservation(customer, newBooking);

        // Assertions to verify the reservation was made correctly
        assertEquals(1, customer.getBookings().size(), "Customer should have 1 booking.");
        assertEquals(newBooking, customer.getBookings().get(0), "Booking should match the newly created booking.");
        verify(bookingRepository, times(1)).addBooking(newBooking); // Verify that the repository's addBooking method was called
    }

    // Test for canceling a reservation
    @Test
    void cancelReservation() {
        // Mock the ID generation for the booking
        when(bookingRepository.generateId()).thenReturn(1L);
        // Create and reserve a new booking
        Booking newBooking = bookingService.createBooking(customer, workspace, LocalTime.of(9, 0), LocalTime.of(11, 0));
        bookingService.makeReservation(customer, newBooking);

        // Assertions before cancellation
        assertEquals(1, customer.getBookings().size(), "Customer should have 1 booking before cancellation.");

        // Cancel the reservation
        bookingService.cancelReservation(customer, newBooking.getId());

        // Assertions after cancellation
        assertEquals(0, customer.getBookings().size(), "Customer should have 0 bookings after cancellation.");
        verify(bookingRepository, times(1)).removeBooking(newBooking); // Verify that the repository's removeBooking method was called
    }

    // Test for retrieving customer bookings
    @Test
    void getCustomerBookings() {
        // Mock the ID generation for the booking
        when(bookingRepository.generateId()).thenReturn(1L);
        // Create and reserve a new booking
        Booking newBooking = bookingService.createBooking(customer, workspace, LocalTime.of(9, 0), LocalTime.of(11, 0));
        bookingService.makeReservation(customer, newBooking);

        // Retrieve the customer's bookings
        List<Booking> bookings = bookingService.getCustomerBookings(customer);

        // Assertions to verify the retrieved bookings
        assertEquals(1, bookings.size(), "Customer should have 1 booking.");
        assertEquals(newBooking, bookings.get(0), "Retrieved booking should match the reserved booking.");
    }
}