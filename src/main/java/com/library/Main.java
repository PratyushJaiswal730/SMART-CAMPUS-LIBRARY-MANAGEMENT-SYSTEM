package com.library;

import com.library.model.*;
import com.library.service.StudentService;
import com.library.service.LibrarianService;
import com.library.exception.*;
import com.library.dao.StudentDAO;
import com.library.dao.LibrarianDAO;
import com.library.jdbc.DatabaseConfig;
import com.library.util.InputValidator;
import com.library.util.OperationHistory;
import com.library.io.ActivityLogger;
import com.library.thread.BookIssueTask;
import com.library.jpa.JpaBookManager;

import java.util.List;

/**
 * Main application entry point.
 * Menu-driven console application for Smart Campus Library Management System.
 */
public class Main {

    private static User currentUser = null;
    private static boolean running = true;

    public static void main(String[] args) {
        try {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║    SMART CAMPUS LIBRARY MANAGEMENT SYSTEM                   ║");
            System.out.println("║    Version 1.0                                              ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");

            // Test database connection
            if (!DatabaseConfig.testConnection()) {
                System.err.println("\n✗ Failed to connect to database.");
                System.err.println("  Ensure MySQL is running and 'campus_library' database exists.");
                System.err.println("  See database/schema.sql for setup instructions.");
                System.exit(1);
            }

            System.out.println("✓ Connected to database.\n");

            // Initialize JPA
            JpaBookManager.init();

            // Main login loop
            while (running) {
                showLoginMenu();
                if (currentUser != null) {
                    if (currentUser instanceof Student) {
                        studentMenu((Student) currentUser);
                    } else if (currentUser instanceof Librarian) {
                        librarianMenu((Librarian) currentUser);
                    }
                    currentUser = null;
                }
            }

            System.out.println("\n✓ Thank you for using Smart Campus Library Management System.");
            System.out.println("  Goodbye!\n");

        } catch (Exception e) {
            System.err.println("\n✗ Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection();
            JpaBookManager.shutdown();
        }
    }

    /**
     * Display login menu and handle authentication.
     * Demonstrates polymorphism: User reference pointing to Student/Librarian.
     */
    private static void showLoginMenu() {
        System.out.println("\n┌─ LOGIN ─────────────────────────────────────┐");
        System.out.println("│ 1. Student Login                            │");
        System.out.println("│ 2. Student Registration                     │");
        System.out.println("│ 3. Librarian Login                          │");
        System.out.println("│ 4. Run Multithreading Demo                  │");
        System.out.println("│ 5. JPA Demonstration                        │");
        System.out.println("│ 0. Exit                                     │");
        System.out.println("└─────────────────────────────────────────────┘");

        int choice = InputValidator.readInt("Enter choice: ");

        switch (choice) {
            case 1:
                studentLogin();
                break;
            case 2:
                studentRegistration();
                break;
            case 3:
                librarianLogin();
                break;
            case 4:
                runMultithreadingDemo();
                break;
            case 5:
                runJpaDemo();
                break;
            case 0:
                running = false;
                break;
            default:
                System.out.println("✗ Invalid choice.");
        }
    }

    /**
     * Handle student login with validation.
     */
    private static void studentLogin() {
        try {
            System.out.println("\n--- Student Login ---");
            int studentId = InputValidator.readInt("Enter Student ID: ");
            String password = InputValidator.readString("Enter Password: ");

            Student student = StudentDAO.loginStudent(studentId, password);
            if (student != null) {
                System.out.println("✓ Login successful. Welcome, " + student.getName() + "!");
                currentUser = student;  // Polymorphism: User reference
                OperationHistory.push("Student " + studentId + " logged in");
                ActivityLogger.logActivity("Student login: ID " + studentId);
            } else {
                System.out.println("✗ Invalid credentials.");
            }
        } catch (DatabaseOperationException e) {
            System.err.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ Error during login: " + e.getMessage());
        }
    }

    /**
     * Handle student registration.
     */
    private static void studentRegistration() {
        try {
            System.out.println("\n--- Student Registration ---");
            String name = InputValidator.readString("Enter Name: ");
            String email = InputValidator.readEmail("Enter Email: ");
            String department = InputValidator.readString("Enter Department: ");
            String password = InputValidator.readString("Enter Password: ");

            Student student = new Student(0, name, email, department, password);
            StudentDAO.registerStudent(student);

            System.out.println("✓ Registration successful!");
            OperationHistory.push("New student registered: " + name);
            ActivityLogger.logActivity("Student registered: " + name);
        } catch (DatabaseOperationException e) {
            System.err.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ Error during registration: " + e.getMessage());
        }
    }

    /**
     * Handle librarian login.
     */
    private static void librarianLogin() {
        try {
            System.out.println("\n--- Librarian Login ---");
            String username = InputValidator.readString("Enter Username: ");
            String password = InputValidator.readString("Enter Password: ");

            Librarian librarian = LibrarianDAO.loginLibrarian(username, password);
            if (librarian != null) {
                System.out.println("✓ Login successful. Welcome, " + librarian.getName() + "!");
                currentUser = librarian;  // Polymorphism: User reference
                OperationHistory.push("Librarian " + username + " logged in");
                ActivityLogger.logActivity("Librarian login: " + username);
            } else {
                System.out.println("✗ Invalid credentials.");
            }
        } catch (DatabaseOperationException e) {
            System.err.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ Error during login: " + e.getMessage());
        }
    }

    /**
     * Student menu after login.
     */
    private static void studentMenu(Student student) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌─ STUDENT MENU ──────────────────────────────┐");
            System.out.println("│ 1. Search Books                             │");
            System.out.println("│ 2. View Available Books                     │");
            System.out.println("│ 3. Issue Book                               │");
            System.out.println("│ 4. Return Book                              │");
            System.out.println("│ 5. View My Issued Books                     │");
            System.out.println("│ 6. Check Fine                               │");
            System.out.println("│ 7. View My Profile                          │");
            System.out.println("│ 0. Logout                                   │");
            System.out.println("└─────────────────────────────────────────────┘");

            int choice = InputValidator.readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1:
                        searchBooks();
                        break;
                    case 2:
                        viewAvailableBooks();
                        break;
                    case 3:
                        issueBook(student);
                        break;
                    case 4:
                        returnBook(student);
                        break;
                    case 5:
                        viewIssuedBooks(student);
                        break;
                    case 6:
                        checkFine(student);
                        break;
                    case 7:
                        viewStudentProfile(student);
                        break;
                    case 0:
                        System.out.println("✓ Logged out successfully.");
                        inMenu = false;
                        break;
                    default:
                        System.out.println("✗ Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("✗ " + e.getMessage());
            }
        }
    }

    /**
     * Librarian menu after login.
     */
    private static void librarianMenu(Librarian librarian) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n┌─ LIBRARIAN MENU ────────────────────────────┐");
            System.out.println("│ 1. Add Book                                 │");
            System.out.println("│ 2. Remove Book                              │");
            System.out.println("│ 3. Update Book                              │");
            System.out.println("│ 4. Search Books                             │");
            System.out.println("│ 5. View All Books                           │");
            System.out.println("│ 6. View All Students                        │");
            System.out.println("│ 7. View Issued Books                        │");
            System.out.println("│ 8. Library Statistics                       │");
            System.out.println("│ 9. Activity Report                          │");
            System.out.println("│ 0. Logout                                   │");
            System.out.println("└─────────────────────────────────────────────┘");

            int choice = InputValidator.readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        removeBook();
                        break;
                    case 3:
                        updateBook();
                        break;
                    case 4:
                        searchBooks();
                        break;
                    case 5:
                        viewAllBooks();
                        break;
                    case 6:
                        viewAllStudents();
                        break;
                    case 7:
                        viewAllIssuedBooks();
                        break;
                    case 8:
                        viewStatistics();
                        break;
                    case 9:
                        viewActivityReport();
                        break;
                    case 0:
                        System.out.println("✓ Logged out successfully.");
                        inMenu = false;
                        break;
                    default:
                        System.out.println("✗ Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("✗ " + e.getMessage());
            }
        }
    }

    // ═══════════════════ STUDENT OPERATIONS ═══════════════════

    private static void searchBooks() throws DatabaseOperationException {
        String keyword = InputValidator.readString("Enter book title or author: ");
        List<Book> results = StudentService.searchBooks(keyword);

        if (results.isEmpty()) {
            System.out.println("  No books found.");
            return;
        }

        System.out.println("\n--- Search Results ---");
        for (Book book : results) {
            System.out.println("  " + book.getSearchSummary());
        }
    }

    private static void viewAvailableBooks() throws DatabaseOperationException {
        List<Book> books = StudentService.getAvailableBooks();

        if (books.isEmpty()) {
            System.out.println("  No available books at the moment.");
            return;
        }

        System.out.println("\n--- Available Books ---");
        for (Book book : books) {
            System.out.println("  " + book.getSearchSummary());
        }
    }

    private static void issueBook(Student student) throws DatabaseOperationException {
        int bookId = InputValidator.readInt("Enter Book ID to issue: ");

        try {
            StudentService.issueBook(student, bookId);
            System.out.println("✓ Book issued successfully. Due date: " + java.time.LocalDate.now().plusDays(14));
        } catch (InvalidBookException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (BookNotAvailableException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void returnBook(Student student) throws DatabaseOperationException {
        List<IssuedBook> issued = StudentService.getIssuedBooks(student.getId());

        if (issued.isEmpty()) {
            System.out.println("  You have no issued books.");
            return;
        }

        System.out.println("\n--- Your Issued Books ---");
        for (IssuedBook book : issued) {
            System.out.println("  Issue #" + book.getIssueId() + ": Book " + book.getBookId() + " - " + book.getBookTitle());
        }

        int issueId = InputValidator.readInt("Enter Issue ID to return: ");
        int bookId = InputValidator.readInt("Enter Book ID: ");

        try {
            StudentService.returnBook(issueId, bookId);
            System.out.println("✓ Book returned successfully.");
        } catch (InvalidBookException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private static void viewIssuedBooks(Student student) throws DatabaseOperationException {
        List<IssuedBook> issued = StudentService.getIssuedBooks(student.getId());

        if (issued.isEmpty()) {
            System.out.println("  You have no issued books.");
            return;
        }

        System.out.println("\n--- Your Issued Books ---");
        for (IssuedBook book : issued) {
            System.out.println("  " + book.toString());
        }
    }

    private static void checkFine(Student student) throws DatabaseOperationException {
        double fine = StudentService.calculateFine(student);
        if (fine > 0) {
            System.out.println("\n--- Fine Summary ---");
            System.out.println("  Total Fine: Rs. " + fine);
        } else {
            System.out.println("  You have no outstanding fines.");
        }
    }

    private static void viewStudentProfile(Student student) {
        System.out.println("\n--- Your Profile ---");
        System.out.println("  " + student.getDisplayInfo());
    }

    // ═══════════════════ LIBRARIAN OPERATIONS ═══════════════════

    private static void addBook() throws DatabaseOperationException {
        String title = InputValidator.readString("Enter Book Title: ");
        String author = InputValidator.readString("Enter Author Name: ");

        System.out.println("\nCategories: ");
        for (BookCategory cat : BookCategory.values()) {
            System.out.println("  - " + cat.getDisplayName());
        }
        String categoryStr = InputValidator.readString("Enter Category: ");
        BookCategory category = BookCategory.fromString(categoryStr);

        LibrarianService.addBook(title, author, category);
        System.out.println("✓ Book added successfully.");
    }

    private static void removeBook() throws DatabaseOperationException {
        int bookId = InputValidator.readInt("Enter Book ID to remove: ");
        LibrarianService.removeBook(bookId);
        System.out.println("✓ Book removed successfully.");
    }

    private static void updateBook() throws DatabaseOperationException {
        int bookId = InputValidator.readInt("Enter Book ID: ");
        Book book = com.library.dao.BookDAO.getBookById(bookId);

        if (book == null) {
            System.out.println("✗ Book not found.");
            return;
        }

        String title = InputValidator.readString("Enter new title (current: " + book.getTitle() + "): ");
        String author = InputValidator.readString("Enter new author (current: " + book.getAuthor() + "): ");

        book.setTitle(title);
        book.setAuthor(author);
        LibrarianService.updateBook(book);
        System.out.println("✓ Book updated successfully.");
    }

    private static void viewAllBooks() throws DatabaseOperationException {
        List<Book> books = LibrarianService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("  No books in library.");
            return;
        }

        System.out.println("\n--- All Books ---");
        for (Book book : books) {
            System.out.println("  " + book.getSearchSummary());
        }
    }

    private static void viewAllStudents() throws DatabaseOperationException {
        List<Student> students = LibrarianService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("  No students registered.");
            return;
        }

        System.out.println("\n--- All Students ---");
        for (Student student : students) {
            System.out.println("  " + student.getDisplayInfo());
        }
    }

    private static void viewAllIssuedBooks() throws DatabaseOperationException {
        List<IssuedBook> issued = (List<IssuedBook>) LibrarianService.getAllIssuedBooksAsVector();

        if (issued.isEmpty()) {
            System.out.println("  No issued books.");
            return;
        }

        System.out.println("\n--- All Issued Books (Active) ---");
        for (IssuedBook book : issued) {
            if (book.getReturnDate() == null) {  // Only show active issues
                System.out.println("  " + book.toString());
            }
        }
    }

    private static void viewStatistics() throws DatabaseOperationException {
        System.out.println("\n--- Library Statistics ---");
        System.out.println("  " + LibrarianService.getLibraryStatistics());
    }

    private static void viewActivityReport() {
        ActivityLogger.displayActivityLog();
    }

    // ═══════════════════ DEMO OPERATIONS ═══════════════════

    /**
     * Demonstrate multithreading with concurrent book issue attempts.
     * Shows thread safety with synchronized blocks.
     */
    private static void runMultithreadingDemo() {
        System.out.println("\n--- Multithreading Demo: Concurrent Book Issue ---");
        System.out.println("Simulating 3 students trying to issue the same book simultaneously...\n");

        try {
            // Ensure book exists and is available
            Book testBook = new Book(999, "Demo Book", "Demo Author", BookCategory.GENERAL, true);
            com.library.dao.BookDAO.addBook(testBook);

            // Create 3 threads representing students issuing the same book
            Thread student1 = new Thread(new BookIssueTask(999, 101, "Student-1"));
            Thread student2 = new Thread(new BookIssueTask(999, 102, "Student-2"));
            Thread student3 = new Thread(new BookIssueTask(999, 103, "Student-3"));

            // Start all threads
            student1.start();
            student2.start();
            student3.start();

            // Wait for all threads to complete
            student1.join();
            student2.join();
            student3.join();

            System.out.println("\n✓ Demo complete: Only one student should have issued the book.");

            // Cleanup
            com.library.dao.BookDAO.deleteBook(999);

        } catch (Exception e) {
            System.err.println("✗ Demo error: " + e.getMessage());
        }
    }

    /**
     * Demonstrate JPA/Hibernate operations.
     * Shows CRUD operations with EntityManager and JPQL.
     */
    private static void runJpaDemo() {
        System.out.println("\n--- JPA/Hibernate Demonstration ---");
        System.out.println("Running CRUD operations using JPA instead of JDBC...\n");

        try {
            // CREATE: Persist a new book
            System.out.println("1. Creating a book with JPA persist()...");
            com.library.jpa.BookEntity newBook = new com.library.jpa.BookEntity(
                "JPA Demo Book", "ORM Author", "COMPUTER_SCIENCE"
            );
            JpaBookManager.createBook(newBook);

            // READ: Find by ID (after JPA auto-increments, we search)
            System.out.println("\n2. Searching books by category using JPQL...");
            List<com.library.jpa.BookEntity> csBooks = JpaBookManager.findBooksByCategory("COMPUTER_SCIENCE");
            System.out.println("  Found " + csBooks.size() + " Computer Science book(s)");

            // UPDATE: Merge changes
            if (!csBooks.isEmpty()) {
                System.out.println("\n3. Updating a book with JPA merge()...");
                com.library.jpa.BookEntity bookToUpdate = csBooks.get(0);
                bookToUpdate.setTitle("Updated: " + bookToUpdate.getTitle());
                JpaBookManager.updateBook(bookToUpdate);
            }

            // JPQL Query: Get all available books
            System.out.println("\n4. Querying available books with JPQL...");
            List<com.library.jpa.BookEntity> available = JpaBookManager.getAllAvailableBooks();
            System.out.println("  Found " + available.size() + " available book(s)");

            System.out.println("\n✓ JPA demo complete.");

        } catch (Exception e) {
            System.err.println("✗ JPA demo error: " + e.getMessage());
        }
    }
}
