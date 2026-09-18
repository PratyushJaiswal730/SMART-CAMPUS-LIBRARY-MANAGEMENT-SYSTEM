package com.library.model;

import java.time.LocalDate;

/**
 * Book model implementing Searchable interface.
 *
 * Demonstrates:
 * - Encapsulation (private fields, getters/setters)
 * - Enum usage (BookCategory)
 * - Interface implementation (Searchable)
 * - Constructor overloading
 */
public class Book implements Searchable {

    private int bookId;
    private String title;
    private String author;
    private BookCategory category;
    private boolean available;
    private LocalDate issueDate;
    private LocalDate returnDate;

    /**
     * Full constructor.
     */
    public Book(int bookId, String title, String author, BookCategory category, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = available;
    }

    /**
     * Overloaded constructor — new book is available by default.
     * Demonstrates constructor overloading.
     */
    public Book(int bookId, String title, String author, BookCategory category) {
        this(bookId, title, author, category, true);  // this(): calls another constructor
    }

    /**
     * Overloaded constructor — minimal info, defaults to GENERAL category.
     */
    public Book(int bookId, String title, String author) {
        this(bookId, title, author, BookCategory.GENERAL, true);
    }

    // --- Getters and Setters ---

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // --- Searchable interface implementation ---

    @Override
    public boolean matchesSearch(String keyword) {
        String lower = keyword.toLowerCase();
        return title.toLowerCase().contains(lower)
            || author.toLowerCase().contains(lower)
            || category.getDisplayName().toLowerCase().contains(lower)
            || String.valueOf(bookId).equals(keyword);
    }

    @Override
    public String getSearchSummary() {
        return String.format("Book ID: %d | %s by %s | %s | %s",
            bookId, title, author, category.getDisplayName(),
            available ? "Available" : "Issued");
    }

    @Override
    public String toString() {
        return getSearchSummary();
    }
}
