package com.library.dao;

import com.library.model.Student;
import com.library.exception.DatabaseOperationException;
import com.library.jdbc.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object for Student entity.
 *
 * Demonstrates:
 * - JDBC operations with PreparedStatement
 * - HashMap for fast lookups by ID
 * - ResultSet processing
 * - Exception handling
 */
public class StudentDAO {

    /**
     * Register a new student.
     */
    public static void registerStudent(Student student) throws DatabaseOperationException {
        String sql = "INSERT INTO students (name, email, department, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getDepartment());
            pstmt.setString(4, student.getPassword());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to register student: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve a student by ID.
     */
    public static Student getStudentById(int studentId) throws DatabaseOperationException {
        String sql = "SELECT student_id, name, email, department, password FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve student: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Authenticate student login.
     * @return Student if credentials match, null otherwise
     */
    public static Student loginStudent(int studentId, String password) throws DatabaseOperationException {
        Student student = getStudentById(studentId);
        if (student != null && student.getPassword().equals(password)) {
            return student;
        }
        return null;
    }

    /**
     * Get all students as a List.
     */
    public static List<Student> getAllStudents() throws DatabaseOperationException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT student_id, name, email, department, password FROM students ORDER BY student_id";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve students: " + e.getMessage(), e);
        }
        return students;
    }

    /**
     * Get all students as a HashMap for fast lookups by ID.
     * Demonstrates HashMap usage for efficient searching.
     */
    public static Map<Integer, Student> getAllStudentsAsMap() throws DatabaseOperationException {
        Map<Integer, Student> studentMap = new HashMap<>();
        List<Student> students = getAllStudents();
        for (Student student : students) {
            studentMap.put(student.getId(), student);
        }
        return studentMap;
    }

    /**
     * Update student information.
     */
    public static void updateStudent(Student student) throws DatabaseOperationException {
        String sql = "UPDATE students SET name = ?, email = ?, department = ?, password = ? WHERE student_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getDepartment());
            pstmt.setString(4, student.getPassword());
            pstmt.setInt(5, student.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update student: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to map ResultSet to Student.
     */
    private static Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        int studentId = rs.getInt("student_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String department = rs.getString("department");
        String password = rs.getString("password");

        return new Student(studentId, name, email, department, password);
    }
}
