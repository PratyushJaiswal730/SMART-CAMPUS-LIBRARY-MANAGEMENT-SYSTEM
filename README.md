# Smart Campus Library Management System

A console-based library management system for a college campus, built with Java to demonstrate core programming concepts from a college Java syllabus.

## Project Overview

This project implements a complete library management system allowing students and librarians to interact with a digital library. Students can search, issue, and return books. Librarians can manage the catalog, view statistics, and generate activity reports.

**Technology Stack:**
- Java 17 (with Maven)
- MySQL 8.0+
- JDBC for database operations
- JPA/Hibernate for ORM (demonstration)
- Collections Framework (ArrayList, HashMap, Vector, Stack)
- Multithreading with synchronization

## Features

### Student Features
1. **Student Registration** - Create new student account
2. **Student Login** - Authenticate with credentials
3. **Search Books** - Find books by title, author, or category
4. **View Available Books** - Browse currently available books
5. **Issue Book** - Borrow a book from the library
6. **Return Book** - Return a previously issued book
7. **View Issued Books** - See list of currently borrowed books
8. **Check Fine** - Calculate and view overdue fines
9. **View Profile** - See personal information

### Librarian Features
1. **Librarian Login** - Authenticate with username/password
2. **Add Book** - Add new book to catalog
3. **Remove Book** - Delete book from catalog
4. **Update Book** - Modify book information
5. **Search Books** - Find books in catalog
6. **View All Books** - Display complete book inventory
7. **View All Students** - List all registered students
8. **View Issued Books** - See active book issues
9. **Library Statistics** - View library usage statistics
10. **Activity Report** - Generate and view activity log

### System Features
1. **Concurrent Issue Handling** - Thread-safe book issuance (demonstration)
2. **Activity Logging** - Log all transactions to file
3. **JPA Demonstration** - ORM operations alongside JDBC
4. **Fine Calculation** - Automatic fine calculation for overdue books
5. **Input Validation** - Robust error handling and user input validation

## Technologies Used

| Technology | Purpose | Version |
|-----------|---------|---------|
| Java | Programming language | 17 |
| Maven | Build tool | 3.9+ |
| MySQL | Database | 8.0+ |
| JDBC | Database connectivity | Built-in |
| Hibernate | ORM framework | 6.4.1 |
| JUnit | Unit testing | 5.10.1 |

## Architecture

### Layered Architecture
```
┌──────────────────────────────────────┐
│        Main.java (UI/Menu)           │
├──────────────────────────────────────┤
│    Service Layer                     │
│  (StudentService, LibrarianService)  │
├──────────────────────────────────────┤
│    DAO Layer                         │
│  (BookDAO, StudentDAO, etc.)         │
├──────────────────────────────────────┤
│    Database Layer (JDBC/JPA)         │
│    (DatabaseConfig, Persistence)     │
├──────────────────────────────────────┤
│    MySQL Database                    │
└──────────────────────────────────────┘
```

## Project Structure

```
SMART-CAMPUS-LIBRARY-MANAGEMENT-SYSTEM/
│
├── pom.xml                           # Maven configuration
├── README.md                         # This file
├── VIVA_QUESTIONS.md                # Important interview questions
│
├── database/
│   └── schema.sql                   # MySQL database schema
│
├── src/main/java/com/library/
│   ├── Main.java                    # Entry point, menu-driven UI
│   │
│   ├── model/                       # Data models
│   │   ├── User.java               # Abstract base class
│   │   ├── Student.java            # Student extending User
│   │   ├── Librarian.java          # Librarian extending User
│   │   ├── Book.java               # Book model
│   │   ├── IssuedBook.java         # Issue tracking model
│   │   ├── BookCategory.java       # Enum for categories
│   │   ├── UserRole.java           # Enum for roles
│   │   ├── Searchable.java         # Interface for searchable entities
│   │   └── FineCalculable.java     # Interface for fine calculation
│   │
│   ├── service/                     # Business logic
│   │   ├── StudentService.java      # Student operations
│   │   └── LibrarianService.java    # Librarian operations
│   │
│   ├── dao/                         # Database access
│   │   ├── BookDAO.java            # Book CRUD operations
│   │   ├── StudentDAO.java         # Student CRUD operations
│   │   ├── IssuedBookDAO.java      # Issue tracking CRUD
│   │   └── LibrarianDAO.java       # Librarian authentication
│   │
│   ├── exception/                   # Custom exceptions
│   │   ├── InvalidBookException.java
│   │   ├── InvalidStudentException.java
│   │   ├── BookNotAvailableException.java
│   │   └── DatabaseOperationException.java
│   │
│   ├── util/                        # Utility classes
│   │   ├── InputValidator.java      # Input validation & reading
│   │   └── OperationHistory.java    # Stack for operation tracking
│   │
│   ├── jdbc/                        # JDBC configuration
│   │   └── DatabaseConfig.java      # Database connection management
│   │
│   ├── io/                          # File I/O
│   │   └── ActivityLogger.java      # Activity logging to file
│   │
│   ├── thread/                      # Multithreading
│   │   └── BookIssueTask.java       # Thread-safe book issuance
│   │
│   └── jpa/                         # JPA/Hibernate demonstration
│       ├── BookEntity.java          # JPA entity
│       └── JpaBookManager.java      # JPA manager for CRUD
│
├── src/main/resources/
│   └── META-INF/
│       └── persistence.xml          # JPA configuration
│
├── src/test/java/
│   └── (Test classes here)
│
└── library_activity.txt             # Activity log (generated at runtime)
```

