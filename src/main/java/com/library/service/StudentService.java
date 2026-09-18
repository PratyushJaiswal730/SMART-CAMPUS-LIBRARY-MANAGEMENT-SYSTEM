package com.library.service;

import com.library.model.Book;
import com.library.model.Student;
import com.library.model.IssuedBook;
import com.library.exception.*;
import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.dao.IssuedBookDAO;
import com.library.io.ActivityLogger;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service layer for student operations.
 * Encapsulates business logic for student-related features.
 */
public class StudentService {

    /**
     * Issue a book to a student.
     * Demonstrates exception throwing and handling.
     */
    public static void issueBook(Student student, int bookId) throws InvalidBookException, BookNotAvailableException, DatabaseOperationException {
        // Validate book exists
        Book book = BookDAO.getBookById(bookId);
        if (book == null) {
            throw new InvalidBookException(bookId);
        }

        // Check availability
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(bookId);
        }

        // Mark book as unavailable
        book.setAvailable(false);
        book.setIssueDate(LocalDate.now());
        BookDAO.updateBook(book);

        // Create issued book record (14-day borrowing period)
        IssuedBook issued = new IssuedBook(
            0,
            bookId,
            student.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );
        IssuedBookDAO.issueBook(issued);

        // Log activity
        ActivityLogger.logActivity("Book issued to Student " + student.getId() + ": Book ID " + bookId);
    }

    /**
     * Return an issued book.
     */
    public static void returnBook(int issueId, int bookId) throws InvalidBookException, DatabaseOperationException {
        Book book = BookDAO.getBookById(bookId);
        if (book == null) {
            throw new InvalidBookException(bookId);
        }

        // Mark book as available
        book.setAvailable(true);
        book.setReturnDate(LocalDate.now());
        BookDAO.updateBook(book);

        // Update issued book record
        IssuedBookDAO.returnBook(issueId, LocalDate.now());

        ActivityLogger.logActivity("Book returned: Book ID " + bookId);
    }

    /**
     * Get all currently issued books for a student.
     */
    public static List<IssuedBook> getIssuedBooks(int studentId) throws DatabaseOperationException {
        return IssuedBookDAO.getIssuedBooksByStudent(studentId);
    }

    /**
     * Calculate fine for overdue books.
     * Demonstrates the FineCalculable interface implementation.
     */
    public static double calculateFine(Student student) throws DatabaseOperationException {
        List<IssuedBook> issued = getIssuedBooks(student.getId());
        double totalFine = 0;

        for (IssuedBook book : issued) {
            if (book.getReturnDate() == null) {  // Not yet returned
                LocalDate dueDate = book.getDueDate();
                LocalDate today = LocalDate.now();

                if (today.isAfter(dueDate)) {
                    long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
                    double fine = student.calculateFine((int) daysOverdue);
                    totalFine += fine;
                }
            }
        }

        return totalFine;
    }

    /**
     * Search books by keyword.
     */
    public static List<Book> searchBooks(String keyword) throws DatabaseOperationException {
        return BookDAO.searchBooks(keyword);
    }

    /**
     * Get all available books.
     */
    public static List<Book> getAvailableBooks() throws DatabaseOperationException {
        List<Book> books = BookDAO.getAllBooks();
        // Filter in-memory (could also do in SQL)
        books.removeIf(book -> !book.isAvailable());
        return books;
    }
}
