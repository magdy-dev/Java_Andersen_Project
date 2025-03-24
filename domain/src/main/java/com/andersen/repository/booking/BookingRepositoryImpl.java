package com.andersen.repository.booking;

import com.andersen.connection.DatabaseConnection;
import com.andersen.entity.booking.Booking;
import com.andersen.entity.booking.BookingStatus;
import com.andersen.entity.role.User;
import com.andersen.entity.workspace.Workspace;
import com.andersen.exception.DataAccessException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl implements BookingRepository {

    private static final String CREATE_SQL = "INSERT INTO bookings " +
            "(customer_id, workspace_id, booking_date, start_time, end_time, status, total_price) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_CUSTOMER_SQL = "SELECT * FROM bookings WHERE customer_id = ?";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM bookings WHERE id = ?";
    private static final String CANCEL_SQL = "UPDATE bookings SET status = ? WHERE id = ?";
    private static final String CHECK_AVAILABILITY_SQL =
            "SELECT COUNT(*) FROM bookings WHERE " +
                    "workspace_id = ? AND booking_date = ? AND status = 'CONFIRMED' " +
                    "AND ((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?)";

    @Override
    public Booking createBooking(Booking booking) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);

            setBookingParameters(stmt, booking);
            stmt.setString(6, booking.getStatus().name());
            stmt.setDouble(7, booking.getTotalPrice());

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
            throw new DataAccessException("Failed to create booking: " + e.getMessage()+e);
        } finally {
            closeResources(conn, stmt, generatedKeys);
        }
    }

    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Booking> bookings = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(FIND_BY_CUSTOMER_SQL);
            stmt.setLong(1, customerId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get bookings for customer: " + customerId+e);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public Booking getBookingById(Long id) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(FIND_BY_ID_SQL);
            stmt.setLong(1, id);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
            throw new DataAccessException("Booking not found with id: " + id);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get booking by id: " + id+e);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    @Override
    public boolean cancelBooking(Long bookingId) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(CANCEL_SQL);
            stmt.setString(1, BookingStatus.CANCELLED.name());
            stmt.setLong(2, bookingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to cancel booking: " + bookingId+e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public boolean isWorkspaceAvailable(Long workspaceId, LocalDate date,
                                        LocalTime startTime, LocalTime endTime) throws DataAccessException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(CHECK_AVAILABILITY_SQL);

            stmt.setLong(1, workspaceId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(endTime));
            stmt.setTime(4, Time.valueOf(startTime));
            stmt.setTime(5, Time.valueOf(endTime));
            stmt.setTime(6, Time.valueOf(startTime));

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException(
                    String.format("Failed to check availability for workspace %d on %s from %s to %s",
                            workspaceId, date, startTime, endTime)+e);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private void setBookingParameters(PreparedStatement stmt, Booking booking) throws SQLException {
        stmt.setLong(1, booking.getCustomer().getId());
        stmt.setLong(2, booking.getWorkspace().getId());
        stmt.setDate(3, Date.valueOf(booking.getBookingDate()));
        stmt.setTime(4, Time.valueOf(booking.getStartTime()));
        stmt.setTime(5, Time.valueOf(booking.getEndTime()));
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
                rs.getDate("booking_date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                BookingStatus.valueOf(rs.getString("status")),
                rs.getDouble("total_price")
        );
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