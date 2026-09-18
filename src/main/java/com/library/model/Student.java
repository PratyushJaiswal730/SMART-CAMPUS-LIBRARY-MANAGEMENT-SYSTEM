package com.library.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Student model extending User.
 *
 * Demonstrates:
 * - Inheritance (extends User)
 * - super keyword (calling parent constructor)
 * - Method overriding (getDisplayInfo)
 * - Encapsulation
 * - ArrayList usage (issuedBookIds)
 * - Searchable interface implementation
 * - FineCalculable interface implementation
 */
public class Student extends User implements Searchable, FineCalculable {

    private String department;
    private String password;
    private List<Integer> issuedBookIds;  // ArrayList of currently issued book IDs

    /**
     * Full constructor.
     */
    public Student(int id, String name, String email, String department, String password) {
        super(id, name, email, UserRole.STUDENT);  // super: calls User constructor
        this.department = department;
        this.password = password;
        this.issuedBookIds = new ArrayList<>();
    }

    /**
     * Overloaded constructor — without password (for display purposes).
     * Demonstrates method overloading (constructor overloading).
     */
    public Student(int id, String name, String email, String department) {
        super(id, name, email, UserRole.STUDENT);
        this.department = department;
        this.password = "";
        this.issuedBookIds = new ArrayList<>();
    }

    // --- Getters and Setters ---

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getIssuedBookIds() {
        return issuedBookIds;
    }

    public void setIssuedBookIds(List<Integer> issuedBookIds) {
        this.issuedBookIds = issuedBookIds;
    }

    public void addIssuedBook(int bookId) {
        this.issuedBookIds.add(bookId);
    }

    public void removeIssuedBook(int bookId) {
        this.issuedBookIds.remove(Integer.valueOf(bookId));
    }

    // --- Abstract method implementation (Method Overriding) ---

    @Override
    public String getDisplayInfo() {
        return String.format(
            "Student ID: %d | Name: %s | Email: %s | Dept: %s | Books Issued: %d",
            getId(), getName(), getEmail(), department, issuedBookIds.size()
        );
    }

    // --- Searchable interface ---

    @Override
    public boolean matchesSearch(String keyword) {
        String lower = keyword.toLowerCase();
        return getName().toLowerCase().contains(lower)
            || getEmail().toLowerCase().contains(lower)
            || department.toLowerCase().contains(lower)
            || String.valueOf(getId()).equals(keyword);
    }

    @Override
    public String getSearchSummary() {
        return "Student: " + getName() + " (" + department + ")";
    }

    // --- FineCalculable interface ---

    @Override
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) return 0.0;
        return daysOverdue * FINE_PER_DAY;
    }

    @Override
    public boolean isOverdue(int borrowedDays) {
        return borrowedDays > MAX_BORROW_DAYS;
    }
}
