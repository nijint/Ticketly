package com.ticketly;

import com.ticketly.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple test class to check database connection and query users table.
 */
public class TestDatabaseConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Testing database connection...");
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("Database connection successful!");

            // Test query on users table
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Users table exists with " + count + " records.");
            }
            rs.close();
            stmt.close();

            // Test insert a user
            com.ticketly.dao.UserRepository userRepo = new com.ticketly.dao.UserRepository();
            com.ticketly.model.User testUser = new com.ticketly.model.User("testuser", "test@example.com", "$2a$10$test.hash", "Test User", "USER");
            try {
                userRepo.save(testUser);
                System.out.println("Test user inserted successfully.");
            } catch (Exception e) {
                System.out.println("Failed to insert test user: " + e.getMessage());
            }

            conn.close();
            System.out.println("Connection closed successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
