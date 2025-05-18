import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class Attendance {
    private static Map<Integer, Double> attendanceMap = new HashMap<>(); // Employee ID → Total Work Hours
    private static final String CSV_FILE = "src/AttendanceRecords.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    // Load CSV Data into HashMap
    public static void loadAttendanceFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean firstRow = true;

            while ((line = br.readLine()) != null) {
                if (firstRow) { firstRow = false; continue; } // Skip headers

                String[] values = line.split(",");
                if (values.length < 6) { continue; } // Validate row format

                int employeeID = Integer.parseInt(values[0]); // Employee ID
                LocalDate date = parseDate(values[3]);
                LocalTime timeIn = parseTime(values[4]); // Log in
                LocalTime timeOut = parseTime(values[5]); // Log out

                if (date != null) {
                    addAttendanceRecord(employeeID, date, timeIn, timeOut); // Store in HashMap
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance CSV: " + e.getMessage());
        }
    }
    // Stores Attendance Record in HashMap
    public static void addAttendanceRecord(int employeeID, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        double workHours = computeWorkHours(timeIn, timeOut);
        attendanceMap.put(employeeID, attendanceMap.getOrDefault(employeeID, 0.0) + workHours);
    }

    private static double computeWorkHours(LocalTime timeIn, LocalTime timeOut) {
        return (double) Duration.between(timeIn, timeOut).toMinutes() / 60;
    }

    // Retrieves Total Work Hours for an Employee
    public static double getTotalWorkHours(int employeeID) {
        return attendanceMap.getOrDefault(employeeID, 0.0); // Return total hours worked
    }

    // Parses Date with Format Handling
    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + dateStr + " (Skipping record)");
            return null;
        }
    }
    // Parses Time with Format Handling
    private static LocalTime parseTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER); // Corrected time formatting
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format: " + timeStr + " (Skipping record)");
            return null;
        }
    }
}
