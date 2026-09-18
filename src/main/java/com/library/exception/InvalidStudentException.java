package com.library.exception;

/**
 * Thrown when an invalid student ID is provided or the student does not exist.
 */
public class InvalidStudentException extends Exception {

    public InvalidStudentException(String message) {
        super(message);
    }

    public InvalidStudentException(int studentId) {
        super("Invalid student: no student found with ID " + studentId);
    }
}
