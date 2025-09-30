# 🎬 Ticketly - Movie Ticket Booking System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-19+-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A comprehensive movie ticket booking desktop application built with Java, JavaFX, and SQL database. Ticketly provides both user and admin interfaces for managing movie bookings, theaters, and showtimes with modern UI design and robust backend architecture.

## 🎯 Project Overview

**Project Name**: Ticketly  
**Version**: 1.0.0  
**Technology Stack**: Java 17, JavaFX 19, MySQL/PostgreSQL, Maven  
**Architecture**: MVC (Model-View-Controller) with Service Layer  
**Development Status**: Core Implementation Complete

## ✨ Features

### 🧑‍💻 User Module
- [x] **User Registration & Login** - Secure authentication with BCrypt password hashing
- [x] **Profile Management** - Update email, password, and personal information
- [x] **Browse Movies** - List, search, and filter movies by language/genre
- [x] **View Showtimes** - Browse available shows by date, time, and theater
- [ ] **Interactive Seat Selection** - Visual grid layout for seat booking (Framework Ready)
- [ ] **Ticket Booking** - Secure booking with seat locking and confirmation (Framework Ready)
- [ ] **Booking History** - View past tickets with movie details (Framework Ready)

### 👨‍💼 Admin Module
- [x] **Admin Authentication** - Separate admin login system
- [ ] **Manage Movies** - CRUD operations for movies (Framework Ready)
- [ ] **Manage Theaters** - Theater and seating configuration (Framework Ready)
- [ ] **Manage Shows** - Schedule shows with pricing (Framework Ready)
- [ ] **User Management** - View and manage user accounts (Framework Ready)
- [ ] **Reports & Analytics** - Booking reports and revenue summary (Framework Ready)

### 🛠️ Technical Features
- [x] **Database Integration** - MySQL/PostgreSQL with connection pooling
- [x] **Authentication & Authorization** - Role-based access control
- [x] **Password Security** - BCrypt hashing with strength validation
- [x] **Modern UI Design** - Responsive JavaFX interface with CSS styling
- [x] **Error Handling** - Comprehensive validation and error management
- [x] **Logging** - SLF4J with Logback for application monitoring

## 🗄️ Database Schema

### Core Tables
- **`users`** - User accounts with role management
- **`movies`** - Movie catalog with metadata
- **`theaters`** - Theater locations and seating layouts
- **`shows`** - Movie screenings with pricing
- **`seats`** - Individual seat configurations
- **`bookings`** - Ticket reservations
- **`booking_seats`** - Seat-booking relationships
- **`payments`** - Payment transactions
- **`seat_reservations`** - Temporary seat locking

### Key Relationships
- Users → Bookings (One-to-Many)
- Movies → Shows (One-to-Many)
- Shows → Seats (Many-to-Many via booking_seats)
- Bookings → Payments (One-to-One)

## 📁 Project Structure

```
ticketly/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ticketly/
│   │   │       ├── TicketlyApplication.java          # Main application entry
│   │   │       ├── controller/                      # JavaFX controllers
│   │   │       │   ├── LoginController.java         # Login screen controller
│   │   │       │   ├── user/                       # User module controllers
│   │   │       │   └── admin/                      # Admin module controllers
│   │   │       ├── model/                          # Entity classes
│   │   │       │   ├── User.java                   # User entity
│   │   │       │   ├── Movie.java                  # Movie entity
│   │   │       │   ├── Theater.java                # Theater entity
│   │   │       │   ├── Show.java                   # Show entity
│   │   │       │   ├── Seat.java                   # Seat entity
│   │   │       │   └── Booking.java                # Booking entity
│   │   │       ├── dao/                            # Data Access Objects
│   │   │       │   ├── BaseDao.java                # Base DAO interface
│   │   │       │   ├── UserDao.java                # User DAO interface
│   │   │       │   └── impl/                       # DAO implementations
│   │   │       │       └── UserDaoImpl.java        # User DAO implementation
│   │   │       ├── service/                        # Business logic layer
│   │   │       │   └── UserService.java            # User service
│   │   │       └── util/                           # Utility classes
│   │   │           ├── DatabaseUtil.java           # Database connection
│   │   │           ├── PasswordUtil.java           # Password operations
│   │   │           └── SceneManager.java           # JavaFX scene management
│   │   └── resources/
│   │       ├── fxml/                               # JavaFX FXML files
│   │       │   ├── login.fxml                      # Login screen
│   │       │   ├── user/                          # User interface screens
│   │       │   └── admin/                         # Admin interface screens
│   │       ├── css/
│   │       │   └── styles.css                      # Application styling
│   │       ├── sql/
│   │       │   └── schema.sql                      # Database schema
│   │       └── database.properties                 # Database configuration
│   └── test/
│       └── java/
│           └── com/ticketly/
│               └── util/
│                   └── PasswordUtilTest.java       # Unit tests
├── pom.xml                                         # Maven configuration
├── .gitignore                                      # Git ignore rules
└── README.md                                       # Project documentation
```

