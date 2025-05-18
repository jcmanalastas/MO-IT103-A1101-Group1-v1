import java.util.Scanner;

public class MotorPH {
    public static void main(String[] args) {
        Attendance.loadAttendanceFromCSV();
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
            System.out.println("Compute Salary: "+ employee.computeSalary());
            System.out.println("\n===== Work Hours ===========");
            System.out.printf("Total Hours Worked: %.2f%n", Attendance.getTotalWorkHours(inputID));

        } else {
            System.out.println("Employee ID not found.");
        }

        scanner.close();
    }
}
