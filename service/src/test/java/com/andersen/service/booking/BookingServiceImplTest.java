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
import static org.mockito.ArgumentMatchers.anyLong;
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
    private Booking testBooking;
    private final Long testUserId = 1L;
    private final Long testWorkspaceId = 1L;
    private final Long testBookingId = 1L;
    private final LocalDate testDate = LocalDate.now().plusDays(1);
    private final LocalTime testStartTime = LocalTime.of(9, 0);
    private final LocalTime testEndTime = LocalTime.of(12, 0);

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setUsername("testuser");

        testWorkspace = new Workspace();
        testWorkspace.setId(testWorkspaceId);
        testWorkspace.setName("Test Workspace");
        testWorkspace.setPricePerHour(10.0);
        testWorkspace.setActive(true);

        testBooking = new Booking();
        testBooking.setId(testBookingId);
        testBooking.setCustomer(testUser);
        testBooking.setWorkspace(testWorkspace);
        testBooking.setBookingDate(testDate);
        testBooking.setStartTime(testStartTime);
        testBooking.setEndTime(testEndTime);
        testBooking.setStatus(BookingStatus.CONFIRMED);
        testBooking.setTotalPrice(30.0);
    }

    @Test
    void createBooking_Successful() throws Exception {
        when(workspaceRepository.getWorkspaceById(testWorkspaceId)).thenReturn(testWorkspace);
        when(bookingRepository.isWorkspaceAvailable(anyLong(), any(), any(), any())).thenReturn(true);
        when(bookingRepository.createBooking(any(Booking.class))).thenReturn(testBooking);

        Booking createdBooking = bookingService.createBooking(
                testUser, testWorkspaceId, testDate, testStartTime, testEndTime);

        assertNotNull(createdBooking);
        assertEquals(testBookingId, createdBooking.getId());
        verify(workspaceRepository, times(1)).getWorkspaceById(testWorkspaceId);
        verify(bookingRepository, times(1)).isWorkspaceAvailable(
                testWorkspaceId, testDate, testStartTime, testEndTime);
        verify(bookingRepository, times(1)).createBooking(any(Booking.class));
    }

    @Test
    void createBooking_InvalidParameters_ThrowsException() {
        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(null, testWorkspaceId, testDate, testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(testUser, null, testDate, testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(testUser, testWorkspaceId, null, testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(testUser, testWorkspaceId, testDate, null, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(testUser, testWorkspaceId, testDate, testStartTime, null));

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(testUser, testWorkspaceId, LocalDate.now().minusDays(1),
                        testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(testUser, testWorkspaceId, testDate,
                        testEndTime, testStartTime));
    }

    @Test
    void createBooking_WorkspaceNotFound_ThrowsException() throws Exception {
        when(workspaceRepository.getWorkspaceById(testWorkspaceId)).thenReturn(null);

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(
                        testUser, testWorkspaceId, testDate, testStartTime, testEndTime));
    }

    @Test
    void createBooking_WorkspaceNotAvailable_ThrowsException() throws Exception {
        when(workspaceRepository.getWorkspaceById(testWorkspaceId)).thenReturn(testWorkspace);
        when(bookingRepository.isWorkspaceAvailable(anyLong(), any(), any(), any())).thenReturn(false);

        assertThrows(BookingServiceException.class, () ->
                bookingService.createBooking(
                        testUser, testWorkspaceId, testDate, testStartTime, testEndTime));
    }

    @Test
    void getCustomerBookings_Successful() throws Exception {
        when(bookingRepository.getBookingsByCustomer(testUserId))
                .thenReturn(Collections.singletonList(testBooking));

        List<Booking> bookings = bookingService.getCustomerBookings(testUserId);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(testBookingId, bookings.get(0).getId());
        verify(bookingRepository, times(1)).getBookingsByCustomer(testUserId);
    }

    @Test
    void getCustomerBookings_InvalidCustomerId_ThrowsException() {
        assertThrows(BookingServiceException.class, () -> bookingService.getCustomerBookings(null));
    }

    @Test
    void getCustomerBookings_RepositoryError_ThrowsException() throws Exception {
        when(bookingRepository.getBookingsByCustomer(testUserId))
                .thenThrow(new DataAccessException("DB error"));

        assertThrows(BookingServiceException.class, () ->
                bookingService.getCustomerBookings(testUserId));
    }

    @Test
    void cancelBooking_Successful() throws Exception {
        when(bookingRepository.getBookingById(testBookingId)).thenReturn(testBooking);
        when(bookingRepository.cancelBooking(testBookingId)).thenReturn(true);

        boolean result = bookingService.cancelBooking(testBookingId, testUserId);

        assertTrue(result);
        verify(bookingRepository, times(1)).getBookingById(testBookingId);
        verify(bookingRepository, times(1)).cancelBooking(testBookingId);
    }

    @Test
    void cancelBooking_AlreadyCancelled_ReturnsFalse() throws Exception {
        testBooking.setStatus(BookingStatus.CANCELLED);
        when(bookingRepository.getBookingById(testBookingId)).thenReturn(testBooking);

        boolean result = bookingService.cancelBooking(testBookingId, testUserId);

        assertFalse(result);
        verify(bookingRepository, never()).cancelBooking(anyLong());
    }

    @Test
    void cancelBooking_UnauthorizedUser_ThrowsException() throws Exception {
        when(bookingRepository.getBookingById(testBookingId)).thenReturn(testBooking);

        assertThrows(BookingServiceException.class, () ->
                bookingService.cancelBooking(testBookingId, 999L));
    }

    @Test
    void cancelBooking_InvalidParameters_ThrowsException() {
        assertThrows(BookingServiceException.class, () ->
                bookingService.cancelBooking(null, testUserId));

        assertThrows(BookingServiceException.class, () ->
                bookingService.cancelBooking(testBookingId, null));
    }

    @Test
    void isWorkspaceAvailable_Successful() throws Exception {
        when(bookingRepository.isWorkspaceAvailable(
                testWorkspaceId, testDate, testStartTime, testEndTime)).thenReturn(true);

        boolean available = bookingService.isWorkspaceAvailable(
                testWorkspaceId, testDate, testStartTime, testEndTime);

        assertTrue(available);
        verify(bookingRepository, times(1)).isWorkspaceAvailable(
                testWorkspaceId, testDate, testStartTime, testEndTime);
    }

    @Test
    void isWorkspaceAvailable_InvalidParameters_ThrowsException() {
        assertThrows(BookingServiceException.class, () ->
                bookingService.isWorkspaceAvailable(null, testDate, testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.isWorkspaceAvailable(testWorkspaceId, null, testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.isWorkspaceAvailable(testWorkspaceId, testDate, null, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.isWorkspaceAvailable(testWorkspaceId, testDate, testStartTime, null));

        assertThrows(BookingServiceException.class, () ->
                bookingService.isWorkspaceAvailable(testWorkspaceId, LocalDate.now().minusDays(1),
                        testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.isWorkspaceAvailable(testWorkspaceId, testDate,
                        testEndTime, testStartTime));
    }

    @Test
    void getAvailableWorkspaces_Successful() throws Exception {
        when(workspaceRepository.getAvailableWorkspaces(testDate, testStartTime, testEndTime))
                .thenReturn(Collections.singletonList(testWorkspace));

        List<Workspace> workspaces = bookingService.getAvailableWorkspaces(
                testDate, testStartTime, testEndTime);

        assertNotNull(workspaces);
        assertEquals(1, workspaces.size());
        assertEquals(testWorkspaceId, workspaces.get(0).getId());
        verify(workspaceRepository, times(1)).getAvailableWorkspaces(
                testDate, testStartTime, testEndTime);
    }

    @Test
    void getAvailableWorkspaces_InvalidParameters_ThrowsException() {
        assertThrows(BookingServiceException.class, () ->
                bookingService.getAvailableWorkspaces(null, testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.getAvailableWorkspaces(testDate, null, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.getAvailableWorkspaces(testDate, testStartTime, null));

        assertThrows(BookingServiceException.class, () ->
                bookingService.getAvailableWorkspaces(LocalDate.now().minusDays(1),
                        testStartTime, testEndTime));

        assertThrows(BookingServiceException.class, () ->
                bookingService.getAvailableWorkspaces(testDate, testEndTime, testStartTime));
    }

    @Test
    void calculateTotalPrice_CorrectCalculation() {
        double price = bookingService.calculateTotalPrice(testWorkspace,
                LocalTime.of(10, 0), LocalTime.of(14, 30)); // 4.5 hours

        assertEquals(45.0, price); // 4.5 * 10.0
    }
}