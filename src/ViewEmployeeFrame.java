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
    private JRadioButton a115thRadioButton;
    private JRadioButton a1630thRadioButton;
    private JTextArea txtSalary;

    private Employee emp;

    public ViewEmployeeFrame(Employee emp) {
        this.emp = emp;

        ButtonGroup group = new ButtonGroup();
        group.add(a115thRadioButton);
        group.add(a1630thRadioButton);

        setContentPane(mainPanel);
        setTitle("Employee Details & Salary");
        setSize(500, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cmbMonth.setModel(new DefaultComboBoxModel<>(new String[]{
                "June", "July", "August", "September", "October", "November", "December"
        }));

        btnCompute.addActionListener(new ComputeButtonListener());

        // Show profile immediately
        showEmployeeDetails();

        setVisible(true);
    }

    private class ComputeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            computeSalary();
        }
    }

    private void showEmployeeDetails() {
        txtDetails.setText(String.format(
                "========= EMPLOYEE PROFILE =========\n\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n",
                "Last Name:", emp.getName().getLastName(),
                "First Name:", emp.getName().getFirstName(),
                "Birthday:", emp.getBirthday(),
                "Address:", emp.getContact().getAddress(),
                "Phone Number:", emp.getContact().getPhone(),
                "SSS #:", emp.getGovernmentId().getSss(),
                "PhilHealth #:", emp.getGovernmentId().getPhilhealth(),
                "TIN #:", emp.getGovernmentId().getTin(),
                "Pag-IBIG #:", emp.getGovernmentId().getPagibig(),
                "Status:", emp.getStatus(),
                "Position:", emp.getPosition().getPosition(),
                "Supervisor:", emp.getPosition().getSupervisor(),
                "Basic Salary:", emp.getPay().getBasicSalary(),
                "Rice Subsidy:", emp.getPay().getRiceSubsidy(),
                "Phone Allowance:", emp.getPay().getPhoneAllowance(),
                "Clothing Allowance:", emp.getPay().getClothingAllowance(),
                "Semi-monthly Rate:", emp.getPay().getSemiGross(),
                "Hourly Rate:", emp.getPay().getHourlyRate()
        ));
    }

    private void computeSalary() {
        int month = cmbMonth.getSelectedIndex() + 6;
        int year = 2024;

        LocalDate from, to;

        if (a115thRadioButton.isSelected()) {
            from = LocalDate.of(year, month, 1);
            to = LocalDate.of(year, month, 15);
        } else if (a1630thRadioButton.isSelected()) {
            from = LocalDate.of(year, month, 16);
            to = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth());
        } else {
            JOptionPane.showMessageDialog(null, "Please select a pay period (1–15 or 16–30).");
            return;
        }

        double hours = Attendance.getTotalWorkHours(emp.getEmployeeNumber(), from, to);
        Payroll payroll = new Payroll();
        payroll.processPayroll(emp, from, to, hours);

        txtDetails.append(String.format(
                "\n========= SALARY DETAILS =========\n" +
                        "%-25s %s to %s\n" +
                        "%-25s %.2f\n" +
                        "%-25s %,.2f\n\n" +
                        "--- Deductions ---\n" +
                        "%-25s %,.2f\n" +
                        "%-25s %,.2f\n" +
                        "%-25s %,.2f\n" +
                        "%-25s %,.2f\n" +
                        "===============================\n" +
                        "%-25s %,.2f\n" +
                        "%-25s %,.2f\n",
                "Pay Period:", from, to,
                "Worked Hours:", hours,
                "Gross Salary:", payroll.getGrossSalary(),
                "SSS:", payroll.getSss(),
                "PhilHealth:", payroll.getPhilHealth(),
                "Pag-IBIG:", payroll.getPagIbig(),
                "Tax:", payroll.getTax(),
                "Total Deductions:", payroll.getSss() + payroll.getPhilHealth() + payroll.getPagIbig() + payroll.getTax(),
                "Net Pay:", payroll.getNetSalary()
        ));
    }
}
