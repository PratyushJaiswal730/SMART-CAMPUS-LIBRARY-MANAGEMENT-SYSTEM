package com.library.thread;

import com.library.model.Book;
import com.library.exception.BookNotAvailableException;
import com.library.dao.BookDAO;

/**
 * Demonstrates thread-safe book issuance with synchronization.
 *
 * Scenario: Two students try to issue the same book concurrently.
 * Only ONE should succeed; the other should get BookNotAvailableException.
 *
 * Demonstrates:
 * - Runnable interface
 * - synchronized block (thread safety)
 * - Thread.sleep() for simulation
 * - Exception handling in threads
 */
public class BookIssueTask implements Runnable {

    private final int bookId;
    private final int studentId;
    private final String studentName;

    public BookIssueTask(int bookId, int studentId, String studentName) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    @Override
    public void run() {
        try {
            // Simulate some processing time
            Thread.sleep(100);

            // CRITICAL SECTION: Synchronized to ensure thread-safety
            synchronized (BookIssueTask.class) {
                // Re-check availability inside synchronized block
                Book book = BookDAO.getBookById(bookId);

                if (book == null) {
                    System.out.println("[" + studentName + "] Book ID " + bookId + " not found.");
                    return;
                }

                if (!book.isAvailable()) {
                    throw new BookNotAvailableException(
                        "Book ID " + bookId + " is already issued to another student.");
                }

                // Mark book as unavailable
                book.setAvailable(false);
                BookDAO.updateBook(book);

                System.out.println("[✓ " + studentName + "] Successfully issued Book ID " + bookId);
            }

        } catch (BookNotAvailableException e) {
            System.out.println("[✗ " + studentName + "] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[✗ " + studentName + "] Error: " + e.getMessage());
        }
    }
}
