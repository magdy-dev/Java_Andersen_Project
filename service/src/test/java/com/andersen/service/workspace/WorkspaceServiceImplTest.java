package com.andersen.service.workspace;

import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Availability;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.WorkspaceNotFoundException;
import com.andersen.repository.workspace.WorkspaceRepositoryEntityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceServiceImplTest {
    private WorkspaceServiceImpl workspaceService; // Service under test
    private WorkspaceRepositoryEntityImpl workspaceRepository; // Mock for the repository
    private Workspace workspace1; // First workspace for testing
    private Workspace workspace2; // Second workspace for testing
    private Customer customer; // Customer entity for booking
    private Booking booking; // Booking entity for testing
    private Availability availability1; // First availability for testing
    private Availability availability2; // Second availability for testing

    // Constants for workspace IDs
    private static final Long WORKSPACE1_ID = 1L;
    private static final Long WORKSPACE2_ID = 2L;

    // Setup method to initialize the test environment
    @BeforeEach
    void setUp() throws WorkspaceNotFoundException {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        workspaceRepository = mock(WorkspaceRepositoryEntityImpl.class); // Create a mock repository
        workspaceService = new WorkspaceServiceImpl(workspaceRepository); // Initialize the service with the mock

        // Create a customer for testing
        customer = new Customer("john_doe", "securePassword123");

        // Create workspaces for testing
        workspace1 = new Workspace(WORKSPACE1_ID, "Cozy Office", "A comfortable workspace with natural light.");
        workspace2 = new Workspace(WORKSPACE2_ID, "Modern Desk", "A sleek workspace with modern amenities.");

        // Create a booking for the first workspace
        booking = new Booking(1L, customer, workspace1, LocalTime.of(9, 0), LocalTime.of(17, 0));
        // Create availability instances for testing
        availability1 = new Availability(1L, LocalDate.now(), LocalTime.of(14, 0), 20, 15);
        availability2 = new Availability(2L, LocalDate.now(), LocalTime.of(13, 0), 30, 16);

        // Mock repository responses
        when(workspaceRepository.getAllWorkspaces()).thenReturn(List.of(workspace1, workspace2));
        when(workspaceRepository.getWorkspaceById(WORKSPACE1_ID)).thenReturn(Optional.of(workspace1));
        when(workspaceRepository.getWorkspaceById(WORKSPACE2_ID)).thenReturn(Optional.of(workspace2));
    }

    // Valid Scenario: Adding and retrieving workspaces
    @Test
    void addWorkspace() {
        // Add booking and availabilities to workspace1
        workspace1.addBooking(booking);
        workspace1.addAvailability(availability1);
        workspace1.addAvailability(availability2);

        // Assertions to verify the state of workspace1
        assertEquals(1, workspace1.getBookings().size(), "Workspace should have 1 booking.");
        assertEquals(2, workspace1.getAvailabilities().size(), "Workspace should have 2 availabilities.");
    }

    // Test to retrieve all workspaces
    @Test
    void getAllWorkspaces() throws WorkspaceNotFoundException {
        List<Workspace> allWorkspaces = workspaceService.getAllWorkspaces(); // Call the service method

        // Assertions to verify the retrieved workspaces
        assertEquals(2, allWorkspaces.size(), "There should be 2 workspaces.");
        assertTrue(allWorkspaces.contains(workspace1), "Workspace1 should be present.");
        assertTrue(allWorkspaces.contains(workspace2), "Workspace2 should be present.");
    }

    // Invalid Scenario: Attempting to remove a non-existent workspace
    @Test
    void removeWorkspace_NotFound() {
        long nonExistentId = 99999; // ID that does not exist

        // Mock the repository to return empty for the non-existent ID
        when(workspaceRepository.getWorkspaceById(nonExistentId)).thenReturn(Optional.empty());

        // Assert that removing a non-existent workspace throws an exception
        assertThrows(WorkspaceNotFoundException.class, () -> {
            workspaceService.removeWorkspace(nonExistentId);
        });
    }

    // Invalid Scenario: Removing an existing workspace with no bookings
    // Invalid Scenario: Removing an existing workspace with no bookings
    @Test
    void removeExistingWorkspace_NoBookings() throws WorkspaceNotFoundException {
        // Remove the workspace
        workspaceService.removeWorkspace(workspace1.getId());
    }
}