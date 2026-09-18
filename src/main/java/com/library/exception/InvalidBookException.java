package com.library.exception;

/**
 * Thrown when an invalid book ID is provided or the book does not exist.
 */
public class InvalidBookException extends Exception {

    public InvalidBookException(String message) {
        super(message);
    }

    public InvalidBookException(int bookId) {
        super("Invalid book: no book found with ID " + bookId);
    }
}
