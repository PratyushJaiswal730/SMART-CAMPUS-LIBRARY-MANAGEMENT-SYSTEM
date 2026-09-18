package com.library.util;

import java.util.Stack;

/**
 * Tracks recent operations performed in the system using a Stack.
 *
 * Demonstrates:
 * - Stack collection usage
 * - LIFO (Last-In-First-Out) behavior for recent actions
 */
public class OperationHistory {

    private static final Stack<String> history = new Stack<>();
    private static final int MAX_HISTORY = 50;

    /**
     * Push a new operation onto the history stack.
     */
    public static void push(String operation) {
        if (history.size() >= MAX_HISTORY) {
            // Remove oldest entry from bottom to keep stack bounded
            history.remove(0);
        }
        history.push(operation);
    }

    /**
     * View the most recent operation without removing it.
     */
    public static String peekLast() {
        if (history.isEmpty()) {
            return "No operations recorded.";
        }
        return history.peek();
    }

    /**
     * Get all operations in reverse order (most recent first).
     */
    public static void displayHistory() {
        if (history.isEmpty()) {
            System.out.println("  No operations recorded.");
            return;
        }
        System.out.println("\n--- Recent Operations (most recent first) ---");
        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.println("  " + (history.size() - i) + ". " + history.get(i));
        }
        System.out.println("----------------------------------------------");
    }

    /**
     * Get the number of recorded operations.
     */
    public static int size() {
        return history.size();
    }

    /**
     * Clear all history.
     */
    public static void clear() {
        history.clear();
    }
}
