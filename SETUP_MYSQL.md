# MySQL Setup Guide for Ticketly

## Prerequisites
- MySQL Server installed and running
- MySQL Workbench or command line client

## Step 1: Create Database
```sql
CREATE DATABASE ticketly;
```

## Step 2: Create Tables
Run these SQL commands in MySQL:

```sql
USE ticketly;

-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Theaters table
CREATE TABLE theaters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    capacity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Movies table
CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL,
    genre VARCHAR(50),
    release_date DATE,
    poster_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Shows table
CREATE TABLE shows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    theater_id BIGINT NOT NULL,
    show_date DATE NOT NULL,
    show_time TIME NOT NULL,
    price_regular DECIMAL(10,2) NOT NULL,
    price_premium DECIMAL(10,2),
    price_vip DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (theater_id) REFERENCES theaters(id)
);

-- Seats table
CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    theater_id BIGINT NOT NULL,
    seat_row VARCHAR(5) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    seat_type ENUM('REGULAR', 'PREMIUM', 'VIP') DEFAULT 'REGULAR',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (theater_id) REFERENCES theaters(id),
    UNIQUE KEY unique_seat_theater (theater_id, seat_row, seat_number)
);

-- Bookings table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    show_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    status ENUM('CONFIRMED', 'CANCELLED') DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (show_id) REFERENCES shows(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id)
);

-- Insert sample data
INSERT INTO theaters (name, location, capacity) VALUES
('Theater 1', 'Downtown', 100),
('Theater 2', 'Mall', 80),
('Theater 3', 'Cinema Complex', 120);

INSERT INTO users (username, password_hash, email, full_name) VALUES
('admin', 'admin@ticketly.com', 'Administrator');

INSERT INTO movies (title, description, duration_minutes, genre, release_date) VALUES
('Avengers: Endgame', 'The epic conclusion to the Infinity Saga', 181, 'Action', '2019-04-26'),
('The Dark Knight', 'Batman faces the Joker in Gotham City', 152, 'Action', '2008-07-18'),
('Inception', 'A thief who steals corporate secrets through dream-sharing technology', 148, 'Sci-Fi', '2010-07-16');

INSERT INTO shows (movie_id, theater_id, show_date, show_time, price_regular, price_premium, price_vip) VALUES
(1, 1, '2025-10-15', '14:00:00', 15.00, 20.00, 25.00),
(1, 1, '2025-10-15', '18:00:00', 18.00, 23.00, 28.00),
(2, 2, '2025-10-15', '16:00:00', 12.00, 17.00, 22.00),
(3, 3, '2025-10-15', '20:00:00', 14.00, 19.00, 24.00);

-- Insert sample seats for theaters
INSERT INTO seats (theater_id, seat_row, seat_number, seat_type) VALUES
(1, 'A', '1', 'REGULAR'), (1, 'A', '2', 'REGULAR'), (1, 'A', '3', 'PREMIUM'),
(1, 'B', '1', 'REGULAR'), (1, 'B', '2', 'REGULAR'), (1, 'B', '3', 'VIP');
-- Add more seats as needed
```

## Step 3: Update database.properties
Edit `src/main/resources/database.properties`:

```
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/ticketly?useSSL=false&serverTimezone=UTC
db.username=your_mysql_username
db.password=your_mysql_password
```

## Step 4: Update DatabaseUtil.java
Modify `src/main/java/com/ticketly/util/DatabaseUtil.java` to not initialize schema for MySQL:

```java
// Comment out or remove the schema initialization for MySQL
// initializeDatabaseSchema();
// insertSampleData();
```

## Step 5: Create Model Classes
Create these new model classes:

### Movie.java
```java
package com.ticketly.model;

import java.time.LocalDate;

public class Movie {
    private int id;
    private String title;
    private String description;
    private int durationMinutes;
    private String genre;
    private LocalDate releaseDate;
    private String posterUrl;

    // Getters and setters
}
```

### Show.java
```java
package com.ticketly.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Show {
    private int id;
    private int movieId;
    private LocalDateTime showTime;
    private String theater;
    private BigDecimal price;
    private int availableSeats;

    // Getters and setters
}
```

### Seat.java
```java
package com.ticketly.model;

public class Seat {
    private int id;
    private int showId;
    private String seatNumber;
    private String rowNumber;
    private boolean isBooked;

    // Getters and setters
}
```

### Booking.java
```java
package com.ticketly.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int userId;
    private int showId;
    private int seatId;
    private LocalDateTime bookingTime;
    private BigDecimal totalAmount;
    private String status;

    // Getters and setters
}
```

## Step 6: Create DAO Classes
Create corresponding DAO classes in `src/main/java/com/ticketly/dao/`:

- MovieRepository.java
- ShowRepository.java
- SeatRepository.java
- BookingRepository.java

## Step 7: Update SceneManager.java
Add methods for:
- showMovieSelectionScreen()
- showShowSelectionScreen(int movieId)
- showSeatSelectionScreen(int showId)
- showBookingConfirmation(Booking booking)

## Step 8: Update AuthService.java
Modify login success to navigate to movie selection instead of showing alert.

## Step 9: Test the Application
1. Run `mvn clean compile`
2. Run `mvn javafx:run`
3. Test login -> movie selection -> show selection -> seat selection -> booking

## Files to Modify/Create:
1. pom.xml (MySQL dependency added)
2. database.properties (MySQL config)
3. DatabaseUtil.java (disable schema init)
4. Create Movie.java, Show.java, Seat.java, Booking.java models
5. Create MovieRepository.java, ShowRepository.java, SeatRepository.java, BookingRepository.java DAOs
6. SceneManager.java (add movie booking UI)
7. AuthService.java (update navigation after login)
