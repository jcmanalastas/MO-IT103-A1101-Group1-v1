import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Payroll {

    private double sss;
    private double philHealth;
    private double pagIbig;
    private double tax;
    private double netSalary;
    private double grossSalary;

    public void processPayroll(Employee employee, LocalDate fromDate, LocalDate toDate, double totalHours) {
        double hourlyRate = employee.getPay().getHourlyRate();
        if (hourlyRate <= 0) {
            hourlyRate = employee.getPay().calculateHourlyRate();
        }

        double basicSalary = employee.getPay().getBasicSalary();
        calculateGrossSalary(totalHours, hourlyRate);
        computeAllDeductions(basicSalary);
        printPayslipSummary(employee, fromDate, toDate, totalHours);
    }

    private void calculateGrossSalary(double totalHours, double hourlyRate) {
        this.grossSalary = totalHours * hourlyRate;
    }
    private double computeSSS(double basicSalary) {
        if (basicSalary < 3250) {
            return 135.00;
        }

        // If above or equal to 24,750
        if (basicSalary >= 24750) {
            return 1125.00;
        }

        // Calculate bracket index: (salary - 3250) / 500
        int bracket = (int) ((basicSalary - 3250) / 500);

        // Base contribution: 157.50 for first bracket, +22.50 per bracket
        return 157.50 + bracket * 22.50;
    }
    private double computePhilHealth(double basicSalary) {
        double rate = 0.03;
        double employeeShareRate = rate / 2.0;

        // Salary between 10,000 and 60,000
        if (basicSalary < 10000) basicSalary = 10000;
        else if (basicSalary > 60000) basicSalary = 60000;

        return basicSalary * employeeShareRate;
    }

    private double computePagIBIG(double basicSalary) {
        double contributionRate;

        if (basicSalary >= 1000 && basicSalary <= 1500) {
            contributionRate = 0.01;
        } else if (basicSalary > 1500) {
            contributionRate = 0.02;
        } else {
            return 0.0;
        }
        double contribution = basicSalary * contributionRate;
        return Math.min(contribution, 100.00);
    }

    private double computeWithholdingTax(double basicSalary) {
        if (basicSalary <= 20833) {
            return 0.0;
        } else if (basicSalary < 33333) {
            return (basicSalary - 20833) * 0.20;
        } else if (basicSalary < 66667) {
            return 2500 + (basicSalary - 33333) * 0.25;
        } else if (basicSalary < 166667) {
            return 10833 + (basicSalary - 66667) * 0.30;
        } else if (basicSalary < 666667) {
            return 40833.33 + (basicSalary - 166667) * 0.32;
        } else {
            return 200833.33 + (basicSalary - 666667) * 0.35;
        }
    }

    private void computeAllDeductions(double monthlySalary) {
        sss = computeSSS(monthlySalary);
        philHealth = computePhilHealth(monthlySalary);
        pagIbig = computePagIBIG(monthlySalary);
        tax = computeWithholdingTax(monthlySalary);
        netSalary = grossSalary - (sss + philHealth + pagIbig + tax);
    }

    private void printPayslipSummary(Employee employee, LocalDate fromDate, LocalDate toDate, double totalHours) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        System.out.println("\n======= PAYSLIP =======");
        System.out.printf("Employee #: %d\n", employee.getEmployeeNumber());
        System.out.printf("Name: %s\n", employee.getFullName());
        System.out.printf("Birthday: %s\n", employee.getBirthday());
        System.out.printf("Pay Period: %s to %s\n", fromDate.format(formatter), toDate.format(formatter));
        System.out.printf("Total Hours Worked: %.2f\n", totalHours);
        System.out.printf("Gross Salary: %.2f\n", grossSalary);
        System.out.println("--- Deductions ---");
        System.out.printf("SSS: %.2f\n", sss);
        System.out.printf("PhilHealth: %.2f\n", philHealth);
        System.out.printf("Pag-IBIG: %.2f\n", pagIbig);
        System.out.printf("Tax: %.2f\n", tax);
        System.out.printf("Total Deductions: %.2f\n", sss + philHealth + pagIbig + tax);
        System.out.printf("Net Salary: %.2f\n", netSalary);
        System.out.println("========================");
    }

    public double getSss() {
        return sss;
    }

    public double getPhilHealth() {
        return philHealth;
    }

    public double getPagIbig() {
        return pagIbig;
    }

    public double getTax() {
        return tax;
    }
    public double getNetSalary() {
        return netSalary;
    }
    public double getGrossSalary() {
        return grossSalary;
    }
}