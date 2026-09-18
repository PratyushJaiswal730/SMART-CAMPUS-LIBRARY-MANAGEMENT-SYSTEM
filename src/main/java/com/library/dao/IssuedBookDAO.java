package com.library.dao;

import com.library.model.IssuedBook;
import com.library.exception.DatabaseOperationException;
import com.library.jdbc.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Data Access Object for IssuedBook entity.
 *
 * Demonstrates:
 * - JDBC operations
 * - Vector collection for activity/history data (thread-safe list)
 * - ResultSet processing
 */
public class IssuedBookDAO {

    /**
     * Issue a book to a student.
     */
    public static void issueBook(IssuedBook issuedBook) throws DatabaseOperationException {
        String sql = "INSERT INTO issued_books (book_id, student_id, issue_date, due_date) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, issuedBook.getBookId());
            pstmt.setInt(2, issuedBook.getStudentId());
            pstmt.setDate(3, java.sql.Date.valueOf(issuedBook.getIssueDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(issuedBook.getDueDate()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to issue book: " + e.getMessage(), e);
        }
    }

    /**
     * Return a book (mark as returned with return date).
     */
    public static void returnBook(int issueId, LocalDate returnDate) throws DatabaseOperationException {
        String sql = "UPDATE issued_books SET return_date = ? WHERE issue_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(returnDate));
            pstmt.setInt(2, issueId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to return book: " + e.getMessage(), e);
        }
    }

    /**
     * Get all currently issued books for a student (not yet returned).
     * Uses ArrayList to store results.
     */
    public static List<IssuedBook> getIssuedBooksByStudent(int studentId) throws DatabaseOperationException {
        List<IssuedBook> issued = new ArrayList<>();
        String sql = "SELECT ib.issue_id, ib.book_id, ib.student_id, ib.issue_date, ib.due_date, " +
                     "ib.return_date, b.title, s.name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN students s ON ib.student_id = s.student_id " +
                     "WHERE ib.student_id = ? AND ib.return_date IS NULL " +
                     "ORDER BY ib.issue_date DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    issued.add(mapResultSetToIssuedBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve issued books: " + e.getMessage(), e);
        }
        return issued;
    }

    /**
     * Get all issued books (including returned) as a Vector.
     * Demonstrates Vector usage — thread-safe alternative to ArrayList.
     */
    @SuppressWarnings("unchecked")
    public static Vector<IssuedBook> getAllIssuedBooksAsVector() throws DatabaseOperationException {
        Vector<IssuedBook> issued = new Vector<>();
        String sql = "SELECT ib.issue_id, ib.book_id, ib.student_id, ib.issue_date, ib.due_date, " +
                     "ib.return_date, b.title, s.name " +
                     "FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN students s ON ib.student_id = s.student_id " +
                     "ORDER BY ib.issue_date DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                issued.add(mapResultSetToIssuedBook(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve issued books: " + e.getMessage(), e);
        }
        return issued;
    }

    /**
     * Helper to map ResultSet to IssuedBook.
     */
    private static IssuedBook mapResultSetToIssuedBook(ResultSet rs) throws SQLException {
        IssuedBook ib = new IssuedBook();
        ib.setIssueId(rs.getInt("issue_id"));
        ib.setBookId(rs.getInt("book_id"));
        ib.setStudentId(rs.getInt("student_id"));
        ib.setIssueDate(rs.getDate("issue_date").toLocalDate());
        ib.setDueDate(rs.getDate("due_date").toLocalDate());

        Date returnDateSql = rs.getDate("return_date");
        if (returnDateSql != null) {
            ib.setReturnDate(returnDateSql.toLocalDate());
        }

        ib.setBookTitle(rs.getString("title"));
        ib.setStudentName(rs.getString("name"));

        return ib;
    }
}
