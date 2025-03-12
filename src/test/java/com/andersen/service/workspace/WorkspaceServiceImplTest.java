package com.andersen.service.workspace;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Availability;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.repository.workspace.WorkspaceRepositoryEntityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceServiceImplTest {
    private WorkspaceServiceImpl workspaceService;
    private WorkspaceRepositoryEntityImpl workspaceRepository; // Mock for the repository
    private Workspace workspace1;
    private Workspace workspace2;
    private Customer customer;
    private Booking booking;
    private Availability availability1;
    private Availability availability2;



    @BeforeEach
    void setUp() throws WorkspaceNotFoundException {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        workspaceRepository = Mockito.mock(WorkspaceRepositoryEntityImpl.class);
        workspaceService = new WorkspaceServiceImpl(workspaceRepository);

        // Set the mocked logger in the service


        customer = new Customer("john_doe", "securePassword123");

        workspace1 = new Workspace(1, "Cozy Office", "A comfortable workspace with natural light.");
        workspace2 = new Workspace(2, "Modern Desk", "A sleek workspace with modern amenities.");

        booking = new Booking(1, customer, workspace1, LocalTime.of(9, 0), LocalTime.of(17, 0));
        availability1 = new Availability(1, LocalDate.now(), LocalTime.of(14, 0), 20, 15);
        availability2 = new Availability(2, LocalDate.now(), LocalTime.of(13, 0), 30, 16);

        when(workspaceRepository.getAllWorkspaces()).thenReturn(List.of(workspace1, workspace2));

        // Add the workspaces to the service for testing
        workspaceService.addWorkspace(workspace1);
        workspaceService.addWorkspace(workspace2);
    }

    @Test
    void addWorkspace() {
        workspace1.addBooking(booking);
        workspace1.addAvailability(availability1);
        workspace1.addAvailability(availability2);

        assertEquals(1, workspace1.getBookings().size(), "Workspace should have 1 booking.");
        assertEquals(2, workspace1.getAvailabilities().size(), "Workspace should have 2 availabilities.");
    }



    @Test
    void getAllWorkspaces() {
        List<Workspace> allWorkspaces = workspaceService.getAllWorkspaces();

        assertEquals(2, allWorkspaces.size(), "There should be 2 workspaces.");
        assertTrue(allWorkspaces.contains(workspace1), "Workspace1 should be present.");
        assertTrue(allWorkspaces.contains(workspace2), "Workspace2 should be present.");
    }


    @Test
    void removeWorkspace_NotFound() {
        long nonExistentId = 99999; // An ID that does not exist

        // Mock the behavior of the repository to return empty when looking for a non-existent workspace
        when(workspaceRepository.getWorkspaceById(nonExistentId)).thenReturn(Optional.empty());

        // Verify that the exception is thrown when trying to remove a non-existent workspace
        assertThrows(WorkspaceNotFoundException.class, () -> {
            workspaceService.removeWorkspace(nonExistentId);
        });
    }
}