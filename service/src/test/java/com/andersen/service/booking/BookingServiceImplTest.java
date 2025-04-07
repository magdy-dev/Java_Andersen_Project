package com.andersen.service.booking;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository.booking.BookingRepository;
import com.andersen.domain.repository.workspace.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Workspace workspace;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        workspace = new Workspace();
        workspace.setId(1L);

        start = LocalDateTime.now().plusHours(1);
        end = start.plusHours(2);
    }

    @Test
    void createBooking_shouldReturnSavedBooking() throws Exception {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        Booking savedBooking = new Booking();
        savedBooking.setCustomer(user);
        savedBooking.setWorkspace(workspace);
        savedBooking.setStartTime(start);
        savedBooking.setEndTime(end);
        savedBooking.setStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        Booking result = bookingService.createBooking(user, 1L, start, end);

        assertThat(result).isNotNull();
        assertThat(result.getCustomer()).isEqualTo(user);
        assertThat(result.getWorkspace()).isEqualTo(workspace);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void getCustomerBookings_shouldReturnListOfBookings() throws Exception {
        Booking booking = new Booking();
        booking.setCustomer(user);

        when(bookingRepository.getByCustomerId(1L)).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getCustomerBookings(1L);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0).getCustomer()).isEqualTo(user);
    }

    @Test
    void cancelBooking_shouldCancelBooking() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomer(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.getById(1L)).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        boolean result = bookingService.cancelBooking(1L, 1L);

        assertThat(result).isTrue();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void getAvailableWorkspaces_shouldReturnAvailableList() throws Exception {
        when(workspaceRepository.getAvailableWorkspaces(start, end)).thenReturn(List.of(workspace));

        List<Workspace> available = bookingService.getAvailableWorkspaces(start, end);

        assertThat(available).containsExactly(workspace);
    }

    @Test
    void getAllBookings_shouldReturnAllBookings() throws DataAccessException {
        Booking booking = new Booking();
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> result = bookingService.getAllBookings();

        assertThat(result).hasSize(1);
    }
}
