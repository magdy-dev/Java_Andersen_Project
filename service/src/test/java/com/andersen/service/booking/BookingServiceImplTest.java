package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import com.andersen.repository.booking.BookingRepository;
import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.service.auth.SessionManager;
import com.andersen.service.excption.BookingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private SessionManager sessionManager;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testUser;
    private Workspace testWorkspace;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "password", "test@email.com", "Test User", null);
        testWorkspace = new Workspace(1L, "Workspace 1", "Description", null, 10.0, 4, true, null);
        startTime = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
        endTime = startTime.plusHours(3);
    }

    @Test
    void createBooking_ShouldSuccess() throws DataAccessException, BookingException {
        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(testWorkspace);
        when(bookingRepository.create(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });

        Booking result = bookingService.createBooking(testUser, 1L, startTime, endTime);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(testUser, result.getCustomer());
        assertEquals(testWorkspace, result.getWorkspace());
        verify(bookingRepository).create(any(Booking.class));
    }

    @Test
    void createBooking_ShouldThrowWhenWorkspaceNotFound() throws DataAccessException {
        when(workspaceRepository.getWorkspaceById(1L));

        assertThrows(BookingException.class,
                () -> bookingService.createBooking(testUser, 1L, startTime, endTime));
    }

    @Test
    void createBooking_ShouldThrowWhenWorkspaceInactive() throws DataAccessException {
        testWorkspace.setActive(false);
        when(workspaceRepository.getWorkspaceById(1L));

        assertThrows(BookingException.class,
                () -> bookingService.createBooking(testUser, 1L, startTime, endTime));
    }

    @Test
    void createBooking_ShouldThrowWhenInvalidTime() {
        // End time before start time
        assertThrows(BookingException.class,
                () -> bookingService.createBooking(testUser, 1L, endTime, startTime));
    }

    @Test
    void getCustomerBookings_ShouldReturnList() throws DataAccessException, BookingException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                startTime, endTime, BookingStatus.CONFIRMED, 30.0);
        when(bookingRepository.getByCustomer(1L));

        List<Booking> result = bookingService.getCustomerBookings(1L);

        assertEquals(1, result.size());
        assertEquals(testBooking, result.get(0));
    }

    @Test
    void getCustomerBookings_ShouldThrowWhenCustomerIdNull() {
        assertThrows(BookingException.class,
                () -> bookingService.getCustomerBookings(null));
    }

    @Test
    void cancelBooking_ShouldSuccess() throws DataAccessException, BookingException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                startTime, endTime, BookingStatus.CONFIRMED, 30.0);
        when(bookingRepository.getById(1L));

        boolean result = bookingService.cancelBooking(1L, 1L);

        assertTrue(result);
        assertEquals(BookingStatus.CANCELLED, testBooking.getStatus());
        verify(bookingRepository).update(testBooking);
    }

    @Test
    void cancelBooking_ShouldReturnFalseWhenAlreadyCancelled() throws DataAccessException, BookingException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                startTime, endTime, BookingStatus.CANCELLED, 30.0);
        when(bookingRepository.getById(1L));

        boolean result = bookingService.cancelBooking(1L, 1L);

        assertFalse(result);
        verify(bookingRepository, never()).update(any());
    }

    @Test
    void cancelBooking_ShouldThrowWhenUnauthorized() throws DataAccessException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                startTime, endTime, BookingStatus.CONFIRMED, 30.0);
        when(bookingRepository.getById(1L));

        assertThrows(BookingException.class,
                () -> bookingService.cancelBooking(1L, 2L)); // Different user ID
    }

    @Test
    void getAvailableWorkspaces_ShouldReturnList() throws DataAccessException, BookingException {
        when(workspaceRepository.getAvailableWorkspaces(startTime, endTime))
                .thenReturn(List.of(testWorkspace));

        List<Workspace> result = bookingService.getAvailableWorkspaces(startTime, endTime);

        assertEquals(1, result.size());
        assertEquals(testWorkspace, result.get(0));
    }

    @Test
    void getAvailableWorkspaces_ShouldThrowWhenInvalidTime() {
        assertThrows(BookingException.class,
                () -> bookingService.getAvailableWorkspaces(endTime, startTime)); // End before start
    }

    @Test
    void getAvailableWorkspaces_ShouldReturnEmptyList() throws DataAccessException, BookingException {
        when(workspaceRepository.getAvailableWorkspaces(startTime, endTime))
                .thenReturn(Collections.emptyList());
        List<Workspace> result = bookingService.getAvailableWorkspaces(startTime, endTime);
        assertTrue(result.isEmpty());
    }
}