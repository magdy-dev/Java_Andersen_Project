package com.andersen.service.booking;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import com.andersen.repository.booking.BookingRepository;
import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.service.excption.BookingServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testUser;
    private Workspace testWorkspace;
    private LocalDate testDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "password", "test@email.com", "Test User", null);
        testWorkspace = new Workspace(1L, "Workspace 1", "Description", null, 10.0, 4, true, null);
        testDate = LocalDate.now().plusDays(1);
        startTime = LocalTime.of(9, 0);
        endTime = LocalTime.of(12, 0);
    }

    @Test
    void createBooking_ShouldSuccess() throws DataAccessException, BookingServiceException {
        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(testWorkspace);
        when(bookingRepository.create(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });

        Booking result = bookingService.createBooking(testUser, 1L, testDate, startTime, endTime);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(30.0, result.getTotalPrice()); // 3 hours * 10.0 per hour
        verify(bookingRepository).create(any(Booking.class));
    }

    @Test
    void createBooking_ShouldThrowWhenWorkspaceNotAvailable() throws DataAccessException {
        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(testWorkspace);


        assertThrows(BookingServiceException.class,
                () -> bookingService.createBooking(testUser, 1L, testDate, startTime, endTime));
    }

    @Test
    void getCustomerBookings_ShouldReturnList() throws DataAccessException, BookingServiceException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                testDate.atStartOfDay(), startTime, endTime, BookingStatus.CONFIRMED, 30.0);
        when(bookingRepository.getByCustomer(1L)).thenReturn(List.of(testBooking));

        List<Booking> result = bookingService.getCustomerBookings(1L);

        assertEquals(1, result.size());
        assertEquals(testBooking, result.get(0));
    }

    @Test
    void getCustomerBookings_ShouldThrowWhenCustomerIdNull() {
        assertThrows(BookingServiceException.class,
                () -> bookingService.getCustomerBookings(null));
    }

    @Test
    void cancelBooking_ShouldSuccess() throws DataAccessException, BookingServiceException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                testDate.atStartOfDay(), startTime, endTime, BookingStatus.CONFIRMED, 30.0);
        when(bookingRepository.getById(1L)).thenReturn(testBooking);

        boolean result = bookingService.cancelBooking(1L, 1L);

        assertTrue(result);
        assertEquals(BookingStatus.CANCELLED, testBooking.getStatus());
        verify(bookingRepository).update(testBooking);
    }

    @Test
    void cancelBooking_ShouldReturnFalseWhenAlreadyCancelled() throws DataAccessException, BookingServiceException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                testDate.atStartOfDay(), startTime, endTime, BookingStatus.CANCELLED, 30.0);
        when(bookingRepository.getById(1L)).thenReturn(testBooking);

        boolean result = bookingService.cancelBooking(1L, 1L);

        assertFalse(result);
        verify(bookingRepository, never()).update(any());
    }

    @Test
    void cancelBooking_ShouldThrowWhenUnauthorized() throws DataAccessException {
        Booking testBooking = new Booking(1L, testUser, testWorkspace,
                testDate.atStartOfDay(), startTime, endTime, BookingStatus.CONFIRMED, 30.0);
        when(bookingRepository.getById(1L)).thenReturn(testBooking);

        assertThrows(BookingServiceException.class,
                () -> bookingService.cancelBooking(1L, 2L)); // Different user ID
    }

    @Test
    void getAvailableWorkspaces_ShouldReturnList() throws DataAccessException, BookingServiceException {
        when(workspaceRepository.getAvailableWorkspaces(testDate, startTime, endTime))
                .thenReturn(List.of(testWorkspace));

        List<Workspace> result = bookingService.getAvailableWorkspaces(testDate, startTime, endTime);

        assertEquals(1, result.size());
        assertEquals(testWorkspace, result.get(0));
    }

    @Test
    void getAvailableWorkspaces_ShouldThrowWhenInvalidTime() {
        assertThrows(BookingServiceException.class,
                () -> bookingService.getAvailableWorkspaces(testDate, endTime, startTime)); // End before start
    }

    @Test
    void createBooking_ShouldThrowWhenPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThrows(BookingServiceException.class,
                () -> bookingService.createBooking(testUser, 1L, pastDate, startTime, endTime));
    }

    @Test
    void createBooking_ShouldThrowWhenWorkspaceInactive() throws DataAccessException {
        Workspace inactiveWorkspace = new Workspace(1L, "Workspace 1", "Description", null, 10.0, 4, false, null);
        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(inactiveWorkspace);

        assertThrows(BookingServiceException.class,
                () -> bookingService.createBooking(testUser, 1L, testDate, startTime, endTime));
    }

    @Test
    void createBooking_ShouldThrowWhenDataAccessFails() throws DataAccessException {
        when(workspaceRepository.getWorkspaceById(1L)).thenReturn(testWorkspace);
        when(bookingRepository.create(any(Booking.class))).thenThrow(new DataAccessException("DB error"));

        assertThrows(BookingServiceException.class,
                () -> bookingService.createBooking(testUser, 1L, testDate, startTime, endTime));
    }

    @Test
    void getAvailableWorkspaces_ShouldReturnEmptyList() throws DataAccessException, BookingServiceException {
        when(workspaceRepository.getAvailableWorkspaces(testDate, startTime, endTime))
                .thenReturn(Collections.emptyList());
        List<Workspace> result = bookingService.getAvailableWorkspaces(testDate, startTime, endTime);
        assertTrue(result.isEmpty());
    }
}