## 🚀 Getting Started

### Prerequisites
- **Java 17+** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.java.net/)
- **Maven 3.8+** - Download from [Apache Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** or **PostgreSQL 12+** - Database server
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/ticketly.git
   cd ticketly
   ```

2. **Database Setup**
   ```sql
   -- Create database (MySQL)
   CREATE DATABASE ticketly CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- Create user (optional)
   CREATE USER 'ticketly_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON ticketly.* TO 'ticketly_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure Database Connection**
   
   Edit `src/main/resources/database.properties`:
   ```properties
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/ticketly?useSSL=false&serverTimezone=UTC
   db.username=your_username
   db.password=your_password
   ```

4. **Build and Run**
   ```bash
   # Compile the project
   mvn clean compile
   
   # Run the application
   mvn javafx:run
   
   # Or create executable JAR
   mvn clean package
   java -jar target/ticketly-1.0.0-jar-with-dependencies.jar
   ```

### Default Admin Account
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: `admin@ticketly.com`

## 🔧 Configuration

### Database Configuration
The application supports both MySQL and PostgreSQL. Configure your database connection in `database.properties`:

**MySQL Configuration:**
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/ticketly?useSSL=false&serverTimezone=UTC
```

**PostgreSQL Configuration:**
```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/ticketly
```

### Application Properties
- **Password Security**: BCrypt with cost factor 12
- **Session Management**: In-memory user sessions
- **UI Themes**: Modern CSS styling with responsive design
- **Logging**: SLF4J with Logback configuration

## 📊 Current Implementation Status

### ✅ Completed Features
- **Core Architecture**: MVC structure with service layer
- **Database Layer**: Full DAO implementation with MySQL support
- **Authentication**: Secure login with password hashing
- **User Management**: Registration, profile updates, password changes
- **UI Framework**: JavaFX with modern CSS styling
- **Utilities**: Database connection, password security, scene management

### 🚧 Framework Ready (Implementation Pending)
- **Seat Selection**: Interactive grid UI for seat booking
- **Booking System**: Transaction-safe booking with seat locking
- **Admin Dashboard**: Complete admin interface for system management
- **Reports**: Booking analytics and revenue reporting
- **Payment Integration**: Mock payment system framework

### 🎯 Recommended Next Steps

1. **Complete Seat Selection UI**
   - Implement interactive seat grid component
   - Add seat type styling and availability status
   - Integrate with booking service layer

2. **Implement Booking System**
   - Add booking service with transaction support
   - Implement seat reservation and locking mechanism
   - Create booking confirmation and history screens

3. **Admin Module Development**
   - Complete admin controllers and FXML screens
   - Implement movie, theater, and show management
   - Add user management and reporting features

4. **Enhanced Features**
   - Email notifications for booking confirmations
   - PDF ticket generation
   - Advanced search and filtering
   - Payment gateway integration

## 🧪 Testing

Run unit tests:
```bash
mvn test
```

Current test coverage includes:
- Password utility functions
- User authentication logic
- Database connection validation

## 📝 API Documentation

### Service Layer APIs

#### UserService
- `authenticate(username, password)` - User authentication
- `registerUser(user, password)` - New user registration
- `updateUserProfile(user)` - Profile management
- `changePassword(userId, currentPassword, newPassword)` - Password updates

#### Database Layer
- **BaseDao**: Generic CRUD operations
- **UserDao**: User-specific database operations
- **Connection Management**: Automatic connection pooling and cleanup

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java naming conventions
- Write comprehensive JavaDoc comments
- Include unit tests for new features
- Use consistent code formatting
- Update documentation for API changes

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **JavaFX Community** - For excellent UI framework and documentation
- **Maven Central** - For dependency management
- **BCrypt Library** - For secure password hashing
- **SLF4J/Logback** - For logging infrastructure

## 📞 Support

For support and questions:
- Create an issue on GitHub
- Email: support@ticketly.com
- Documentation: [Wiki](https://github.com/yourusername/ticketly/wiki)

---

**Ticketly v1.0.0** - Built with ❤️ using Java and JavaFX