public class Payroll {

    // Calculate SSS
    public double calculateSSS(double grossSalary) {
        double sssRate = 0.11; // Example: 11% contribution rate
        return grossSalary * sssRate;
    }

    // Calculate PhilHealth
    public double calculatePhilHealth(double grossSalary) {
        double philHealthRate = 0.035; // Example: 3.5% contribution rate
        return grossSalary * philHealthRate;
    }

    // Calculate Pag-IBIG
    public double calculatePagIBIG(double grossSalary) {
        double pagIBIGRate = 0.02; // Example: 2% contribution rate
        return grossSalary * pagIBIGRate;
    }

    // Calculate Tax
    public double calculateTax(double grossSalary) {
        if (grossSalary <= 25000) {
            return 0; // No tax for salaries <= 25,000
        } else if (grossSalary <= 50000) {
            return (grossSalary - 25000) * 0.10; // 10% tax for amount over 25,000
        } else {
            return (grossSalary - 50000) * 0.20 + 2500; // 20% tax for amount over 50,000 plus 2,500 fixed
        }
    }

    public void processPayroll(Employee employee, double totalHours) {
        double grossSalary = employee.getPay().getMonthlyGrossSalary();
        double sss = calculateSSS(grossSalary);
        double philHealth = calculatePhilHealth(grossSalary);
        double pagIBIG = calculatePagIBIG(grossSalary);
        double tax = calculateTax(grossSalary);

        double totalDeductions = sss + philHealth + pagIBIG + tax;
        double netPay = grossSalary - totalDeductions;

        // Print payslip summary
        System.out.println("Gross Salary: " + grossSalary);
        System.out.println("SSS: " + sss);
        System.out.println("PhilHealth: " + philHealth);
        System.out.println("Pag-IBIG: " + pagIBIG);
        System.out.println("Tax: " + tax);
        System.out.println("Total Deductions: " + totalDeductions);
        System.out.println("Net Pay: " + netPay);
    }
}
