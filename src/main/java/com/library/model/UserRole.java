package com.library.model;

/**
 * Enum representing user roles in the library system.
 * Used to distinguish between Student and Librarian access levels.
 */
public enum UserRole {
    STUDENT("Student"),
    LIBRARIAN("Librarian");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
