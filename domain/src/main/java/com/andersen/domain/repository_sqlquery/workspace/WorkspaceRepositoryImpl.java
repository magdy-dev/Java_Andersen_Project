package com.andersen.domain.repository_sqlquery.workspace;

import com.andersen.domain.connection.DatabaseConnection;
import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.domain.entity.workspace.WorkspaceType;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.exception.errorCode.ErrorCodeRepo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    // SQL queries remain the same
    private static final String INSERT_WORKSPACE = "INSERT INTO workspaces (name, description, type, price_per_hour, capacity, is_active) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM workspaces WHERE is_active = TRUE ORDER BY name";
    private static final String SELECT_BY_ID = "SELECT * FROM workspaces WHERE id = ? AND is_active = TRUE";
    private static final String UPDATE_WORKSPACE = "UPDATE workspaces SET name = ?, description = ?, type = ?, price_per_hour = ?, capacity = ? WHERE id = ?";
    private static final String SOFT_DELETE = "UPDATE workspaces SET is_active = FALSE WHERE id = ?";
    private static final String SELECT_AVAILABLE = "SELECT w.* FROM workspaces w WHERE w.is_active = TRUE AND w.id NOT IN (SELECT b.workspace_id FROM bookings b WHERE b.booking_date = ? AND b.status = 'CONFIRMED' AND ((b.start_time < ? AND b.end_time > ?) OR (b.start_time < ? AND b.end_time > ?)))";

    @Override
    public Workspace createWorkspace(Workspace workspace) throws DataAccessException {
        // Implementation remains the same
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(INSERT_WORKSPACE, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, workspace.getName());
            stmt.setString(2, workspace.getDescription());
            stmt.setString(3, workspace.getType().name());
            stmt.setDouble(4, workspace.getPricePerHour());
            stmt.setInt(5, workspace.getCapacity());
            stmt.setBoolean(6, true);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Creating workspace failed - no rows affected", ErrorCodeRepo.WS_001);
            }

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                workspace.setId(rs.getLong(1));
                return workspace;
            } else {
                throw new DataAccessException("Creating workspace failed - no ID obtained", ErrorCodeRepo.WS_001);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating workspace" + e, ErrorCodeRepo.WS_001);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Workspace> getAllWorkspaces() throws DataAccessException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Workspace> workspaces = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL);

            while (rs.next()) {
                Workspace workspace = new Workspace();
                workspace.setId(rs.getLong("id"));
                workspace.setName(rs.getString("name"));
                workspace.setDescription(rs.getString("description"));
                workspace.setType(WorkspaceType.valueOf(rs.getString("type")));
                workspace.setPricePerHour(rs.getDouble("price_per_hour"));
                workspace.setCapacity(rs.getInt("capacity"));
                workspace.setActive(rs.getBoolean("is_active"));
                workspaces.add(workspace);
            }
            return workspaces;
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all workspaces" + e, ErrorCodeRepo.WS_002);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Workspace getWorkspaceById(Long id) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_BY_ID);
            stmt.setLong(1, id);

            rs = stmt.executeQuery();
            if (rs.next()) {
                Workspace workspace = new Workspace();
                workspace.setId(rs.getLong("id"));
                workspace.setName(rs.getString("name"));
                workspace.setDescription(rs.getString("description"));
                workspace.setType(WorkspaceType.valueOf(rs.getString("type")));
                workspace.setPricePerHour(rs.getDouble("price_per_hour"));
                workspace.setCapacity(rs.getInt("capacity"));
                workspace.setActive(rs.getBoolean("is_active"));
                return workspace;
            }
            throw new DataAccessException("Workspace not found with ID: " + id, ErrorCodeRepo.WS_002);
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving workspace: " + id + e, ErrorCodeRepo.WS_002);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Workspace> availableWorkspaces = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_AVAILABLE);

            stmt.setTime(1, Time.valueOf(String.valueOf(endTime)));
            stmt.setTime(2, Time.valueOf(String.valueOf(startTime)));
            stmt.setTime(3, Time.valueOf(String.valueOf(endTime)));
            stmt.setTime(4, Time.valueOf(String.valueOf(startTime)));

            rs = stmt.executeQuery();
            while (rs.next()) {
                Workspace workspace = new Workspace();
                workspace.setId(rs.getLong("id"));
                workspace.setName(rs.getString("name"));
                workspace.setDescription(rs.getString("description"));
                workspace.setType(WorkspaceType.valueOf(rs.getString("type")));
                workspace.setPricePerHour(rs.getDouble("price_per_hour"));
                workspace.setCapacity(rs.getInt("capacity"));
                workspace.setActive(rs.getBoolean("is_active"));
                availableWorkspaces.add(workspace);
            }
            return availableWorkspaces;
        } catch (SQLException e) {
            throw new DataAccessException(
                    String.format("Error checking available workspaces on %s from %s to %s", startTime, endTime) + e, ErrorCodeRepo.WS_003);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // updateWorkspace and deleteWorkspace implementations remain unchanged
    @Override
    public boolean updateWorkspace(Workspace workspace) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_WORKSPACE);

            stmt.setString(1, workspace.getName());
            stmt.setString(2, workspace.getDescription());
            stmt.setString(3, workspace.getType().name());
            stmt.setDouble(4, workspace.getPricePerHour());
            stmt.setInt(5, workspace.getCapacity());
            stmt.setLong(6, workspace.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating workspace: " + workspace.getId() + e, ErrorCodeRepo.WS_004);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public boolean deleteWorkspace(Long id) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SOFT_DELETE);
            stmt.setLong(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting workspace: " + id + e, ErrorCodeRepo.WS_004);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) throws DataAccessException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            throw new DataAccessException("Error closing ResultSet: " + e.getMessage(), ErrorCodeRepo.WS_005);
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            throw new DataAccessException("Error closing Statement: " + e.getMessage(), ErrorCodeRepo.WS_005);
        }
        DatabaseConnection.closeConnection(conn);
    }
}