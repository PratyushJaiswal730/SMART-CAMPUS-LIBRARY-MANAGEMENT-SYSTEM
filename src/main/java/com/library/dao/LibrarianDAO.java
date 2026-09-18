package com.library.dao;

import com.library.model.Librarian;
import com.library.exception.DatabaseOperationException;
import com.library.jdbc.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Librarian entity.
 */
public class LibrarianDAO {

    /**
     * Authenticate librarian login.
     */
    public static Librarian loginLibrarian(String username, String password) throws DatabaseOperationException {
        String sql = "SELECT librarian_id, name, email, username, password FROM librarians " +
                     "WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibrarian(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to authenticate librarian: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Get librarian by ID.
     */
    public static Librarian getLibrarianById(int librarianId) throws DatabaseOperationException {
        String sql = "SELECT librarian_id, name, email, username, password FROM librarians WHERE librarian_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, librarianId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibrarian(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve librarian: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Helper to map ResultSet to Librarian.
     */
    private static Librarian mapResultSetToLibrarian(ResultSet rs) throws SQLException {
        int librarianId = rs.getInt("librarian_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String username = rs.getString("username");
        String password = rs.getString("password");

        return new Librarian(librarianId, name, email, username, password);
    }
}
