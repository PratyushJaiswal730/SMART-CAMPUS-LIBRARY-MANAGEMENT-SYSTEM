package com.library.util;

import java.util.Scanner;

/**
 * Utility class for reading and validating console input.
 * Prevents the application from crashing on invalid input.
 */
public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Read a non-empty string from the user.
     */
    public static String readString(String prompt) {
        String input = "";
        while (input.isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("  Input cannot be empty. Please try again.");
            }
        }
        return input;
    }

    /**
     * Read an integer from the user with validation.
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number. Please enter a valid integer.");
            }
        }
    }

    /**
     * Read an integer within a specific range.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("  Please enter a number between " + min + " and " + max + ".");
        }
    }

    /**
     * Read a yes/no confirmation from the user.
     * @return true if user enters y/yes
     */
    public static boolean readConfirmation(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    /**
     * Read an email with basic validation.
     */
    public static String readEmail(String prompt) {
        while (true) {
            String email = readString(prompt);
            if (email.contains("@") && email.contains(".")) {
                return email;
            }
            System.out.println("  Invalid email format. Please enter a valid email.");
        }
    }

    /**
     * Wait for user to press Enter to continue.
     */
    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
