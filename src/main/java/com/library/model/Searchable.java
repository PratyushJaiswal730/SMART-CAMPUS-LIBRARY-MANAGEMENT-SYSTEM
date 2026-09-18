package com.library.model;

/**
 * Interface for searchable entities in the library.
 * Demonstrates the use of interfaces in Java.
 */
public interface Searchable {

    /**
     * Check if this entity matches the given search keyword.
     * @param keyword the search term
     * @return true if the entity matches
     */
    boolean matchesSearch(String keyword);

    /**
     * Get a short summary of the entity for search results display.
     * @return summary string
     */
    String getSearchSummary();
}
