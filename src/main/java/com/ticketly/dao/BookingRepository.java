package com.ticketly.dao;

import com.ticketly.model.Booking;
import com.ticketly.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookingRepository.class);

    public List<Booking> findByUserId(long userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booking_date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setId(rs.getLong("id"));
                    booking.setUserId(rs.getLong("user_id"));
                    booking.setShowId(rs.getLong("show_id"));
                    booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                    booking.setTotalAmount(rs.getBigDecimal("total_amount"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPaymentStatus(rs.getString("payment_status"));
                    bookings.add(booking);
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching bookings by user id", e);
        }

        return bookings;
    }

    public Booking findById(long id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setId(rs.getLong("id"));
                    booking.setUserId(rs.getLong("user_id"));
                    booking.setShowId(rs.getLong("show_id"));
                    booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                    booking.setTotalAmount(rs.getBigDecimal("total_amount"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPaymentStatus(rs.getString("payment_status"));
                    return booking;
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching booking by id", e);
        }

        return null;
    }
}
