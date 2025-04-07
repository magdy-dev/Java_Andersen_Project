package com.andersen.service.workspace;

import com.andersen.domain.entity.booking.Booking;
import com.andersen.domain.entity.booking.BookingStatus;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.entity.workspace.WorkspaceType;
import com.andersen.domain.repository.workspace.WorkspaceRepository;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.exception.DataAccessException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private Workspace workspace;

    @BeforeEach
    void setUp() {
        workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("Test Workspace");
        workspace.setDescription("Quiet room with AC");
        workspace.setCapacity(4);
        workspace.setPricePerHour(20.0);
        workspace.setType(WorkspaceType.MEETING_ROOM);
        workspace.setActive(true);
    }

    @Test
    void createWorkspace_shouldSaveAndReturnWorkspace() throws Exception {
        when(workspaceRepository.save(any())).thenReturn(workspace);

        Workspace saved = workspaceService.createWorkspace(workspace);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Workspace");
        verify(workspaceRepository, times(1)).save(workspace);
    }

    @Test
    void getWorkspaceById_shouldReturnWorkspace_whenFoundAndActive() throws Exception {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));

        Optional<Workspace> result = workspaceService.getWorkspaceById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(workspaceRepository).findById(1L);
    }
    @Test
    void isWorkspaceAvailable_shouldReturnFalse_whenBookingConflicts() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(3));

        workspace.setBookings(List.of(booking));

        boolean available = workspaceService.isWorkspaceAvailable(
                workspace,
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(4)
        );

        assertThat(available).isFalse();
    }

    @Test
    void isWorkspaceAvailable_shouldReturnTrue_whenNoConflict() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setStartTime(LocalDateTime.now().plusHours(5));
        booking.setEndTime(LocalDateTime.now().plusHours(6));

        workspace.setBookings(List.of(booking));

        boolean available = workspaceService.isWorkspaceAvailable(
                workspace,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );

        assertThat(available).isTrue();
    }

    @Test
    void getAllWorkspaces_shouldReturnList_whenNoException() throws Exception {
        when(workspaceRepository.findAll()).thenReturn(List.of(workspace));

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();

        assertThat(workspaces).hasSize(1);
        assertThat(workspaces.get(0).getName()).isEqualTo("Test Workspace");
    }

    @Test
    void getAllWorkspaces_shouldThrow_whenRepositoryFails() {
        when(workspaceRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> workspaceService.getAllWorkspaces())
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Failed to retrieve workspaces");
    }
}
