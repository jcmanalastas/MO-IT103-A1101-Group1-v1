import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GUI extends JFrame {
    private JPanel main;
    private JTextField txtEmpNum;
    private JComboBox cmbPayPeriod;
    private JRadioButton btn15;
    private JRadioButton btn30;
    private JTable txtPayslip;
    private JButton btnSearch;
    private JLabel lblEmpName1;
    private JLabel lblEmpNum1;
    private JLabel lblBirthday1;
    private JTable tblEmployees;
    private JButton btnViewAll;
    private JButton btnViewEmployee;
    private JButton btnAddEmployee;

    private MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("src/Data.csv");
    private static Map<Integer, Map<LocalDate, Double>> attendanceMap = new HashMap<>();
    private static final String CSV_FILE = "src/AttendanceRecords.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");


    public GUI() {
        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
        setLocationRelativeTo(null);
        createTable();
        Attendance.loadAttendanceFromCSV();

        ButtonGroup payPeriodGroup = new ButtonGroup();
        payPeriodGroup.add(btn15);
        payPeriodGroup.add(btn30);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empNumText = txtEmpNum.getText().trim();

                if (empNumText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter Employee ID");
                    return;
                }

                if (!btn15.isSelected() && !btn30.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please select pay coverage (15th or 30th)");
                    return;
                }
                try {
                    int empNum = Integer.parseInt(empNumText);
                    Employee employee = csvLoader.getEmployee(empNum);

                    if (employee == null) {
                        JOptionPane.showMessageDialog(null, "Employee not found");
                        return;
                    }
                    // Get selected month from combo box
                    String monthName = cmbPayPeriod.getSelectedItem().toString(); // e.g., "June"
                    int year = 2024;
                    int month = java.time.Month.valueOf(monthName.toUpperCase()).getValue();

                    LocalDate fromDate, toDate;

                    if (btn15.isSelected()) {
                        fromDate = LocalDate.of(year, month, 1);
                        toDate = LocalDate.of(year, month, 15);
                    } else {
                        fromDate = LocalDate.of(year, month, 16);
                        toDate = LocalDate.of(year, month, java.time.YearMonth.of(year, month).lengthOfMonth());
                    }

                    // Compute total hours from attendance
                    double totalHours = Attendance.getTotalWorkHours(empNum, fromDate, toDate);

                    // Process payroll
                    Payroll payroll = new Payroll();
                    payroll.processPayroll(employee, fromDate, toDate, totalHours);


                    // Display employee info
                    lblEmpNum1.setText("Employee Number: " + empNum);
                    lblEmpName1.setText("Employee Name: " + employee.getFullName());
                    lblBirthday1.setText("Birthday: " + employee.getBirthday());

                    // Fill payslip table
                    DefaultTableModel model = (DefaultTableModel) txtPayslip.getModel();
                    model.setRowCount(0); // Clear previous data
                    model.addRow(new Object[]{
                            fromDate,
                            toDate,
                            String.format("%.2f",totalHours),
                            String.format("%.2f",payroll.getGrossSalary()),
                            String.format("%.2f",payroll.getSss()),
                            String.format("%.2f",payroll.getPhilHealth()),
                            String.format("%.2f",payroll.getPagIbig()),
                            String.format("%.2f",payroll.getTax()),
                            String.format("%.2f",payroll.getSss() + payroll.getPhilHealth() + payroll.getPagIbig() + payroll.getTax()),
                            String.format("%.2f",payroll.getNetSalary()),
                    });

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid employee number. Please enter digits only.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        btnViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Employee> employees = csvLoader.getAllEmployees();

                DefaultTableModel model = new DefaultTableModel(
                        new String[]{"ID", "Name", "SSS No.", "PhilHealth No.", "TIN", "Pag-ibig No."}, 0
                );

                for (Employee emp : employees) {
                    model.addRow(new Object[]{
                            emp.getEmployeeNumber(),
                            emp.getFullName(),
                            emp.getGovernmentId().getSss(),
                            emp.getGovernmentId().getPhilhealth(),
                            emp.getGovernmentId().getTin(),
                            emp.getGovernmentId().getPagibig(),
                    });
                }

                tblEmployees.setModel(model);
            }
        });

        btnViewEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblEmployees.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an employee.");
                    return;
                }
                int empId = (int) tblEmployees.getValueAt(selectedRow, 0);
                Employee emp = csvLoader.getEmployee(empId);
                if (emp == null) {
                    JOptionPane.showMessageDialog(null, "Selected employee not found.");
                    return;
                }
                new ViewEmployeeFrame(emp);
            }
        });
        btnAddEmployee.addActionListener(e -> {
            new NewEmployeeFrame(csvLoader, tblEmployees);
        });
    }

    private void createTable() {
        txtPayslip.setModel(new DefaultTableModel(
                null,
                new String[]{"Pay Start", "Pay End", "Total Hours", "Gross Salary", "SSS", "PhilHealth", "Pag-IBIG", "Tax", "Total Deductions", "Net Pay"}));
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}