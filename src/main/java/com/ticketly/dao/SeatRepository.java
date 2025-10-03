package com.ticketly.dao;

import com.ticketly.model.Seat;
import com.ticketly.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatRepository {
    private static final Logger logger = LoggerFactory.getLogger(SeatRepository.class);

    // Fetch all seats of a theater (or show if you join bookings)
    public List<Seat> findByTheaterId(long theaterId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE theater_id = ? ORDER BY row_name, seat_number";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, theaterId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat();
                    seat.setId(rs.getLong("id"));
                    seat.setTheaterId(rs.getLong("theater_id"));
                    seat.setSeatRow(rs.getString("row_name"));
                    seat.setSeatNumber(rs.getInt("seat_number"));
                    seat.setSeatType(rs.getString("seat_type"));
                    seat.setActive(rs.getBoolean("is_active"));
                    seat.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    seats.add(seat);
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching seats by theater id", e);
        }

        return seats;
    }

    // Check if seat is already booked for the show
    public boolean isSeatBooked(long showId, long seatId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE show_id = ? AND seat_id = ? AND status = 'CONFIRMED'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, showId);
            stmt.setLong(2, seatId);
            logger.info("Checking if seat is booked: showId={}, seatId={}", showId, seatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("Seat booking count: {}", count);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking if seat is booked: showId={}, seatId={}", showId, seatId, e);
        }
        return false;
    }

    // Booking a seat: simplified version
    public boolean bookSeat(long seatId, long userId, long showId, double price) {
        if (isSeatBooked(showId, seatId)) {
            logger.warn("Attempt to book already booked seat: seatId={}, showId={}", seatId, showId);
            return false; // Seat already booked
        }

        String insertBookingSql = "INSERT INTO bookings (user_id, show_id, seat_id, total_amount, payment_status, status) " +
                                  "VALUES (?, ?, ?, ?, 'PAID', 'CONFIRMED')";

        logger.info("Booking seat: seatId={}, userId={}, showId={}, price={}", seatId, userId, showId, price);

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertBookingSql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, showId);
            stmt.setLong(3, seatId);
            stmt.setDouble(4, price);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                logger.warn("No rows affected when booking seat: seatId={}, userId={}, showId={}", seatId, userId, showId);
                return false;
            }

            logger.info("Seat booked successfully: seatId={}, userId={}, showId={}", seatId, userId, showId);
            return true;

        } catch (SQLException e) {
            logger.error("Error booking seat: seatId={}, userId={}, showId={}", seatId, userId, showId, e);
        }

        return false;
    }
}
