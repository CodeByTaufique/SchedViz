package com.taufique.schedViz.util;

public class ValidationUtils {

    private ValidationUtils() {
        // Prevent instantiation
    }

    // Check if string is a valid integer
    public static boolean isInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if integer is positive
    public static boolean isPositive(int value) {
        return value > 0;
    }

    // Check if integer is non-negative (arrival time)
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }

    // Validate process input fields
    public static boolean validateProcessInput(String arrival, String burst) {
        if (!isInteger(arrival) || !isInteger(burst)) {
            return false;
        }

        int arrivalTime = Integer.parseInt(arrival.trim());
        int burstTime = Integer.parseInt(burst.trim());

        return isNonNegative(arrivalTime) && isPositive(burstTime);
    }
}
