package com.library.service;

import com.library.model.Book;
import com.library.model.BookCategory;
import com.library.model.Student;
import com.library.model.IssuedBook;
import com.library.exception.DatabaseOperationException;
import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.dao.IssuedBookDAO;
import com.library.io.ActivityLogger;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Service layer for librarian operations.
 * Encapsulates business logic for admin/librarian features.
 */
public class LibrarianService {

    /**
     * Add a new book to the library.
     */
    public static void addBook(String title, String author, BookCategory category) throws DatabaseOperationException {
        Book book = new Book(0, title, author, category, true);
        BookDAO.addBook(book);
        ActivityLogger.logActivity("Book added: " + title + " by " + author);
    }

    /**
     * Remove a book from the library.
     */
    public static void removeBook(int bookId) throws DatabaseOperationException {
        BookDAO.deleteBook(bookId);
        ActivityLogger.logActivity("Book removed: Book ID " + bookId);
    }

    /**
     * Update book information.
     */
    public static void updateBook(Book book) throws DatabaseOperationException {
        BookDAO.updateBook(book);
        ActivityLogger.logActivity("Book updated: Book ID " + book.getBookId());
    }

    /**
     * Get all books in the library.
     */
    public static List<Book> getAllBooks() throws DatabaseOperationException {
        return BookDAO.getAllBooks();
    }

    /**
     * Search books by keyword.
     */
    public static List<Book> searchBooks(String keyword) throws DatabaseOperationException {
        return BookDAO.searchBooks(keyword);
    }

    /**
     * Get all students using a HashMap for fast lookup.
     * Demonstrates HashMap usage for statistics.
     */
    public static Map<Integer, Student> getAllStudentsAsMap() throws DatabaseOperationException {
        return StudentDAO.getAllStudentsAsMap();
    }

    /**
     * Get all students as a regular list.
     */
    public static List<Student> getAllStudents() throws DatabaseOperationException {
        return StudentDAO.getAllStudents();
    }

    /**
     * Get all issued books (using Vector for thread-safe access).
     * Demonstrates Vector usage.
     */
    @SuppressWarnings("unchecked")
    public static Vector<IssuedBook> getAllIssuedBooksAsVector() throws DatabaseOperationException {
        return IssuedBookDAO.getAllIssuedBooksAsVector();
    }

    /**
     * Calculate library statistics.
     * Returns statistics as a formatted string.
     */
    public static String getLibraryStatistics() throws DatabaseOperationException {
        List<Book> allBooks = getAllBooks();
        List<Student> allStudents = getAllStudents();
        Vector<IssuedBook> issuedBooks = getAllIssuedBooksAsVector();

        int totalBooks = allBooks.size();
        int availableBooks = (int) allBooks.stream().filter(Book::isAvailable).count();
        int issuedCount = (int) issuedBooks.stream().filter(b -> b.getReturnDate() == null).count();
        int totalStudents = allStudents.size();

        return String.format(
            "Total Books: %d | Available: %d | Currently Issued: %d | Total Students: %d",
            totalBooks, availableBooks, issuedCount, totalStudents
        );
    }

    /**
     * Generate activity report.
     */
    public static void generateActivityReport() {
        ActivityLogger.displayActivityLog();
    }
}
