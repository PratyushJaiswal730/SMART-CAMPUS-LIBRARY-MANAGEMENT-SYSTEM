package com.library.exception;

/**
 * Thrown when a database operation fails.
 * Wraps underlying SQLExceptions with a user-friendly message.
 */
public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
