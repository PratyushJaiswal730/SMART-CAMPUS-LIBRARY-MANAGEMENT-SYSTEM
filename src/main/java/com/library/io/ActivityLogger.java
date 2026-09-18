package com.library.io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles library activity logging to a text file.
 *
 * Demonstrates:
 * - File, FileWriter, BufferedWriter (character streams)
 * - FileOutputStream (byte stream) for one demo
 * - Exception handling with try-catch-finally
 * - File I/O best practices
 */
public class ActivityLogger {

    private static final String LOG_FILE = "library_activity.txt";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Log an activity to the activity file.
     * Uses BufferedWriter for efficient character stream writing.
     *
     * Demonstrates:
     * - FileWriter (creates file if not exists)
     * - BufferedWriter (buffering for performance)
     * - try-catch-finally for resource management
     */
    public static void logActivity(String activity) {
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(LOG_FILE, true);  // true = append mode
            bw = new BufferedWriter(fw);

            String timestamp = LocalDateTime.now().format(TIME_FORMAT);
            String logEntry = "[" + timestamp + "] " + activity;

            bw.write(logEntry);
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            System.err.println("Error logging activity: " + e.getMessage());
        } finally {
            // Proper resource cleanup
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.err.println("Error closing BufferedWriter: " + e.getMessage());
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    System.err.println("Error closing FileWriter: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Display the activity log on console.
     * Uses FileReader and BufferedReader for efficient reading.
     *
     * Demonstrates:
     * - FileReader (character stream for reading)
     * - BufferedReader (line-by-line reading)
     * - try-catch for exception handling
     */
    public static void displayActivityLog() {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            System.out.println("  Activity log file does not exist yet.");
            return;
        }

        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            System.out.println("\n========== Library Activity Log ==========");
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("  " + line);
            }
            System.out.println("==========================================\n");

        } catch (IOException e) {
            System.err.println("Error reading activity log: " + e.getMessage());
        } finally {
            // Proper resource cleanup
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Error closing BufferedReader: " + e.getMessage());
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    System.err.println("Error closing FileReader: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Byte-stream demonstration: Export log as binary format.
     * Shows FileInputStream and FileOutputStream usage.
     *
     * Demonstrates:
     * - FileOutputStream (byte stream writing)
     * - FileInputStream (byte stream reading)
     * - Byte-level I/O
     */
    public static void backupLogAsBinary() {
        String backupFile = "library_activity.bak";

        // Read activity log and write as binary
        try (FileInputStream fis = new FileInputStream(LOG_FILE);
             FileOutputStream fos = new FileOutputStream(backupFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("  Activity log backed up to: " + backupFile);

        } catch (IOException e) {
            System.err.println("Error backing up log: " + e.getMessage());
        }
    }

    /**
     * Clear the activity log file.
     */
    public static void clearLog() {
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {  // false = truncate
            // Empty write clears the file
        } catch (IOException e) {
            System.err.println("Error clearing activity log: " + e.getMessage());
        }
    }
}
