package com.library.model;

/**
 * Librarian model extending User.
 *
 * Demonstrates:
 * - Inheritance (extends User)
 * - super keyword
 * - Method overriding (getDisplayInfo)
 * - Encapsulation
 */
public class Librarian extends User {

    private String username;
    private String password;

    /**
     * Full constructor.
     */
    public Librarian(int id, String name, String email, String username, String password) {
        super(id, name, email, UserRole.LIBRARIAN);  // super: calls User constructor
        this.username = username;
        this.password = password;
    }

    // --- Getters and Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- Method Overriding ---

    @Override
    public String getDisplayInfo() {
        return String.format(
            "Librarian ID: %d | Name: %s | Email: %s | Username: %s",
            getId(), getName(), getEmail(), username
        );
    }
}
