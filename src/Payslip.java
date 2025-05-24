import java.time.LocalDate;

public class Payslip {
    private String payStart;
    private String payEnd;
    private double totalHours;
    private double grossSalary;
    private double SSS;
    private double philHealth;
    private double pagIBIG;
    private double tax;
    private double totalDeductions;
    private double netPay;

    public String getPayStart() { return payStart; }
    public void setPayStart(String payStart) { this.payStart = payStart; }
    public String getPayEnd() { return payEnd; }
    public void setPayEnd(String payEnd) { this.payEnd = payEnd; }
    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = totalHours; }
    public double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(double grossSalary) { this.grossSalary = grossSalary; }
    public double getSSS() { return SSS; }
    public void setSSS(double SSS) { this.SSS = SSS; }
    public double getPhilHealth() { return philHealth; }
    public void setPhilHealth(double philHealth) { this.philHealth = philHealth; }
    public double getPagIBIG() { return pagIBIG; }
    public void setPagIBIG(double pagIBIG) { this.pagIBIG = pagIBIG; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(double totalDeductions) { this.totalDeductions = totalDeductions; }
    public double getNetPay() { return netPay; }
    public void setNetPay(double netPay) { this.netPay = netPay; }
}