-- ============================================================
-- SMART CAMPUS LIBRARY MANAGEMENT SYSTEM
-- Database Schema (MySQL)
-- ============================================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS campus_library;
USE campus_library;

-- ============================================================
-- STUDENTS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- LIBRARIANS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS librarians (
    librarian_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- BOOKS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- ISSUED_BOOKS TABLE
-- Maps students to books they have issued
-- ============================================================
CREATE TABLE IF NOT EXISTS issued_books (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    student_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TRANSACTIONS TABLE (for record keeping)
-- ============================================================
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    user_type VARCHAR(20),  -- 'STUDENT' or 'LIBRARIAN'
    action VARCHAR(100),
    details VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- INDEXES for Performance
-- ============================================================
CREATE INDEX idx_student_email ON students(email);
CREATE INDEX idx_librarian_username ON librarians(username);
CREATE INDEX idx_book_title ON books(title);
CREATE INDEX idx_book_author ON books(author);
CREATE INDEX idx_issued_student ON issued_books(student_id);
CREATE INDEX idx_issued_book ON issued_books(book_id);
CREATE INDEX idx_issued_return ON issued_books(return_date);

-- ============================================================
-- SAMPLE DATA (Optional)
-- ============================================================

-- Insert sample librarian
INSERT INTO librarians (name, email, username, password) VALUES
    ('Admin User', 'admin@library.edu', 'admin', 'admin123');

-- Insert sample students
INSERT INTO students (name, email, department, password) VALUES
    ('Rahul Kumar', 'rahul@student.edu', 'Computer Science', 'pass123'),
    ('Priya Sharma', 'priya@student.edu', 'Information Technology', 'pass456'),
    ('Arjun Singh', 'arjun@student.edu', 'Electronics', 'pass789');

-- Insert sample books
INSERT INTO books (title, author, category, available) VALUES
    ('Introduction to Java', 'Herbert Schildt', 'COMPUTER_SCIENCE', TRUE),
    ('Data Structures', 'Mark Allen Weiss', 'COMPUTER_SCIENCE', TRUE),
    ('Discrete Mathematics', 'Kenneth H. Rosen', 'MATHEMATICS', TRUE),
    ('The C Programming Language', 'Brian W. Kernighan', 'COMPUTER_SCIENCE', TRUE),
    ('Modern Operating Systems', 'Andrew S. Tanenbaum', 'COMPUTER_SCIENCE', TRUE),
    ('Design Patterns', 'Gang of Four', 'COMPUTER_SCIENCE', TRUE),
    ('Calculus', 'James Stewart', 'MATHEMATICS', TRUE),
    ('Physics Principles', 'David Halliday', 'SCIENCE', TRUE);

-- ============================================================
-- VIEWS (Optional, for easier reporting)
-- ============================================================

-- View: Currently issued books with student and book details
CREATE OR REPLACE VIEW v_active_issues AS
SELECT
    ib.issue_id,
    s.student_id,
    s.name AS student_name,
    b.book_id,
    b.title AS book_title,
    ib.issue_date,
    ib.due_date,
    DATEDIFF(CURDATE(), ib.due_date) AS days_overdue
FROM issued_books ib
JOIN students s ON ib.student_id = s.student_id
JOIN books b ON ib.book_id = b.book_id
WHERE ib.return_date IS NULL
ORDER BY ib.due_date ASC;

-- View: Library statistics
CREATE OR REPLACE VIEW v_library_stats AS
SELECT
    (SELECT COUNT(*) FROM books) AS total_books,
    (SELECT COUNT(*) FROM books WHERE available = TRUE) AS available_books,
    (SELECT COUNT(*) FROM books WHERE available = FALSE) AS issued_books,
    (SELECT COUNT(*) FROM students) AS total_students,
    (SELECT COUNT(DISTINCT student_id) FROM issued_books WHERE return_date IS NULL) AS active_borrowers;

-- ============================================================
-- End of Schema
-- ============================================================
