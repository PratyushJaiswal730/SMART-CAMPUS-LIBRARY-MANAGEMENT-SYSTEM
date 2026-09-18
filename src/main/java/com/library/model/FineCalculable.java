package com.library.model;

/**
 * Interface for calculating fines on overdue books.
 * Demonstrates the use of interfaces in Java.
 */
public interface FineCalculable {

    /** Fine rate: Rs. 2 per day overdue */
    final double FINE_PER_DAY = 2.0;

    /** Maximum borrowing period in days */
    final int MAX_BORROW_DAYS = 14;

    /**
     * Calculate the fine for an overdue book.
     * @param daysOverdue number of days past the due date
     * @return fine amount in rupees
     */
    double calculateFine(int daysOverdue);

    /**
     * Check if a book is overdue.
     * @param borrowedDays total days since the book was issued
     * @return true if the book is overdue
     */
    boolean isOverdue(int borrowedDays);
}
