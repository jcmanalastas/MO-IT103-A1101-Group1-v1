import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GUI extends JFrame {
    private MotorPHCSVLoader dataLoader;
    private JPanel main;
    private JTextField txtEmpNum;
    private JComboBox cmbPayPeriod;
    private JRadioButton btn15;
    private JRadioButton btn30;
    private JTable txtPayslip;
    private JButton btnSearch;
    private Payroll payrollSystem;

    public GUI() {
        dataLoader = new MotorPHCSVLoader("EmployeeList.csv");
        payrollSystem = new Payroll();

        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
        setLocationRelativeTo(null);

        createTable();
        ButtonGroup payFrequencyGroup = new ButtonGroup();
        payFrequencyGroup.add(btn15);
        payFrequencyGroup.add(btn30);
        btn15.setSelected(true);

        String[] periods = {"June 2024", "July 2024", "August 2024", "September 2024", "October 2024", "November 2024", "December 2024"};
        cmbPayPeriod.setModel(new DefaultComboBoxModel<>(periods));
        btnSearch.addActionListener(e -> searchEmployeePayslip());}

    private void searchEmployeePayslip() {
        String empNumber = txtEmpNum.getText().trim();
        if (empNumber.isEmpty()) { showError("Please enter an employee number");
            return;}

        try {
            int empId = Integer.parseInt(empNumber);
            Employee employee = dataLoader.getEmployee(empId);

            if (employee == null) {
                showError("Employee not found");
                return;}
            Payroll payroll = new Payroll();
            payroll.processPayroll(
                    employee,
                    LocalDate.now().minusDays(15),
                    LocalDate.now(),
                    160.0 // Hours (8????///9????///10????// not sure about this tho)
            );
            Payslip payslip = createPayslipFromPayroll(payroll);
            updatePayslipTable(List.of(payslip));
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Payslip createPayslipFromPayroll(Payroll payroll) {
        Payslip payslip = new Payslip();
        payslip.setPayStart(LocalDate.now().minusDays(15).toString());
        payslip.setPayEnd(LocalDate.now().toString());
        payslip.setGrossSalary(payroll.getGrossSalary());
        payslip.setSSS(payroll.getSss());
        payslip.setPhilHealth(payroll.getPhilHealth());
        payslip.setPagIBIG(payroll.getPagIbig());
        payslip.setTax(payroll.getTax());
        payslip.setTotalDeductions(payroll.getSss() + payroll.getPhilHealth() + payroll.getPagIbig() + payroll.getTax());
        payslip.setNetPay(payroll.getNetSalary());
        return payslip;
    }
            private void createTable() {
        txtPayslip.setModel(new DefaultTableModel(
                null,
                new String[]{"Pay Start", "Pay End", "Total Hours", "Gross Salary", "SSS", "PhilHealth", "Pag-IBIG", "Tax", "Total Deductions", "Net Pay"}
        ));
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void printPayrollDebugInfo(Payroll payroll, Employee employee) {
        System.out.println("\n=== DEBUG INFORMATION ===");
        System.out.println("Employee: " + employee.getFullName());
        System.out.printf("Basic Salary: ₱%,.2f%n", employee.getPay().getBasicSalary());
        System.out.printf("Gross Salary: ₱%,.2f%n", payroll.getGrossSalary());
        System.out.printf("Net Salary: ₱%,.2f%n", payroll.getNetSalary());
        System.out.println("========================");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
