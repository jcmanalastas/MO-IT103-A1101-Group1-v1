import java.io.*;
import java.util.*;

public class MotorPHCSVLoader {

    private Map<Integer, Employee> employeeMap = new HashMap<>();
    private String csvFilePath; // Store path for reuse

    public MotorPHCSVLoader(String filename) {
        this.csvFilePath = filename;
        loadEmployeesFromCSV(filename);
    }

    public void reload() {
        employeeMap.clear();
        loadEmployeesFromCSV("src/Data.csv");
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

                // Person
                String firstName = fields.get(2).trim();
                String lastName = fields.get(1).trim();
                Person person = new Person(firstName, lastName);
                String birthday = fields.get(3).trim();

                // ContactInfo
                String address = fields.get(4).trim();
                String phone = fields.get(5).trim();
                ContactInfo contact = new ContactInfo(address, phone);

                // GovernmentID
                String sss = fields.get(6).trim();
                String philhealth = fields.get(7).trim();
                String tin = fields.get(8).trim();
                String pagibig = fields.get(9).trim();
                GovernmentID govID = new GovernmentID(sss, philhealth, pagibig, tin);

                // Employment
                String status = fields.get(10).trim();
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

                // Create and store Employee
                Employee employee = new Employee(employeeNumber, person, birthday, contact, govID, status, job, comp);
                employeeMap.put(employeeNumber, employee);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
    }

    // Return all employee records loaded in memory
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }

    // Safely parse doubles
    private double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Basic CSV parser that respects quoted commas
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
