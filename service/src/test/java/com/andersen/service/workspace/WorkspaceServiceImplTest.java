package com.andersen.service.workspace;

import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;
import com.andersen.exception.errorCode.ErrorCode;
import com.andersen.repository_JPA.workspace.WorkspaceRepository;
import com.andersen.service.exception.WorkspaceServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private Workspace validWorkspace;
    private Workspace invalidWorkspace;

    @BeforeEach
    void setUp() {
        validWorkspace = new Workspace(1L, "Valid Workspace", "Description",
                null, 10.0, 4, true, null);
        invalidWorkspace = new Workspace(null, "", "", null, -1.0, 0, true, null);
    }

    @Test
    void createWorkspace_ShouldSuccess() throws DataAccessException, WorkspaceServiceException {
        when(workspaceRepository.save(validWorkspace)).thenReturn(validWorkspace);

        Workspace result = workspaceService.createWorkspace(validWorkspace);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(workspaceRepository).save(validWorkspace);
    }

    @Test
    void createWorkspace_ShouldThrowWhenInvalidWorkspace() {
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.createWorkspace(invalidWorkspace));
    }

    @Test
    void createWorkspace_ShouldThrowWhenRepositoryFails() throws DataAccessException {
        when(workspaceRepository.save(validWorkspace))
                .thenThrow(new DataAccessException("DB error", ErrorCode.WS_001));

        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.createWorkspace(validWorkspace));
    }

    @Test
    void getAllWorkspaces_ShouldReturnList() throws DataAccessException, WorkspaceServiceException {
        List<Workspace> workspaces = Arrays.asList(validWorkspace);
        when(workspaceRepository.findAll()).thenReturn(workspaces);

        List<Workspace> result = workspaceService.getAllWorkspaces();

        assertEquals(1, result.size());
        assertEquals(validWorkspace, result.get(0));
    }

    @Test
    void getAllWorkspaces_ShouldReturnEmptyList() throws DataAccessException, WorkspaceServiceException {
        when(workspaceRepository.findAll()).thenReturn(Collections.emptyList());

        List<Workspace> result = workspaceService.getAllWorkspaces();

        assertTrue(result.isEmpty());
    }

    @Test
    void getWorkspaceById_ShouldReturnWorkspace() throws DataAccessException, WorkspaceServiceException {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.ofNullable(validWorkspace));

        Workspace result = workspaceService.getWorkspaceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getWorkspaceById_ShouldThrowWhenIdNull() {
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.getWorkspaceById(null));
    }

    @Test
    void updateWorkspace_ShouldReturnTrue() throws DataAccessException, WorkspaceServiceException {
        when(workspaceRepository.save(validWorkspace));

        boolean result = workspaceService.updateWorkspace(validWorkspace);

        assertTrue(result);
    }

    @Test
    void updateWorkspace_ShouldThrowWhenInvalidWorkspace() {
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.updateWorkspace(invalidWorkspace));
    }



    @Test
    void deleteWorkspace_ShouldThrowWhenIdNull() {
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.deleteWorkspace(null));
    }

    @Test
    void getAvailableWorkspaces_ShouldReturnList() throws DataAccessException, WorkspaceServiceException {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(3);

        when(workspaceRepository.getAvailableWorkspaces(startTime, endTime))
                .thenReturn(Arrays.asList(validWorkspace));

        List<Workspace> result = workspaceService.getAvailableWorkspaces(startTime, endTime);

        assertEquals(1, result.size());
        assertEquals(validWorkspace, result.get(0));
    }

    @Test
    void validateWorkspace_ShouldThrowWhenNull() {
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.createWorkspace(null));
    }

    @Test
    void validateWorkspace_ShouldThrowWhenEmptyName() {
        Workspace workspace = new Workspace(null, "", "Desc", null, 10.0, 4, true, null);
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.createWorkspace(workspace));
    }

    @Test
    void validateWorkspace_ShouldThrowWhenInvalidPrice() {
        Workspace workspace = new Workspace(null, "Name", "Desc", null, -1.0, 4, true, null);
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.createWorkspace(workspace));
    }

    @Test
    void validateWorkspace_ShouldThrowWhenInvalidCapacity() {
        Workspace workspace = new Workspace(null, "Name", "Desc", null, 10.0, 0, true, null);
        assertThrows(WorkspaceServiceException.class,
                () -> workspaceService.createWorkspace(workspace));
    }
}