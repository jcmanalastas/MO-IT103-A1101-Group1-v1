import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class ViewEmployeeFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> cmbMonth;
    private JButton btnCompute;
    private JTextArea txtDetails;

    private Employee emp;

    public ViewEmployeeFrame(Employee emp) {
        this.emp = emp;

        setContentPane(mainPanel);
        setTitle("Employee Details & Salary");
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Populate month dropdown
        cmbMonth.setModel(new DefaultComboBoxModel<>(new String[]{
                "June", "July", "August", "September", "October", "November", "December"
        }));

        btnCompute.addActionListener(e -> computeSalary());

        setVisible(true);
    }

    private void computeSalary() {
        int month = cmbMonth.getSelectedIndex() + 1;
        int year = 2024;

        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth());
        double hours = Attendance.getTotalWorkHours(emp.getEmployeeNumber(), from, to);

        Payroll payroll = new Payroll();
        payroll.processPayroll(emp, from, to, hours);

        txtDetails.setText("EMPLOYEE DETAILS\n"
                + "Name: " + emp.getFullName() + "\n"
                + "Birthday: " + emp.getBirthday() + "\n"
                + "-----------------------------------\n"
                + "Worked Hours: " + hours + "\n\n"
                + "Gross Salary: " + payroll.getGrossSalary() + "\n"
                + "SSS: " + payroll.getSss() + "\n"
                + "PhilHealth: " + payroll.getPhilHealth() + "\n"
                + "Pag-IBIG: " + payroll.getPagIbig() + "\n"
                + "Tax: " + payroll.getTax() + "\n"
                + "-----------------------------------\n"
                + "Total Deductions: " + (payroll.getSss() + payroll.getPhilHealth() + payroll.getPagIbig() + payroll.getTax()) + "\n"
                + "Net Salary: " + payroll.getNetSalary());
    }
}
