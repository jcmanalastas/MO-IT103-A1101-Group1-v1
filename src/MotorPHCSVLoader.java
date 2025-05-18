import java.io.*;
import java.util.*;
import java.util.regex.*;

public class MotorPHCSVLoader {
    private List<String[]> data = new ArrayList<>();
    private String path;

    // Constructor to initialize file path
    public MotorPHCSVLoader(String path) {
        this.path = path;
        loadCSV();
    }

    // Method to load CSV data into memory
    private void loadCSV() {
        String csvRegex = "\"([^\"]*)\"|([^,]+)"; // Regex to handle quoted fields correctly

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> values = new ArrayList<>();
                Matcher matcher = Pattern.compile(csvRegex).matcher(line);

                while (matcher.find()) {
                    values.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2)); // Extract full field
                }

                data.add(values.toArray(new String[0]));
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Method to search for an employee by ID
    public String[] searchEmployee(String employeeID) {
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(employeeID)) {
                return row; // Return the matching row
            }
        }
        return null; // Return null if not found
    }

    // Method to format and display the employee details
    public void displayEmployee(String employeeID) {
        String[] employee = searchEmployee(employeeID);

        if (employee != null) {
            System.out.printf("%-15s %-20s %-25s\n", "Employee Number", "Employee Name", "Employee Address");
            System.out.printf("%-15s %-20s %-25s\n",
                    employee[0].replace("\"", ""),
                    employee[1].replace("\"", "") + " " + employee[2].replace("\"", ""),
                    employee[4].replace("\"", "")
            );
        } else {
            System.out.println("Employee ID not found.");
        }
    }
}
