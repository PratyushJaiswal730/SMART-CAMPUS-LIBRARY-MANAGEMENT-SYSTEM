package com.library.jpa;

import jakarta.persistence.*;

/**
 * JPA Entity for Book.
 * This is a SEPARATE demonstration of JPA/ORM alongside JDBC.
 *
 * The main application uses JDBC with the Book model in com.library.model.
 * This entity demonstrates JPA annotations for educational purposes.
 *
 * Demonstrates:
 * - @Entity (marks as JPA entity)
 * - @Id (primary key)
 * - @Column (column mapping)
 * - Encapsulation with getters/setters
 * - Constructor
 */
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "category")
    private String category;

    @Column(name = "available")
    private boolean available = true;

    // Default constructor (required by JPA)
    public BookEntity() {
    }

    // Parameterized constructor
    public BookEntity(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = true;
    }

    // Getters and Setters
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", available=" + available +
                '}';
    }
}
