import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class Attendance {
    // Map<EmployeeID, Map<Date, WorkHours>>
    private static Map<Integer, Map<LocalDate, Double>> attendanceMap = new HashMap<>();

    private static final String CSV_FILE = "src/AttendanceRecords.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    public static int convertMonthToNumber(String month) {
        switch (month) {
            case "jun": return 6;
            case "jul": return 7;
            case "aug": return 8;
            case "sep": return 9;
            case "oct": return 10;
            case "nov": return 11;
            case "dec": return 12;
            default: return -1;
        }
    }
    public static LocalDate[] getPayPeriod(int year, int month, int coverage) {
        LocalDate fromDate;
        LocalDate toDate;

        if (coverage == 1) {
            fromDate = LocalDate.of(year, month, 1);
            toDate = LocalDate.of(year, month, 15);
        } else if (coverage == 2) {
            int lastDay = java.time.YearMonth.of(year, month).lengthOfMonth();
            fromDate = LocalDate.of(year, month, 16);
            toDate = LocalDate.of(year, month, lastDay);
        } else {
            return null;
        }
        return new LocalDate[] { fromDate, toDate };
    }

    // Load CSV Data into nested Map
    public static void loadAttendanceFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean firstRow = true;

            while ((line = br.readLine()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");
                if (values.length < 6) continue; // Validate row format

                int employeeID = Integer.parseInt(values[0].trim());
                LocalDate date = parseDate(values[3].trim());
                LocalTime timeIn = parseTime(values[4].trim());
                LocalTime timeOut = parseTime(values[5].trim());

                if (date != null && timeIn != null && timeOut != null) {
                    addAttendanceRecord(employeeID, date, timeIn, timeOut);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance CSV: " + e.getMessage());
        }

    }

    // Add or accumulate attendance hours per employee per date
    public static void addAttendanceRecord(int employeeID, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        double workHours = computeWorkHours(timeIn, timeOut);

        attendanceMap.putIfAbsent(employeeID, new HashMap<>());
        Map<LocalDate, Double> dateMap = attendanceMap.get(employeeID);

        double existingHours = dateMap.getOrDefault(date, 0.0);
        dateMap.put(date, existingHours + workHours);

    }

    private static double computeWorkHours(LocalTime timeIn, LocalTime timeOut) {
        return Duration.between(timeIn, timeOut).toMinutes() / 60.0;
    }

    // Get total work hours for employee in a date range (inclusive)
    public static double getTotalWorkHours(int employeeID, LocalDate fromDate, LocalDate toDate) {
        if (!attendanceMap.containsKey(employeeID)) {
            return 0.0;
        }

        Map<LocalDate, Double> dateMap = attendanceMap.get(employeeID);
        double totalHours = 0.0;

        for (Map.Entry<LocalDate, Double> entry : dateMap.entrySet()) {
            LocalDate date = entry.getKey();
            if ((date.isEqual(fromDate) || date.isAfter(fromDate)) &&
                    (date.isEqual(toDate) || date.isBefore(toDate))) {
                totalHours += entry.getValue();
            }
        }
        return totalHours;
    }

    // Parses date string with validation
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + dateStr + " (Skipping record)");
            return null;
        }
    }

    // Parses time string with validation
    public static LocalTime parseTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Parses user input date with message on failure
    public static LocalDate parseDateFromInput(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + dateStr + " (Please use MM/dd/yyyy)");
            return null;
        }
    }
}


