package com.library.model;

import java.time.LocalDate;

/**
 * Represents a book issued to a student.
 * Maps to the issued_books table in the database.
 */
public class IssuedBook {

    private int issueId;
    private int bookId;
    private int studentId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;  // null if not yet returned
    private String bookTitle;      // for display convenience
    private String studentName;    // for display convenience

    public IssuedBook() {}

    public IssuedBook(int issueId, int bookId, int studentId,
                      LocalDate issueDate, LocalDate dueDate) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.studentId = studentId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    // --- Getters and Setters ---

    public int getIssueId() { return issueId; }
    public void setIssueId(int issueId) { this.issueId = issueId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    @Override
    public String toString() {
        return String.format("Issue#%d | Book: %d (%s) | Student: %d (%s) | Issued: %s | Due: %s | Returned: %s",
            issueId, bookId, bookTitle != null ? bookTitle : "-",
            studentId, studentName != null ? studentName : "-",
            issueDate, dueDate, returnDate != null ? returnDate.toString() : "Not returned");
    }
}
