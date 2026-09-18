package com.library.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized database configuration and connection management.
 *
 * All database credentials are configured here — not scattered across the project.
 * Uses JDBC with MySQL Connector/J (no JDBC-ODBC bridge).
 *
 * Demonstrates: Connection, DriverManager, centralized config.
 */
public class DatabaseConfig {

    // --- Database Configuration (change these to match your MySQL setup) ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";  // Change to your MySQL password

    // Connection pool is not used — keep it simple for a college project
    private static Connection connection = null;

    /**
     * Get a database connection. Creates a new one if not already connected.
     * @return active Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC driver (optional for modern JDBC, but explicit)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j to classpath.", e);
            }
        }
        return connection;
    }

    /**
     * Close the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test if the database is reachable.
     * @return true if connection succeeds
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
}
