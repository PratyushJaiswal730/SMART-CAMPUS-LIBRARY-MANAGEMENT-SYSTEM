package com.library.jpa;

import jakarta.persistence.*;
import java.util.List;

/**
 * JPA manager for basic ORM operations.
 * Separate from main JDBC application — for demonstration.
 *
 * Demonstrates:
 * - EntityManager (persistence context)
 * - persist() — insert
 * - find() — select by ID
 * - merge() — update
 * - remove() — delete
 * - JPQL queries (Java Persistence Query Language)
 */
public class JpaBookManager {

    private static EntityManagerFactory emf = null;

    /**
     * Initialize the JPA context.
     */
    public static void init() {
        try {
            emf = Persistence.createEntityManagerFactory("libraryPU");
        } catch (Exception e) {
            System.err.println("JPA initialization failed: " + e.getMessage());
        }
    }

    /**
     * Get an EntityManager instance.
     */
    private static EntityManager getEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("JPA not initialized. Call init() first.");
        }
        return emf.createEntityManager();
    }

    /**
     * Insert a new book using JPA (persist).
     */
    public static void createBook(BookEntity book) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
            System.out.println("  [JPA] Book created: " + book.getTitle());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("  [JPA] Error creating book: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Find a book by ID using JPA (find).
     */
    public static BookEntity findBookById(int bookId) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BookEntity.class, bookId);
        } catch (Exception e) {
            System.err.println("  [JPA] Error finding book: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Update a book using JPA (merge).
     */
    public static void updateBook(BookEntity book) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
            System.out.println("  [JPA] Book updated: " + book.getTitle());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("  [JPA] Error updating book: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Delete a book using JPA (remove).
     */
    public static void deleteBook(int bookId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            BookEntity book = em.find(BookEntity.class, bookId);
            if (book != null) {
                em.remove(book);
                System.out.println("  [JPA] Book deleted: ID " + bookId);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("  [JPA] Error deleting book: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Find books by category using JPQL (Java Persistence Query Language).
     * Demonstrates query-based retrieval.
     */
    public static List<BookEntity> findBooksByCategory(String category) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT b FROM BookEntity b WHERE b.category = :category";
            Query query = em.createQuery(jpql);
            query.setParameter("category", category);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("  [JPA] Error searching books: " + e.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }

    /**
     * Get all available books using JPQL.
     */
    public static List<BookEntity> getAllAvailableBooks() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT b FROM BookEntity b WHERE b.available = true";
            Query query = em.createQuery(jpql);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("  [JPA] Error fetching available books: " + e.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }

    /**
     * Shutdown JPA context.
     */
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
