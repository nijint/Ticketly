package com.ticketly.dao;

import com.ticketly.model.Theater;
import com.ticketly.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TheaterRepository {
    private static final Logger logger = LoggerFactory.getLogger(TheaterRepository.class);

    public List<Theater> findAll() {
        List<Theater> theaters = new ArrayList<>();
        String sql = "SELECT id, name, location, total_seats, created_at FROM theaters ORDER BY name";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Theater theater = new Theater();
                theater.setId(rs.getLong("id"));
                theater.setName(rs.getString("name"));
                theater.setLocation(rs.getString("location"));
                theater.setTotalSeats(rs.getInt("total_seats"));
                theater.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                theaters.add(theater);
            }

            logger.info("Found {} theaters", theaters.size());
        } catch (SQLException e) {
            logger.error("Error fetching theaters", e);
        }

        return theaters;
    }

    public Theater findById(long id) {
        String sql = "SELECT id, name, location, total_seats, created_at FROM theaters WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Theater theater = new Theater();
                    theater.setId(rs.getLong("id"));
                    theater.setName(rs.getString("name"));
                    theater.setLocation(rs.getString("location"));
                    theater.setTotalSeats(rs.getInt("total_seats"));
                    theater.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return theater;
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching theater by id: {}", id, e);
        }

        return null;
    }

    public List<Theater> findByMovieId(long movieId) {
        List<Theater> theaters = new ArrayList<>();
        String sql = "SELECT DISTINCT t.id, t.name, t.location, t.total_seats, t.created_at " +
                     "FROM theaters t " +
                     "JOIN shows s ON t.id = s.theater_id " +
                     "WHERE s.movie_id = ? " +
                     "ORDER BY t.name";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Theater theater = new Theater();
                    theater.setId(rs.getLong("id"));
                    theater.setName(rs.getString("name"));
                    theater.setLocation(rs.getString("location"));
                    theater.setTotalSeats(rs.getInt("total_seats"));
                    theater.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    theaters.add(theater);
                }
            }

            logger.info("Found {} theaters for movie {}", theaters.size(), movieId);
        } catch (SQLException e) {
            logger.error("Error fetching theaters by movie id: {}", movieId, e);
        }

        return theaters;
    }
}