## Database Setup

### Prerequisites
- MySQL Server 8.0 or higher
- MySQL Client or MySQL Workbench

### Installation Steps

1. **Start MySQL Server**
   ```bash
   # On Windows (if using MySQL service)
   net start MySQL80
   
   # On Linux/Mac
   sudo systemctl start mysql
   ```

2. **Create Database and Tables**
   ```bash
   mysql -u root -p < database/schema.sql
   ```
   When prompted, enter your MySQL root password.

3. **Verify Installation**
   ```bash
   mysql -u root -p -e "USE campus_library; SHOW TABLES;"
   ```

### Database Configuration

Update credentials in `src/main/java/com/library/jdbc/DatabaseConfig.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_library";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "root";  // Change to your MySQL password
```

Also update `src/main/resources/META-INF/persistence.xml` with the same credentials.

## Compilation & Execution

### Prerequisites
- JDK 17 or higher
- Maven 3.9 or higher
- MySQL Server running

### Compilation

```bash
cd "SMART CAMPUS LIBRARY MANAGEMENT SYSTEM"
mvn clean compile
```

### Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.library.Main"
```

Or if Maven is not available, compile manually:

```bash
javac -d target/classes -cp target/classes src/main/java/com/library/**/*.java
java -cp target/classes:lib/* com.library.Main
```

### Running Tests

```bash
mvn test
```

## Sample Login Credentials

### Student Login
- **Student ID:** 1 (or 2, 3)
- **Password:** pass123 (or pass456, pass789)

### Librarian Login
- **Username:** admin
- **Password:** admin123

## Key Java Concepts Demonstrated

### 1. Object-Oriented Programming (OOP)

#### Encapsulation
- Private fields with public getters/setters in all model classes
- Example: `Book`, `Student`, `Librarian`

#### Inheritance
- Abstract class: `User`
- Subclasses: `Student extends User`, `Librarian extends User`
- Demonstrates `super` keyword in constructors

#### Polymorphism
- Runtime polymorphism: User reference pointing to Student/Librarian
- Method overriding: `getDisplayInfo()` in Student and Librarian

#### Abstraction
- Abstract class `User` with abstract method `getDisplayInfo()`
- Abstract methods force implementation in subclasses

### 2. Interfaces & Multiple Implementation

**Searchable Interface:**
- Implemented by `Book` and `Student`
- Methods: `matchesSearch()`, `getSearchSummary()`

**FineCalculable Interface:**
- Implemented by `Student`
- Demonstrates default interface variables
- Methods: `calculateFine()`, `isOverdue()`

### 3. Enums

- `BookCategory` - Categories of books (SCIENCE, ENGINEERING, etc.)
- `UserRole` - User roles (STUDENT, LIBRARIAN)

### 4. Collections Framework

| Collection | Usage | Location |
|-----------|-------|----------|
| **ArrayList** | Store books, students, issued books | DAO, Service layers |
| **HashMap** | Fast O(1) student/book lookup | StudentDAO, statistics |
| **Vector** | Thread-safe history; activity log | IssuedBookDAO, OperationHistory |
| **Stack** | Track recent operations (LIFO) | OperationHistory |

### 5. Exception Handling

**Custom Exceptions:**
- `InvalidBookException` - Book not found
- `InvalidStudentException` - Student not found
- `BookNotAvailableException` - Book already issued
- `DatabaseOperationException` - Database operation failed

**Demonstrates:**
- `try-catch-finally` blocks
- `throw` and `throws` keywords
- Multiple catch blocks (catching different exception types)
- Exception propagation

### 6. Multithreading

**Thread Safety with Synchronization:**
- `BookIssueTask` implements `Runnable`
- `synchronized` block ensures only one student can issue a book
- Demonstrates: Thread lifecycle, `Thread.start()`, `join()`, `Thread.sleep()`
- Race condition prevention: Two concurrent issue attempts on same book

### 7. File I/O

**Character Streams:**
- `FileWriter` - Write activity logs
- `BufferedWriter` - Efficient buffered writing
- `FileReader` - Read activity logs
- `BufferedReader` - Line-by-line reading

**Byte Streams:**
- `FileInputStream` - Byte-level reading (binary backup demo)
- `FileOutputStream` - Byte-level writing

**Resource Management:**
- `try-catch-finally` for proper resource cleanup
- Modern `try-with-resources` (Java 7+)

### 8. JDBC (Java Database Connectivity)

**Demonstrates:**
- `Connection` - Database connection management
- `PreparedStatement` - Parameterized queries (SQL injection prevention)
- `ResultSet` - Processing query results
- CRUD Operations: INSERT, SELECT, UPDATE, DELETE
- Resource management with try-with-resources

**Example from BookDAO.java:**
```java
String sql = "SELECT * FROM books WHERE title = ?";
try (Connection conn = DatabaseConfig.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, keyword);  // Parameter binding
    try (ResultSet rs = pstmt.executeQuery()) {
        // Process results
    }
}
```

### 9. JPA/Hibernate (Separate Demonstration)

**JPA Annotations:**
- `@Entity` - Maps class to database table
- `@Id` - Marks primary key
- `@Column` - Maps field to column
- `@GeneratedValue` - Auto-increment support

**ORM Operations:**
- `persist()` - Insert entity
- `find()` - Retrieve by primary key
- `merge()` - Update entity
- `remove()` - Delete entity

**JPQL Queries:**
```java
String jpql = "SELECT b FROM BookEntity b WHERE b.category = :category";
```

**Located in:** `src/main/java/com/library/jpa/`

### 10. Constructors & Method Overloading

**Constructor Overloading (Book.java):**
```java
public Book(int id, String title, String author, BookCategory category, boolean available) { }
public Book(int id, String title, String author, BookCategory category) { }
public Book(int id, String title, String author) { }
```

**Method Overloading (InputValidator.java):**
```java
public static int readInt(String prompt) { }
public static int readInt(String prompt, int min, int max) { }
```

### 11. Keyword Demonstrations

| Keyword | Usage | Location |
|---------|-------|----------|
| `this` | Reference current object | Constructors, User.java |
| `super` | Call parent constructor | Student.java, Librarian.java |
| `final` | Immutable field (UserRole) | User.java |
| `abstract` | Abstract class and method | User.java |
| `interface` | Define contract | Searchable.java, FineCalculable.java |
| `extends` | Class inheritance | Student.java, Librarian.java |
| `implements` | Interface implementation | Book.java, Student.java |
| `static` | Class-level members | DatabaseConfig, OperationHistory |
| `synchronized` | Thread-safe block | BookIssueTask.java |
| `instanceof` | Type checking | Main.java (polymorphism demo) |

## Important Implementation Details

### Thread Safety
The `BookIssueTask` class demonstrates synchronized block usage to ensure only one student can issue a book:

```java
synchronized (BookIssueTask.class) {
    Book book = BookDAO.getBookById(bookId);
    if (!book.isAvailable()) {
        throw new BookNotAvailableException(bookId);
    }
    book.setAvailable(false);
    BookDAO.updateBook(book);
}
```

### JDBC vs JPA

| Aspect | JDBC | JPA |
|--------|------|-----|
| **Purpose** | Direct database access | Object-Relational Mapping |
| **Query** | SQL (PreparedStatement) | JPQL or annotations |
| **Used In** | Main application (DAO layer) | Demonstration only |
| **Performance** | Generally faster | Slight overhead from ORM |
| **Learning** | Lower level, more control | Higher level, less boilerplate |
| **Used For** | Production code | Educational purposes |

**Why Both?**
- JDBC shows direct database connectivity and SQL parameter binding
- JPA shows modern ORM patterns and entity mapping
- Both are important Java concepts for a college curriculum

### Input Validation
All user inputs are validated through `InputValidator` class to prevent crashes:
- Integer parsing with error handling
- Email format validation
- Range checking
- Non-empty string validation

### Activity Logging
All important operations are logged to `library_activity.txt`:
- Student registration, login
- Book issue, return
- Book additions, removals
- Librarian login

### Fine Calculation
- Overdue period: 14 days from issue date
- Fine rate: Rs. 2 per day
- Automatic calculation when viewing fine

## Testing Instructions

### Manual Testing Scenarios

1. **Student Registration & Login**
   - Register a new student
   - Login with new credentials
   - Verify profile displays correctly

2. **Book Management**
   - Login as librarian
   - Add a new book
   - Search for the book
   - Verify it appears in "View All Books"

3. **Book Issue & Return**
   - Login as student
   - Issue an available book
   - Verify it appears in "View My Issued Books"
   - Return the book
   - Verify it's no longer in issued list

4. **Exception Handling**
   - Try issuing a non-existent book (InvalidBookException)
   - Try issuing an already-issued book (BookNotAvailableException)
   - Enter invalid credentials (graceful rejection)

5. **Multithreading Demo**
   - From main menu, select "Run Multithreading Demo"
   - Observe 3 concurrent issue attempts on same book
   - Only 1 should succeed (thread safety)

6. **Activity Logging**
   - Perform various operations
   - View Activity Report
   - Verify entries logged to `library_activity.txt`

7. **Fine Calculation**
   - Return a book past due date
   - Check fine calculation
   - Verify formula: daysOverdue × 2 = fine

### Unit Testing
```bash
mvn test
```

## Syllabus Concept Mapping

| Java Concept | Project Location | Features Demonstrated |
|--------------|------------------|----------------------|
| **Classes & Objects** | model/ | Student, Book, Librarian |
| **Encapsulation** | All model classes | Private fields, getters/setters |
| **Inheritance** | User → Student/Librarian | extends, super |
| **Polymorphism** | Main.java menu dispatch | Runtime type checking, method overriding |
| **Abstraction** | User.java | abstract class, abstract methods |
| **Interfaces** | Searchable, FineCalculable | implements, contracts |
| **Constructors** | model/ | Overloaded constructors |
| **this keyword** | model/ | Constructor chaining |
| **super keyword** | Student.java, Librarian.java | Parent constructor invocation |
| **final keyword** | User.java (UserRole role) | Immutable fields |
| **Enums** | BookCategory, UserRole | Type-safe constants |
| **Exception Handling** | exception/, service/ | try-catch-finally, custom exceptions |
| **Multithreading** | thread/ | Thread, Runnable, synchronized |
| **Collections** | ArrayList, HashMap, Vector, Stack | Various implementations, use cases |
| **File I/O** | io/ | FileReader, FileWriter, Streams |
| **JDBC** | dao/, jdbc/ | Connection, PreparedStatement, ResultSet |
| **JPA/ORM** | jpa/ | Entity, EntityManager, JPQL |
| **Input Validation** | util/ | Scanner, error handling |

## Troubleshooting

### MySQL Connection Failed
**Error:** "Failed to connect to database"
- Verify MySQL is running: `mysql -u root -p -e "SELECT 1"`
- Check credentials in `DatabaseConfig.java`
- Ensure `campus_library` database exists
- Run `mysql -u root -p < database/schema.sql`

### No Books/Students in Database
**Solution:** Sample data is inserted in schema.sql
- If no data, manually insert records:
  ```sql
  USE campus_library;
  INSERT INTO students (name, email, department, password) VALUES ('Test Student', 'test@edu.edu', 'CSE', 'pass');
  ```

### JDBC Driver Not Found
**Error:** "MySQL JDBC Driver not found"
- Ensure `pom.xml` includes: `mysql-connector-j` dependency
- Run `mvn clean compile` to download dependencies

### JPA Configuration Issues
**Error:** JPA initialization failed
- Verify `src/main/resources/META-INF/persistence.xml` exists
- Update database credentials in persistence.xml
- Ensure Hibernate dependencies are downloaded: `mvn dependency:resolve`

## Future Enhancements

1. Implement book reservation system
2. Add renewal functionality for books
3. Book recommendation engine
4. Web interface using Spring Boot
5. Mobile app for student access
6. Email notifications for due dates
7. Advanced reporting and analytics

## Author & Credits

- **Project Type:** College-level Java Programming Course
- **Difficulty Level:** Intermediate
- **Created:** 2026

## License

This project is for educational purposes. Modify and use freely for learning.

---

**Getting Started:**
1. Install MySQL and create database: `mysql -u root -p < database/schema.sql`
2. Update database credentials in `DatabaseConfig.java` and `persistence.xml`
3. Compile: `mvn clean compile`
4. Run: `mvn exec:java -Dexec.mainClass="com.library.Main"`
5. Login with credentials: admin/admin123 (librarian) or 1/pass123 (student)

Enjoy exploring the Smart Campus Library Management System!
