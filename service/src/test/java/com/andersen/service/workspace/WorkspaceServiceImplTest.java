package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.entity.workspace.WorkspaceType;
import com.andersen.exception.DataAccessException;
import com.andersen.repository.workspace.WorkspaceRepository;
import com.andersen.service.excption.WorkspaceServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private Workspace testWorkspace;
    private final Long testWorkspaceId = 1L;
    private final LocalDate testDate = LocalDate.now().plusDays(1);
    private final LocalTime testStartTime = LocalTime.of(9, 0);
    private final LocalTime testEndTime = LocalTime.of(17, 0);

    @BeforeEach
    void setUp() {
        testWorkspace = new Workspace();
        testWorkspace.setId(testWorkspaceId);
        testWorkspace.setName("Test Workspace");
        testWorkspace.setDescription("Test Description");
        testWorkspace.setType(WorkspaceType.valueOf("OPEN_SPACE"));
        testWorkspace.setPricePerHour(10.0);
        testWorkspace.setCapacity(4);
        testWorkspace.setActive(true);
    }

    @Test
    void createWorkspace_Successful() throws Exception {
        when(workspaceRepository.createWorkspace(any(Workspace.class))).thenReturn(testWorkspace);

        Workspace createdWorkspace = workspaceService.createWorkspace(testWorkspace);

        assertNotNull(createdWorkspace);
        assertEquals(testWorkspaceId, createdWorkspace.getId());
        verify(workspaceRepository, times(1)).createWorkspace(any(Workspace.class));
    }

    @Test
    void createWorkspace_InvalidWorkspace_ThrowsException() {
        // Null workspace
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.createWorkspace(null));

        // Empty name
        testWorkspace.setName("");
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.createWorkspace(testWorkspace));

        // Invalid price
        testWorkspace.setName("Valid Name");
        testWorkspace.setPricePerHour(0);
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.createWorkspace(testWorkspace));

        // Invalid capacity
        testWorkspace.setPricePerHour(10.0);
        testWorkspace.setCapacity(0);
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.createWorkspace(testWorkspace));
    }

    @Test
    void createWorkspace_RepositoryError_ThrowsException() throws Exception {
        when(workspaceRepository.createWorkspace(any(Workspace.class)))
                .thenThrow(new DataAccessException("DB error"));

        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.createWorkspace(testWorkspace));
    }

    @Test
    void getAllWorkspaces_Successful() throws Exception {
        when(workspaceRepository.getAllWorkspaces())
                .thenReturn(Collections.singletonList(testWorkspace));

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();

        assertNotNull(workspaces);
        assertEquals(1, workspaces.size());
        assertEquals(testWorkspaceId, workspaces.get(0).getId());
        verify(workspaceRepository, times(1)).getAllWorkspaces();
    }

    @Test
    void getAllWorkspaces_RepositoryError_ThrowsException() throws Exception {
        when(workspaceRepository.getAllWorkspaces())
                .thenThrow(new DataAccessException("DB error"));

        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getAllWorkspaces());
    }

    @Test
    void getWorkspaceById_Successful() throws Exception {
        when(workspaceRepository.getWorkspaceById(testWorkspaceId))
                .thenReturn(testWorkspace);

        Workspace foundWorkspace = workspaceService.getWorkspaceById(testWorkspaceId);

        assertNotNull(foundWorkspace);
        assertEquals(testWorkspaceId, foundWorkspace.getId());
        verify(workspaceRepository, times(1)).getWorkspaceById(testWorkspaceId);
    }

    @Test
    void getWorkspaceById_NotFound_ReturnsNull() throws Exception {
        when(workspaceRepository.getWorkspaceById(testWorkspaceId))
                .thenReturn(null);

        Workspace foundWorkspace = workspaceService.getWorkspaceById(testWorkspaceId);

        assertNull(foundWorkspace);
    }

    @Test
    void getWorkspaceById_NullId_ThrowsException() {
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getWorkspaceById(null));
    }

    @Test
    void getWorkspaceById_RepositoryError_ThrowsException() throws Exception {
        when(workspaceRepository.getWorkspaceById(testWorkspaceId))
                .thenThrow(new DataAccessException("DB error"));

        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getWorkspaceById(testWorkspaceId));
    }

    @Test
    void updateWorkspace_Successful() throws Exception {
        when(workspaceRepository.updateWorkspace(any(Workspace.class))).thenReturn(true);

        boolean result = workspaceService.updateWorkspace(testWorkspace);

        assertTrue(result);
        verify(workspaceRepository, times(1)).updateWorkspace(any(Workspace.class));
    }

    @Test
    void updateWorkspace_InvalidWorkspace_ThrowsException() {
        // Null workspace
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.updateWorkspace(null));

        // Empty name
        testWorkspace.setName("");
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.updateWorkspace(testWorkspace));
    }

    @Test
    void updateWorkspace_RepositoryError_ThrowsException() throws Exception {
        when(workspaceRepository.updateWorkspace(any(Workspace.class)))
                .thenThrow(new DataAccessException("DB error"));

        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.updateWorkspace(testWorkspace));
    }

    @Test
    void deleteWorkspace_Successful() throws Exception {
        when(workspaceRepository.deleteWorkspace(testWorkspaceId)).thenReturn(true);

        boolean result = workspaceService.deleteWorkspace(testWorkspaceId);

        assertTrue(result);
        verify(workspaceRepository, times(1)).deleteWorkspace(testWorkspaceId);
    }

    @Test
    void deleteWorkspace_NullId_ThrowsException() {
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.deleteWorkspace(null));
    }

    @Test
    void deleteWorkspace_RepositoryError_ThrowsException() throws Exception {
        when(workspaceRepository.deleteWorkspace(testWorkspaceId))
                .thenThrow(new DataAccessException("DB error"));

        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.deleteWorkspace(testWorkspaceId));
    }

    @Test
    void getAvailableWorkspaces_Successful() throws Exception {
        when(workspaceRepository.getAvailableWorkspaces(testDate, testStartTime, testEndTime))
                .thenReturn(Collections.singletonList(testWorkspace));

        List<Workspace> availableWorkspaces = workspaceService.getAvailableWorkspaces(
                testDate, testStartTime, testEndTime);

        assertNotNull(availableWorkspaces);
        assertEquals(1, availableWorkspaces.size());
        assertEquals(testWorkspaceId, availableWorkspaces.get(0).getId());
        verify(workspaceRepository, times(1)).getAvailableWorkspaces(
                testDate, testStartTime, testEndTime);
    }

    @Test
    void getAvailableWorkspaces_InvalidTimeParameters_ThrowsException() {
        // Null date
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getAvailableWorkspaces(null, testStartTime, testEndTime));

        // Null start time
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getAvailableWorkspaces(testDate, null, testEndTime));

        // Null end time
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getAvailableWorkspaces(testDate, testStartTime, null));

        // Past date
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getAvailableWorkspaces(LocalDate.now().minusDays(1),
                        testStartTime, testEndTime));

        // Invalid time range
        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getAvailableWorkspaces(testDate,
                        testEndTime, testStartTime));
    }

    @Test
    void getAvailableWorkspaces_RepositoryError_ThrowsException() throws Exception {
        when(workspaceRepository.getAvailableWorkspaces(testDate, testStartTime, testEndTime))
                .thenThrow(new DataAccessException("DB error"));

        assertThrows(WorkspaceServiceException.class, () ->
                workspaceService.getAvailableWorkspaces(testDate, testStartTime, testEndTime));
    }
}