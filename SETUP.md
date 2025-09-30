# 🚀 Ticketly Setup Guide

This guide will help you set up and run the Ticketly Movie Ticket Booking System on your local machine.

## 📋 Prerequisites

### Required Software

1. **Java Development Kit (JDK) 17 or higher**
   - Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.java.net/)
   - Verify installation: `java -version`

2. **Apache Maven 3.8 or higher**
   - Download from [Apache Maven](https://maven.apache.org/download.cgi)
   - Verify installation: `mvn -version`

3. **Database Server (Choose one)**
   - **MySQL 8.0+** (Recommended)
     - Download from [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
   - **PostgreSQL 12+** (Alternative)
     - Download from [PostgreSQL](https://www.postgresql.org/download/)

4. **IDE (Recommended)**
   - [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/) (Recommended)
   - [Eclipse IDE](https://www.eclipse.org/downloads/)
   - [Visual Studio Code](https://code.visualstudio.com/) with Java extensions

## 🛠️ Step-by-Step Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/ticketly.git
cd ticketly
```

### Step 2: Database Setup

#### Option A: MySQL Setup

1. **Install and Start MySQL Server**
   ```bash
   # On Windows (using MySQL Installer)
   # Download and run MySQL Installer from official website
   
   # On macOS (using Homebrew)
   brew install mysql
   brew services start mysql
   
   # On Ubuntu/Debian
   sudo apt update
   sudo apt install mysql-server
   sudo systemctl start mysql
   ```

2. **Create Database and User**
   ```sql
   -- Connect to MySQL as root
   mysql -u root -p
   
   -- Create the database
   CREATE DATABASE ticketly CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- Create a dedicated user (optional but recommended)
   CREATE USER 'ticketly_user'@'localhost' IDENTIFIED BY 'ticketly_password';
   GRANT ALL PRIVILEGES ON ticketly.* TO 'ticketly_user'@'localhost';
   FLUSH PRIVILEGES;
   
   -- Verify database creation
   SHOW DATABASES;
   USE ticketly;
   ```

#### Option B: PostgreSQL Setup

1. **Install and Start PostgreSQL**
   ```bash
   # On Windows
   # Download installer from PostgreSQL website
   
   # On macOS (using Homebrew)
   brew install postgresql
   brew services start postgresql
   
   # On Ubuntu/Debian
   sudo apt update
   sudo apt install postgresql postgresql-contrib
   sudo systemctl start postgresql
   ```

2. **Create Database and User**
   ```bash
   # Connect to PostgreSQL as postgres user
   sudo -u postgres psql
   
   # In PostgreSQL prompt:
   CREATE DATABASE ticketly;
   CREATE USER ticketly_user WITH PASSWORD 'ticketly_password';
   GRANT ALL PRIVILEGES ON DATABASE ticketly TO ticketly_user;
   \\q
   ```

### Step 3: Configure Database Connection

1. **Edit Database Configuration**
   
   Open `src/main/resources/database.properties` and update the configuration:

   **For MySQL:**
   ```properties
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/ticketly?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=ticketly_user
   db.password=ticketly_password
   ```

   **For PostgreSQL:**
   ```properties
   db.driver=org.postgresql.Driver
   db.url=jdbc:postgresql://localhost:5432/ticketly
   db.username=ticketly_user
   db.password=ticketly_password
   ```

### Step 4: Build and Run the Application

1. **Compile the Project**
   ```bash
   mvn clean compile
   ```

2. **Run the Application**
   ```bash
   mvn javafx:run
   ```

   **Alternative: Create Executable JAR**
   ```bash
   mvn clean package
   java -jar target/ticketly-1.0.0-jar-with-dependencies.jar
   ```

### Step 5: First Login

The application will automatically create the database schema and insert sample data on first run.

**Default Admin Account:**
- Username: `admin`
- Password: `admin123`
- Email: `admin@ticketly.com`

## 🔧 IDE Setup Instructions

### IntelliJ IDEA

1. **Import Project**
   - Open IntelliJ IDEA
   - Click "Open or Import"
   - Select the `ticketly` folder
   - Choose "Import project from external model" → "Maven"
   - Click "Finish"

2. **Configure JavaFX**
   - Go to File → Project Structure
   - Under Project Settings → Libraries
   - Verify JavaFX libraries are included (Maven should handle this automatically)

3. **Run Configuration**
   - Go to Run → Edit Configurations
   - Click "+" and select "Application"
   - Main class: `com.ticketly.TicketlyApplication`
   - Module: `ticketly`
   - VM Options: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`

### Eclipse IDE

1. **Import Maven Project**
   - File → Import → Existing Maven Projects
   - Browse to the `ticketly` folder
   - Click "Finish"

2. **Configure Build Path**
   - Right-click project → Properties
   - Java Build Path → Modulepath
   - Add External JARs (JavaFX JARs if not automatically included)

3. **Run Configuration**
   - Right-click TicketlyApplication.java → Run As → Java Application
   - If JavaFX modules error occurs, add VM arguments in Run Configuration

### Visual Studio Code

1. **Install Extensions**
   - Java Extension Pack
   - JavaFX Support (optional)

2. **Open Project**
   - File → Open Folder
   - Select the `ticketly` folder

3. **Configure launch.json**
   ```json
   {
     "type": "java",
     "name": "Launch TicketlyApplication",
     "request": "launch",
     "mainClass": "com.ticketly.TicketlyApplication",
     "vmArgs": "--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml"
   }
   ```

## 🔍 Troubleshooting

### Common Issues and Solutions

#### 1. JavaFX Runtime Not Found
**Error:** `Error: JavaFX runtime components are missing`

**Solution:**
- Ensure JavaFX is included in module path
- Add VM arguments: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`
- Or run via Maven: `mvn javafx:run`

#### 2. Database Connection Failed
**Error:** `SQLException: Connection refused`

**Solutions:**
- Verify database server is running
- Check database credentials in `database.properties`
- Ensure database exists
- For MySQL, try adding `allowPublicKeyRetrieval=true` to connection URL

#### 3. Schema Not Created
**Error:** `Table 'users' doesn't exist`

**Solutions:**
- Check database permissions
- Verify SQL script is in resources folder
- Manually run `src/main/resources/sql/schema.sql`

#### 4. Maven Compilation Errors
**Error:** Various compilation issues

**Solutions:**
- Run `mvn clean install`
- Update Maven dependencies: `mvn dependency:resolve`
- Check Java version: `java -version`

#### 5. Port Already in Use (Database)
**Error:** `Port 3306 already in use`

**Solutions:**
- Stop other MySQL instances
- Change port in database.properties
- Use different database instance

### Performance Optimization

1. **Database Connection Pooling**
   ```properties
   # Add to database.properties
   db.connection.pool.initial=5
   db.connection.pool.max=20
   ```

2. **JVM Memory Settings**
   ```bash
   java -Xmx2g -Xms512m -jar ticketly.jar
   ```

## 📊 Verification Steps

### 1. Check Application Startup
- Application window opens successfully
- Login screen is displayed
- No error messages in console

### 2. Test Database Connection
- Application logs show "Database connection established"
- Sample data is loaded (check with admin login)

### 3. Test Authentication
- Admin login works with default credentials
- User registration creates new accounts

### 4. Verify UI Components
- All screens load without errors
- CSS styling is applied correctly
- Navigation between screens works

## 🚀 Development Mode

For development with hot reload:

1. **Run in Development Mode**
   ```bash
   mvn compile javafx:run
   ```

2. **Enable Debug Logging**
   Edit `src/main/resources/logback.xml`:
   ```xml
   <root level="DEBUG">
     <appender-ref ref="STDOUT"/>
   </root>
   ```

3. **Auto-compilation (IntelliJ)**
   - Build → Build Project Automatically
   - Help → Find Action → Registry → compiler.automake.allow.when.app.running

## 📞 Getting Help

If you encounter issues:

1. Check the troubleshooting section above
2. Review application logs in `logs/application.log`
3. Create an issue on GitHub with:
   - Error messages
   - Your system configuration
   - Steps to reproduce the problem

## ✅ Next Steps

Once setup is complete:

1. **Explore the Application**
   - Login as admin and regular user
   - Browse through different screens
   - Test basic functionality

2. **Review Documentation**
   - Read the main README.md
   - Check code comments and JavaDoc
   - Understand the project structure

3. **Start Development**
   - Implement pending features
   - Add new functionality
   - Contribute to the project

---

**Happy Coding! 🎬✨**