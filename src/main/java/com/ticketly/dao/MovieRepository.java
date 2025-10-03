package com.ticketly.dao;

import com.ticketly.model.Movie;
import com.ticketly.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private static final Logger logger = LoggerFactory.getLogger(MovieRepository.class);

    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY title";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(rs.getString("title"));
                movie.setDescription(rs.getString("description"));
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                movie.setGenre(rs.getString("genre"));
                movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
                movie.setPosterUrl(rs.getString("poster_url"));
                movies.add(movie);
            }

        } catch (SQLException e) {
            logger.error("Error fetching movies", e);
        }

        return movies;
    }

    public Movie findById(int id) {
        String sql = "SELECT * FROM movies WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie();
                    movie.setId(rs.getInt("id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setDescription(rs.getString("description"));
                    movie.setDurationMinutes(rs.getInt("duration_minutes"));
                    movie.setGenre(rs.getString("genre"));
                    movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
                    movie.setPosterUrl(rs.getString("poster_url"));
                    return movie;
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching movie by id", e);
        }

        return null;
    }
}
