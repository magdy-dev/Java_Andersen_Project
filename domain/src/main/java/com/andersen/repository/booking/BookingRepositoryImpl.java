package com.andersen.repository.booking;

import com.andersen.connection.DatabaseConnection;
import com.andersen.entity.booking.Booking;
import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl implements BookingRepository {
    private static final String CREATE_SQL = "INSERT INTO bookings " +
            "(customer_id, workspace_id, start_time, end_time, status, total_price) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_BY_ID_SQL = "SELECT * FROM bookings WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE bookings SET " +
            "customer_id = ?, workspace_id = ?, " +
            "start_time = ?, end_time = ?, status = ?, total_price = ? " +
            "WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bookings WHERE id = ?";
    private static final String GET_BY_CUSTOMER_SQL = "SELECT * FROM bookings WHERE customer_id = ?";

    @Override
    public Booking create(Booking booking) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);

            setBookingParameters(stmt, booking);
            stmt.setString(5, booking.getStatus().name());
            stmt.setDouble(6, booking.getTotalPrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Creating booking failed, no rows affected.");
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                booking.setId(generatedKeys.getLong(1));
                return booking;
            } else {
                throw new DataAccessException("Creating booking failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create booking: " + e.getMessage() + e);
        } finally {
            closeResources(conn, stmt, generatedKeys);
        }
    }

    @Override
    public Booking getById(Long id) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_ID_SQL);
            stmt.setLong(1, id);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
            throw new DataAccessException("Booking not found with id: " + id);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get booking by id: " + id + e);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Booking update(Booking booking) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);

            setBookingParameters(stmt, booking);
            stmt.setString(5, booking.getStatus().name());
            stmt.setDouble(6, booking.getTotalPrice());
            stmt.setLong(7, booking.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Updating booking failed, no rows affected.");
            }
            return booking;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update booking: " + booking.getId() + e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public void delete(Long id) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Deleting booking failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete booking: " + id + e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public List<Booking> getByCustomer(Long customerId) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_CUSTOMER_SQL);
            stmt.setLong(1, customerId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get bookings for customer: " + customerId + e);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private void setBookingParameters(PreparedStatement stmt, Booking booking) throws SQLException {
        stmt.setLong(1, booking.getCustomer().getId());
        stmt.setLong(2, booking.getWorkspace().getId());
        stmt.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
        stmt.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        User customer = new User();
        customer.setId(rs.getLong("customer_id"));

        Workspace workspace = new Workspace();
        workspace.setId(rs.getLong("workspace_id"));

        return new Booking(
                rs.getLong("id"),
                customer,
                workspace,
                rs.getTimestamp("start_time").toLocalDateTime(),
                rs.getTimestamp("end_time").toLocalDateTime(),
                BookingStatus.valueOf(rs.getString("status")),
                rs.getDouble("total_price")
        );
    }

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) throws DataAccessException {
        try {
            if (rs != null) {rs.close();}
            if (stmt != null) {stmt.close();}
            if (conn != null) {conn.close();}
        } catch (SQLException e) {

            throw new DataAccessException("Error closing database resources: " + e.getMessage());
        }
    }
}