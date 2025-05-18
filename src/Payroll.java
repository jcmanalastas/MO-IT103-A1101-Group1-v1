public class Payroll {

    double sss;
    double philHealth;
    double pagIbig;
    double tax;
    double netSalary;
    double grossSalary;

    public double computeDeductions(){
        return sss + philHealth + pagIbig + tax;
    }

    public void generatePayslip(){
        netSalary = grossSalary - computeDeductions();

        System.out.println("======= PAYSLIP ======");
        System.out.println("Gross Salary: " + grossSalary);
        System.out.println("SSS: " + sss);
        System.out.println("PhilHealth: " + philHealth);
        System.out.println("Pag-ibig: " + pagIbig);
        System.out.println("Tax: " + tax);
        System.out.println("-------------------------");
        System.out.println("Total Deductions: " +computeDeductions());
        System.out.println("Net Salary: " + netSalary);
        System.out.println("-------------------------");
    }
}
