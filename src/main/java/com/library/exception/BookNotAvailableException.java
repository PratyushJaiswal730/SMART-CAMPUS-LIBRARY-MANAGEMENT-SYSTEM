package com.library.exception;

/**
 * Thrown when a student tries to issue a book that is not available.
 */
public class BookNotAvailableException extends Exception {

    public BookNotAvailableException(String message) {
        super(message);
    }

    public BookNotAvailableException(int bookId) {
        super("Book with ID " + bookId + " is not available for issue.");
    }
}
