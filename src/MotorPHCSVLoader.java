import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MotorPHCSVLoader {

    private Map<Integer, Employee> employeeMap = new HashMap<>();

    public MotorPHCSVLoader(String filename) {
        loadEmployeesFromCSV(filename);
    }

    public Employee getEmployee(int employeeID) {
        return employeeMap.get(employeeID);
    }

    private void loadEmployeesFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                List<String> fields = splitCSV(line);
                if (fields.size() < 19) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }

                int employeeNumber = Integer.parseInt(fields.get(0).trim());

                // Name
                String firstName = fields.get(1).trim();
                String lastName = fields.get(2).trim();
                Person person = new Person(firstName, lastName);

                // Birthday
                String birthday = fields.get(3).trim();

                // Contact
                String phone = fields.get(4).trim();
                String address = fields.get(8).trim();
                ContactInfo contact = new ContactInfo(address, phone);

                // Government ID
                String sss = fields.get(5).trim();
                String philhealth = fields.get(6).trim();
                String pagibig = fields.get(7).trim();
                String tin = fields.get(9).trim();
                GovernmentID govID = new GovernmentID(sss, philhealth, pagibig, tin);

                // Status
                String status = fields.get(10).trim();

                // Job
                String positionTitle = fields.get(11).trim();
                String supervisor = fields.get(12).trim().equalsIgnoreCase("N/A") ? "None" : fields.get(12).trim();
                Job job = new Job(positionTitle, supervisor);

                // Compensation

                double basicSalary = tryParseDouble(fields.get(13));
                double riceSubsidy = tryParseDouble(fields.get(14));
                double phoneAllowance = tryParseDouble(fields.get(15));
                double clothingAllowance = tryParseDouble(fields.get(16));
                double semiGross = tryParseDouble(fields.get(17));
                double hourlyRate = tryParseDouble(fields.get(18));
                Compensation comp = new Compensation(basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, hourlyRate, semiGross);

                // Employee object
                Employee employee = new Employee(employeeNumber, person, birthday, contact, govID, status, job, comp);
                employeeMap.put(employeeNumber, employee);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
    }

    // Safely parse double values after removing commas
    private double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Basic CSV parser that handles quoted fields with commas
    private List<String> splitCSV(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        result.add(current.toString().trim());
        return result;
    }
}
