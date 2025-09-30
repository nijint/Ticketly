package com.ticketly.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database utility class for managing database connections and initialization.
 * Uses HikariCP for connection pooling.
 */
public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static HikariDataSource dataSource;

    static {
        initializeDataSource();
    }

    /**
     * Initialize the database connection pool.
     */
    private static void initializeDataSource() {
        try {
            Properties props = loadDatabaseProperties();

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("db.driver"));
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));

            // Connection pool settings
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.connection.pool.max", "20")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("db.connection.pool.initial", "5")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("db.connection.timeout", "30000")));

            // Additional settings for better performance
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");

        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Load database properties from configuration file.
     */
    private static Properties loadDatabaseProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = DatabaseUtil.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IOException("database.properties not found in classpath");
            }
            props.load(input);
        }
        return props;
    }

    /**
     * Get a database connection from the pool.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Get the data source.
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Initialize database schema and sample data.
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            logger.info("Initializing database schema...");

            // Create tables
            createTables(stmt);

            // Insert sample data
            insertSampleData(stmt);

            logger.info("Database initialization completed successfully");

        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Create database tables.
     */
    private static void createTables(Statement stmt) throws SQLException {
        // Users table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                full_name VARCHAR(100),
                phone VARCHAR(20),
                role ENUM('USER', 'ADMIN') DEFAULT 'USER',
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
            """);

        // Movies table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS movies (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description TEXT,
                duration_minutes INT NOT NULL,
                language VARCHAR(50),
                genre VARCHAR(100),
                release_date DATE,
                poster_url VARCHAR(500),
                rating DECIMAL(3,1),
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);

        // Theaters table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS theaters (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                location VARCHAR(255),
                total_seats INT NOT NULL,
                rows_count INT NOT NULL,
                seats_per_row INT NOT NULL,
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);

        // Shows table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS shows (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                movie_id BIGINT NOT NULL,
                theater_id BIGINT NOT NULL,
                show_date DATE NOT NULL,
                show_time TIME NOT NULL,
                price_regular DECIMAL(10,2) NOT NULL,
                price_premium DECIMAL(10,2) NOT NULL,
                price_vip DECIMAL(10,2) NOT NULL,
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (movie_id) REFERENCES movies(id),
                FOREIGN KEY (theater_id) REFERENCES theaters(id)
            )
            """);

        // Seats table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS seats (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                theater_id BIGINT NOT NULL,
                seat_row CHAR(1) NOT NULL,
                seat_number INT NOT NULL,
                seat_type ENUM('REGULAR', 'PREMIUM', 'VIP') DEFAULT 'REGULAR',
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (theater_id) REFERENCES theaters(id),
                UNIQUE KEY unique_seat (theater_id, seat_row, seat_number)
            )
            """);

        // Bookings table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS bookings (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                user_id BIGINT NOT NULL,
                show_id BIGINT NOT NULL,
                booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                total_amount DECIMAL(10,2) NOT NULL,
                status ENUM('CONFIRMED', 'CANCELLED', 'PENDING') DEFAULT 'PENDING',
                payment_status ENUM('PAID', 'PENDING', 'FAILED') DEFAULT 'PENDING',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (show_id) REFERENCES shows(id)
            )
            """);

        // Booking seats junction table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS booking_seats (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                booking_id BIGINT NOT NULL,
                seat_id BIGINT NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (booking_id) REFERENCES bookings(id),
                FOREIGN KEY (seat_id) REFERENCES seats(id),
                UNIQUE KEY unique_booking_seat (booking_id, seat_id)
            )
            """);

        // Payments table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS payments (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                booking_id BIGINT NOT NULL,
                amount DECIMAL(10,2) NOT NULL,
                payment_method VARCHAR(50),
                transaction_id VARCHAR(255),
                payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                status ENUM('SUCCESS', 'FAILED', 'PENDING') DEFAULT 'PENDING',
                FOREIGN KEY (booking_id) REFERENCES bookings(id)
            )
            """);

        // Seat reservations table (for temporary locking)
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS seat_reservations (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                seat_id BIGINT NOT NULL,
                user_id BIGINT NOT NULL,
                show_id BIGINT NOT NULL,
                reserved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                expires_at TIMESTAMP NOT NULL,
                FOREIGN KEY (seat_id) REFERENCES seats(id),
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (show_id) REFERENCES shows(id),
                UNIQUE KEY unique_reservation (seat_id, show_id)
            )
            """);

        logger.info("Database tables created successfully");
    }

    /**
     * Insert sample data for testing.
     */
    private static void insertSampleData(Statement stmt) throws SQLException {
        // Insert admin user
        stmt.execute("""
            INSERT IGNORE INTO users (username, email, password_hash, full_name, role)
            VALUES ('admin', 'admin@ticketly.com',
                    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj6fM9q1lqKS',
                    'System Administrator', 'ADMIN')
            """);

        // Insert sample movies
        stmt.execute("""
            INSERT IGNORE INTO movies (title, description, duration_minutes, language, genre, release_date, rating)
            VALUES
            ('The Dark Knight', 'Batman faces the Joker in Gotham City', 152, 'English', 'Action,Crime,Drama', '2008-07-18', 9.0),
            ('Inception', 'A thief enters dreams to steal secrets', 148, 'English', 'Action,Sci-Fi,Thriller', '2010-07-16', 8.8),
            ('Interstellar', 'Astronauts travel through a wormhole', 169, 'English', 'Adventure,Drama,Sci-Fi', '2014-11-07', 8.6)
            """);

        // Insert sample theater
        stmt.execute("""
            INSERT IGNORE INTO theaters (name, location, total_seats, rows_count, seats_per_row)
            VALUES ('Grand Cinema', 'Downtown', 100, 10, 10)
            """);

        logger.info("Sample data inserted successfully");
    }

    /**
     * Close the data source.
     */
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }
}
