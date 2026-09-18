package com.library.model;

/**
 * Enum representing categories of books in the library.
 */
public enum BookCategory {
    SCIENCE("Science"),
    ENGINEERING("Engineering"),
    MATHEMATICS("Mathematics"),
    LITERATURE("Literature"),
    HISTORY("History"),
    COMPUTER_SCIENCE("Computer Science"),
    ECONOMICS("Economics"),
    GENERAL("General");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Convert a string to BookCategory (case-insensitive).
     * Returns GENERAL if no match found.
     */
    public static BookCategory fromString(String text) {
        for (BookCategory category : BookCategory.values()) {
            if (category.displayName.equalsIgnoreCase(text) || category.name().equalsIgnoreCase(text)) {
                return category;
            }
        }
        return GENERAL;
    }
}
