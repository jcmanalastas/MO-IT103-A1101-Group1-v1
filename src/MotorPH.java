import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MotorPH {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static void main(String[] args) {
        Attendance.loadAttendanceFromCSV();
        Scanner scanner = new Scanner(System.in);

        MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("src/Data.csv");

        System.out.println("Welcome to MotorPH Payroll System!");

        // Input Employee ID
        System.out.print("Enter Employee ID: ");
        int inputID = scanner.nextInt();
        scanner.nextLine();

        Employee employee = csvLoader.getEmployee(inputID);
        if (employee == null) {
            System.out.println("Employee ID not found.");
            scanner.close();
            return;
        }

        // Input Month
        System.out.print("Enter Month (Jun, Jul, Aug, Sep, Oct, Nov, Dec): ");
        String monthInput = scanner.nextLine().trim().toLowerCase();

        int monthNumber = Attendance.convertMonthToNumber(monthInput);
        if (monthNumber == -1) {
            System.out.println("Invalid month entered.");
            scanner.close();
            return;
        }

        // Input Pay Coverage
        System.out.println("Select Pay Coverage:");
        System.out.println("1 - Day 1 to 15");
        System.out.println("2 - Day 16 to end of month");
        System.out.print("Enter option (1 or 2): ");
        int coverage = scanner.nextInt();
        scanner.nextLine();

        LocalDate fromDate, toDate;
        int currentYear = 2024;

        if (coverage == 1) {
            fromDate = LocalDate.of(currentYear, monthNumber, 1);
            toDate = LocalDate.of(currentYear, monthNumber, 15);
        } else if (coverage == 2) {
            fromDate = LocalDate.of(currentYear, monthNumber, 16);
            YearMonth yearMonth = YearMonth.of(currentYear, monthNumber);
            int lastDay = yearMonth.lengthOfMonth();
            toDate = LocalDate.of(currentYear, monthNumber, lastDay);
        } else {
            System.out.println("Invalid coverage option.");
            scanner.close();
            return;
        }

        //Create a Payroll object and process the employee's payslip based on attendance
        Payroll payroll = new Payroll();
        double totalHours = Attendance.getTotalWorkHours(inputID, fromDate, toDate);

        payroll.processPayroll(employee,fromDate,toDate,totalHours);

        scanner.close();
    }
}
