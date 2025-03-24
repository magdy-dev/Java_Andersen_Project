package com.andersen.repository.workspace;

import com.andersen.connection.DatabaseConnection;
import com.andersen.entity.workspace.Workspace;
import com.andersen.entity.workspace.WorkspaceType;
import com.andersen.exception.DataAccessException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private static final String INSERT_WORKSPACE = "INSERT INTO workspaces (name, description, type, price_per_hour, capacity, is_active) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL = "SELECT * FROM workspaces WHERE is_active = TRUE ORDER BY name";

    private static final String SELECT_BY_ID = "SELECT * FROM workspaces WHERE id = ? AND is_active = TRUE";

    private static final String UPDATE_WORKSPACE = "UPDATE workspaces SET name = ?, description = ?, type = ?, " +
            "price_per_hour = ?, capacity = ? WHERE id = ?";

    private static final String SOFT_DELETE = "UPDATE workspaces SET is_active = FALSE WHERE id = ?";

    private static final String SELECT_AVAILABLE = "SELECT w.* FROM workspaces w " +
            "WHERE w.is_active = TRUE AND w.id NOT IN (" +
            "   SELECT b.workspace_id FROM bookings b " +
            "   WHERE b.booking_date = ? " +
            "   AND b.status = 'CONFIRMED' " +
            "   AND ((b.start_time < ? AND b.end_time > ?) " +
            "   OR (b.start_time < ? AND b.end_time > ?)))";

    @Override
    public Workspace createWorkspace(Workspace workspace) throws DataAccessException {
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
            stmt.setBoolean(6, true); // is_active

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Creating workspace failed - no rows affected");
            }

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                workspace.setId(rs.getLong(1));
                return workspace;
            } else {
                throw new DataAccessException("Creating workspace failed - no ID obtained");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating workspace"+ e);
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
                workspaces.add(mapRowToWorkspace(rs));
            }
            return workspaces;
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all workspaces"+e);
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
                return mapRowToWorkspace(rs);
            }
            throw new DataAccessException("Workspace not found with ID: " + id);
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving workspace: " + id+e);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

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
            throw new DataAccessException("Error updating workspace: " + workspace.getId()+e);
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
            throw new DataAccessException("Error deleting workspace: " + id+e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public List<Workspace> getAvailableWorkspaces(LocalDate date, LocalTime startTime, LocalTime endTime) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Workspace> availableWorkspaces = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_AVAILABLE);

            stmt.setDate(1, Date.valueOf(date));
            stmt.setTime(2, Time.valueOf(endTime));
            stmt.setTime(3, Time.valueOf(startTime));
            stmt.setTime(4, Time.valueOf(endTime));
            stmt.setTime(5, Time.valueOf(startTime));

            rs = stmt.executeQuery();
            while (rs.next()) {
                availableWorkspaces.add(mapRowToWorkspace(rs));
            }
            return availableWorkspaces;
        } catch (SQLException e) {
            throw new DataAccessException(
                    String.format("Error checking available workspaces on %s from %s to %s",
                            date, startTime, endTime)+e);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private Workspace mapRowToWorkspace(ResultSet rs) throws SQLException {
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

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing Statement: " + e.getMessage());
        }
        DatabaseConnection.closeConnection(conn);
    }
}