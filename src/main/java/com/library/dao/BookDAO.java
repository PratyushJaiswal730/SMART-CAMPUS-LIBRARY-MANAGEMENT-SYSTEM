package com.library.dao;

import com.library.model.Book;
import com.library.model.BookCategory;
import com.library.exception.DatabaseOperationException;
import com.library.jdbc.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Book entity.
 *
 * Demonstrates:
 * - JDBC PreparedStatement (parameterized queries)
 * - ResultSet processing
 * - CRUD operations (Create, Read, Update, Delete)
 * - Proper resource management (try-with-resources)
 * - Exception handling with custom exceptions
 */
public class BookDAO {

    /**
     * Insert a new book into the database.
     * @throws DatabaseOperationException if insert fails
     */
    public static void addBook(Book book) throws DatabaseOperationException {
        String sql = "INSERT INTO books (title, author, category, available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory().name());
            pstmt.setBoolean(4, book.isAvailable());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to add book: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve a book by ID.
     * @return Book if found, null otherwise
     */
    public static Book getBookById(int bookId) throws DatabaseOperationException {
        String sql = "SELECT book_id, title, author, category, available FROM books WHERE book_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve book: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Retrieve all books from the database.
     * Uses ArrayList to store results.
     */
    public static List<Book> getAllBooks() throws DatabaseOperationException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author, category, available FROM books ORDER BY book_id";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve books: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * Update a book's information.
     */
    public static void updateBook(Book book) throws DatabaseOperationException {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, available = ? WHERE book_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory().name());
            pstmt.setBoolean(4, book.isAvailable());
            pstmt.setInt(5, book.getBookId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update book: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a book from the database.
     */
    public static void deleteBook(int bookId) throws DatabaseOperationException {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete book: " + e.getMessage(), e);
        }
    }

    /**
     * Search books by title or author.
     * Uses ArrayList to store results.
     */
    public static List<Book> searchBooks(String keyword) throws DatabaseOperationException {
        List<Book> results = new ArrayList<>();
        String sql = "SELECT book_id, title, author, category, available FROM books " +
                     "WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? " +
                     "ORDER BY title";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + keyword.toLowerCase() + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to search books: " + e.getMessage(), e);
        }
        return results;
    }

    /**
     * Helper method to map a ResultSet row to a Book object.
     */
    private static Book mapResultSetToBook(ResultSet rs) throws SQLException {
        int bookId = rs.getInt("book_id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        BookCategory category = BookCategory.valueOf(rs.getString("category"));
        boolean available = rs.getBoolean("available");

        return new Book(bookId, title, author, category, available);
    }
}
