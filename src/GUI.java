import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;

public class GUI extends JFrame {
    private JPanel main;
    private JTextField txtEmpNum;
    private JComboBox<String> cmbPayPeriod;
    private JRadioButton btn15;
    private JRadioButton btn30;
    private JTable txtPayslip;
    private JButton btnSearch;
    private JLabel lblEmpName1;
    private JLabel lblEmpNum1;
    private JLabel lblEName;
    private JLabel lblENum;

    public GUI() {
        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 400);
        setLocationRelativeTo(null);
        createTable();
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int employeeNumber = Integer.parseInt(txtEmpNum.getText().trim());
                    String selectedMonth = (String) cmbPayPeriod.getSelectedItem();

                    MotorPHCSVLoader loader = new MotorPHCSVLoader("src/Data.csv");
                    Employee employee = loader.getEmployee(employeeNumber);

                    if (employee != null && selectedMonth != null) {
                        lblEName.setText(employee.getFullName());
                        lblENum.setText(String.valueOf(employee.getEmployeeNumber()));

                        int monthNumber = Attendance.convertMonthToNumber(selectedMonth.toLowerCase());
                        if (monthNumber == -1) {
                            JOptionPane.showMessageDialog(null, "Invalid month selected.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        LocalDate fromDate, toDate;
                        int fixedYear = 2024;  // Fixed year

                        if (btn15.isSelected()) {
                            fromDate = LocalDate.of(fixedYear, monthNumber, 1);
                            toDate = LocalDate.of(fixedYear, monthNumber, 15);
                        } else if (btn30.isSelected()) {
                            fromDate = LocalDate.of(fixedYear, monthNumber, 16);
                            YearMonth yearMonth = YearMonth.of(fixedYear, monthNumber);
                            toDate = LocalDate.of(fixedYear, monthNumber, yearMonth.lengthOfMonth());
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select a pay period.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        double totalHours = Attendance.getTotalWorkHours(employeeNumber, fromDate, toDate);
                        Payroll payroll = new Payroll();

                        // Call the correct processPayroll with 4 parameters:
                        payroll.processPayroll(employee, fromDate, toDate, totalHours);

                        // Now get values from payroll instance — assuming getters exist:
                        double grossSalary = payroll.getGrossSalary();
                        double sss = payroll.getSSS();
                        double philHealth = payroll.getPhilHealth();
                        double pagIBIG = payroll.getPagIBIG();
                        double tax = payroll.getTax();

                        double totalDeductions = sss + philHealth + pagIBIG + tax;
                        double netPay = grossSalary - totalDeductions;

                        DefaultTableModel model = (DefaultTableModel) txtPayslip.getModel();
                        model.setRowCount(0); // Clear previous data
                        model.addRow(new Object[]{
                                fromDate.toString(),
                                toDate.toString(),
                                totalHours,
                                grossSalary,
                                sss,
                                philHealth,
                                pagIBIG,
                                tax,
                                totalDeductions,
                                netPay
                        });

                    } else {
                        JOptionPane.showMessageDialog(null, "Employee ID not found or month not selected.", "Error", JOptionPane.ERROR_MESSAGE);
                        lblEName.setText("");
                        lblENum.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Employee Number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void createTable() {
        txtPayslip.setModel(new DefaultTableModel(
                null,
                new String[]{"Pay Start", "Pay End", "Total Hours", "Gross Salary", "SSS", "PhilHealth", "Pag-IBIG", "Tax", "Total Deductions", "Net Pay"}
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
