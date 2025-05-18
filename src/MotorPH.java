import java.util.Scanner;

public class MotorPH {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize CSV Loader
        MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("Data.csv");

        System.out.println("Welcome to MotorPH Payroll System!");
        System.out.print("Enter Employee ID: ");
        int inputID = scanner.nextInt();

        // Fetch Employee from the CSV Loader
        Employee employee = csvLoader.getEmployee(inputID);

        if (employee != null) {
            System.out.println("\nEmployee Found:\n");
            System.out.printf("%-15s %-20s %-25s\n", "Employee #", "Employee Name", "Birthday");
            System.out.printf("%-15s %-20s %-25s\n", employee.getEmployeeNumber(), employee.getFullName(), employee.getBirthday());
            System.out.println("\n===== Salary Information ===========");


        } else {
            System.out.println("Employee ID not found.");
        }

        scanner.close();
    }
}
