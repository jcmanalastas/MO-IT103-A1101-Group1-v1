import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;

public class ViewEmployeeFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> cmbMonth;
    private JButton btnCompute;
    private JTextArea txtDetails;
    private JLabel lblSelectMonth;

    private Employee emp;

    public ViewEmployeeFrame(Employee emp) {
        this.emp = emp;

        setContentPane(mainPanel);
        setTitle("Employee Details & Salary");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cmbMonth.setModel(new DefaultComboBoxModel<>(new String[]{
                "June", "July", "August", "September", "October", "November", "December"
        }));

        btnCompute.addActionListener(new ComputeButtonListener());

        setVisible(true);
    }

    private class ComputeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            computeSalary();
        }
    }

    private void computeSalary() {
        int month = cmbMonth.getSelectedIndex() + 6;
        int year = 2024;

        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth());
        double hours = Attendance.getTotalWorkHours(emp.getEmployeeNumber(), from, to);

        Payroll payroll = new Payroll();
        payroll.processPayroll(emp, from, to, hours);

        txtDetails.setText("Employee Details\n\n"
                + "Name: " + emp.getFullName() + "\n"
                + "Birthday: " + emp.getBirthday() + "\n"
                + "-----------------------------------\n"
                + "Worked Hours: " + String.format("%.2f", hours) + "\n"
                + "Gross Salary: " + String.format("%,.2f", payroll.getGrossSalary()) + "\n"
                + "SSS: " + String.format("%,.2f", payroll.getSss()) + "\n"
                + "PhilHealth: " + String.format("%,.2f", payroll.getPhilHealth()) + "\n"
                + "Pag-IBIG: " + String.format("%,.2f", payroll.getPagIbig()) + "\n"
                + "Tax: " + String.format("%,.2f", payroll.getTax()) + "\n"
                + "-----------------------------------\n"
                + "Total Deductions: " + String.format("%,.2f", payroll.getSss() + payroll.getPhilHealth() + payroll.getPagIbig() + payroll.getTax()) + "\n"
                + "Net Salary: " + String.format("%,.2f", payroll.getNetSalary()));
    }
}
