package com.taufique.schedViz.util;

public class TimeFormatter {

    private TimeFormatter() {
        // Prevent instantiation
    }

    // Format as simple integer time
    public static String formatSimple(int time) {
        return "t=" + time;
    }

    // Format as two-digit number (e.g., 01, 09, 15)
    public static String formatTwoDigit(int time) {
        return String.format("%02d", time);
    }

    // Format as HH:MM (if needed later)
    public static String formatAsClock(int time) {
        int hours = time / 60;
        int minutes = time % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
