package com.busreservation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    // Update these values to match your MySQL configuration
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "bus_reservation";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "MYSQL"; // Your MySQL password - try empty string if you don't have a password
    
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // First, try to connect to MySQL server
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Try to connect to the server first (without specifying database)
                Connection serverConnection = DriverManager.getConnection(URL, USER, PASSWORD);
                
                // Create database if it doesn't exist
                try (Statement stmt = serverConnection.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                }
                serverConnection.close();
                
                // Now connect to the specific database
                connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
                System.out.println("Database connection established successfully");
                
                createTablesIfNotExist();
            } catch (ClassNotFoundException e) {
                showError("MySQL JDBC Driver not found. Please add the MySQL connector JAR to your project.", e);
            } catch (SQLException e) {
                String message = "Database connection error: " + e.getMessage() + "\n\n" +
                                "Please check:\n" +
                                "1. MySQL server is running\n" +
                                "2. Username and password are correct\n" +
                                "3. Database permissions are set correctly";
                showError(message, e);
            }
        }
        return connection;
    }
    
    private static void showError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
        
        // Show error dialog
        JOptionPane.showMessageDialog(null, 
                message, 
                "Database Connection Error", 
                JOptionPane.ERROR_MESSAGE);
    }
    
    private static void createTablesIfNotExist() {
        if (connection == null) {
            return;
        }
        
        try (Statement stmt = connection.createStatement()) {
            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(50) NOT NULL, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "phone VARCHAR(20), " +
                    "user_type ENUM('ADMIN', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(createUsersTable);
            
            // Create buses table
            String createBusesTable = "CREATE TABLE IF NOT EXISTS buses (" +
                    "bus_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "bus_number VARCHAR(20) UNIQUE NOT NULL, " +
                    "bus_name VARCHAR(100) NOT NULL, " +
                    "source_location VARCHAR(100) NOT NULL, " +
                    "destination VARCHAR(100) NOT NULL, " +
                    "departure_time TIME NOT NULL, " +
                    "arrival_time TIME NOT NULL, " +
                    "journey_date DATE NOT NULL, " +
                    "total_seats INT NOT NULL, " +
                    "available_seats INT NOT NULL, " +
                    "fare DOUBLE NOT NULL, " +
                    "bus_type ENUM('AC', 'NON_AC', 'SLEEPER', 'SEMI_SLEEPER') NOT NULL" +
                    ")";
            stmt.executeUpdate(createBusesTable);
            
            // Create reservations table
            String createReservationsTable = "CREATE TABLE IF NOT EXISTS reservations (" +
                    "reservation_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "bus_id INT NOT NULL, " +
                    "booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "journey_date DATE NOT NULL, " +
                    "number_of_seats INT NOT NULL, " +
                    "total_fare DOUBLE NOT NULL, " +
                    "status ENUM('CONFIRMED', 'CANCELLED', 'PENDING') NOT NULL DEFAULT 'CONFIRMED', " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                    "FOREIGN KEY (bus_id) REFERENCES buses(bus_id)" +
                    ")";
            stmt.executeUpdate(createReservationsTable);
            
            // Check if admin user exists
            String checkAdmin = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            boolean adminExists = false;
            if (rs.next()) {
                adminExists = rs.getInt(1) > 0;
            }
            rs.close();
            
            // Insert default admin user if not exists
            if (!adminExists) {
                String insertAdmin = "INSERT INTO users (username, password, full_name, email, user_type) " +
                        "VALUES ('admin', 'admin123', 'System Administrator', 'admin@busreservation.com', 'ADMIN')";
                stmt.executeUpdate(insertAdmin);
                System.out.println("Admin user created successfully");
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
