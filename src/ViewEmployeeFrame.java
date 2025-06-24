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

        LocalDate from;
        LocalDate to;

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

        txtDetails.setText("Employee Details\n"
                + "Pay Period: " + from + " to " + to + "\n\n"
                + "Last Name: " + emp.getName().getLastName() + "\n"
                + "First Name: " + emp.getName().getFirstName() + "\n"
                + "Birthday: " + emp.getBirthday() + "\n"
                + "Address:" + emp.getContact().getAddress() + "\n"
                + "Phone Number:" + emp.getContact().getPhone() + "\n"
                + "SSS# :" + emp.getGovernmentId().getSss() + "\n"
                + "PhilHealth number :" + emp.getGovernmentId().getPhilhealth() + "\n"
                + "TIN  :" + emp.getGovernmentId().getTin() + "\n"
                + "Pag-ibig#  :" + emp.getGovernmentId().getPagibig() + "\n"
                + "Status: " + emp.getStatus() + "\n"
                + "Position: " + emp.getPosition().getPosition() + "\n"
                + "Immediate Supervisor: " + emp.getPosition().getSupervisor() + "\n"
                + "Basic Salary: " + emp.getPay().getBasicSalary() + "\n"
                + "Rice Subsidy: " + emp.getPay().getRiceSubsidy() + "\n"
                + "Phone Allowance: " + emp.getPay().getPhoneAllowance() + "\n"
                + "Clothing Allowance: " + emp.getPay().getClothingAllowance() + "\n"
                + "Gross Semi-monthly rate: " + emp.getPay().getSemiGross() + "\n"
                + "Hourly Rate: " + emp.getPay().getHourlyRate() + "\n"

                + "-----------------------------------\n"
                + "Worked Hours: " + String.format("%.2f", hours) + "\n"
                + "Gross Salary: " + String.format("%,.2f", payroll.getGrossSalary()) + "\n"
                + "--- Deductions ---\n"
                + "SSS: " + String.format("%,.2f", payroll.getSss()) + "\n"
                + "PhilHealth: " + String.format("%,.2f", payroll.getPhilHealth()) + "\n"
                + "Pag-IBIG: " + String.format("%,.2f", payroll.getPagIbig()) + "\n"
                + "Tax: " + String.format("%,.2f", payroll.getTax()) + "\n"
                + "==========================\n"
                + "Total Deductions: " + String.format("%,.2f", payroll.getSss() + payroll.getPhilHealth() + payroll.getPagIbig() + payroll.getTax()) + "\n"
                + "Net Pay: " + String.format("%,.2f", payroll.getNetSalary()));
    }
}
