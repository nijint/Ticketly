package com.ticketly.dao;

import com.ticketly.model.Show;
import com.ticketly.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowRepository {
    private static final Logger logger = LoggerFactory.getLogger(ShowRepository.class);

    public List<Show> findByMovieId(long movieId) {
        List<Show> shows = new ArrayList<>();
        String sql = "SELECT s.*, t.name as theater_name FROM shows s JOIN theaters t ON s.theater_id = t.id WHERE s.movie_id = ? ORDER BY s.show_date, s.show_time";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Show show = new Show();
                    show.setId(rs.getLong("id"));
                    show.setMovieId(rs.getLong("movie_id"));
                    show.setTheaterId(rs.getLong("theater_id"));
                    // Combine show_date and show_time into LocalDateTime
                    java.time.LocalDate date = rs.getDate("show_date").toLocalDate();
                    java.time.LocalTime time = rs.getTime("show_time").toLocalTime();
                    show.setShowTime(java.time.LocalDateTime.of(date, time));
                    show.setTheaterName(rs.getString("theater_name"));
                    show.setPrice(rs.getBigDecimal("price_regular")); // assuming regular price
                    // Calculate available seats: total seats - booked seats
                    int totalSeats = getTotalSeatsForTheater(rs.getLong("theater_id"));
                    int bookedSeats = getBookedSeatsForShow(rs.getLong("id"));
                    show.setAvailableSeats(totalSeats - bookedSeats);
                    shows.add(show);
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching shows by movie id", e);
        }

        return shows;
    }

    public List<Show> findByMovieIdAndTheaterId(long movieId, long theaterId) {
        List<Show> shows = new ArrayList<>();
        String sql = "SELECT s.*, t.name as theater_name FROM shows s JOIN theaters t ON s.theater_id = t.id WHERE s.movie_id = ? AND s.theater_id = ? ORDER BY s.show_date, s.show_time";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, movieId);
            stmt.setLong(2, theaterId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Show show = new Show();
                    show.setId(rs.getLong("id"));
                    show.setMovieId(rs.getLong("movie_id"));
                    show.setTheaterId(rs.getLong("theater_id"));
                    // Combine show_date and show_time into LocalDateTime
                    java.time.LocalDate date = rs.getDate("show_date").toLocalDate();
                    java.time.LocalTime time = rs.getTime("show_time").toLocalTime();
                    show.setShowTime(java.time.LocalDateTime.of(date, time));
                    show.setTheaterName(rs.getString("theater_name"));
                    show.setPrice(rs.getBigDecimal("price_regular")); // assuming regular price
                    // Calculate available seats: total seats - booked seats
                    int totalSeats = getTotalSeatsForTheater(rs.getLong("theater_id"));
                    int bookedSeats = getBookedSeatsForShow(rs.getLong("id"));
                    show.setAvailableSeats(totalSeats - bookedSeats);
                    shows.add(show);
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching shows by movie and theater id", e);
        }

        return shows;
    }

    public Show findById(long id) {
        String sql = "SELECT s.*, t.name as theater_name FROM shows s JOIN theaters t ON s.theater_id = t.id WHERE s.id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Show show = new Show();
                    show.setId(rs.getLong("id"));
                    show.setMovieId(rs.getLong("movie_id"));
                    show.setTheaterId(rs.getLong("theater_id"));
                    java.time.LocalDate date = rs.getDate("show_date").toLocalDate();
                    java.time.LocalTime time = rs.getTime("show_time").toLocalTime();
                    show.setShowTime(java.time.LocalDateTime.of(date, time));
                    show.setTheaterName(rs.getString("theater_name"));
                    show.setPrice(rs.getBigDecimal("price_regular"));
                    int totalSeats = getTotalSeatsForTheater(rs.getLong("theater_id"));
                    int bookedSeats = getBookedSeatsForShow(rs.getLong("id"));
                    show.setAvailableSeats(totalSeats - bookedSeats);
                    return show;
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching show by id", e);
        }

        return null;
    }

    private int getTotalSeatsForTheater(long theaterId) {
        String sql = "SELECT COUNT(*) FROM seats WHERE theater_id = ? AND is_active = true";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, theaterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Error counting seats for theater", e);
        }
        return 0;
    }

    private int getBookedSeatsForShow(long showId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE show_id = ? AND status = 'CONFIRMED'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, showId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Error counting booked seats for show", e);
        }
        return 0;
    }
}
