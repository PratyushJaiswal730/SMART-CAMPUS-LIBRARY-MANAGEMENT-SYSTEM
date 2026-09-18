package com.library.model;

/**
 * Abstract base class for all users in the library system.
 *
 * Demonstrates:
 * - Abstraction (abstract class and method)
 * - Encapsulation (private fields, public getters/setters)
 * - Constructors, this keyword
 * - Method that subclasses must override
 */
public abstract class User {

    private int id;
    private String name;
    private String email;
    private final UserRole role;  // final: role cannot change once assigned

    /**
     * Parameterized constructor.
     * @param id   user ID
     * @param name user name
     * @param email user email
     * @param role user role (STUDENT or LIBRARIAN)
     */
    public User(int id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // --- Getters and Setters (Encapsulation) ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public final UserRole getRole() {
        return role;
    }

    /**
     * Abstract method — each subclass must provide its own display format.
     * Demonstrates abstraction.
     */
    public abstract String getDisplayInfo();

    /**
     * Common method available to all users.
     * Demonstrates method that can be inherited directly.
     */
    public String getRoleName() {
        return role.getDisplayName();
    }

    @Override
    public String toString() {
        return role.getDisplayName() + " [ID=" + id + ", Name=" + name + "]";
    }
}
