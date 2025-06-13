import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
    private JButton btnUpdateEmp;
    private JButton btnAddEmployee;
    private JButton btnDeleteEmp;

    private MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("src/Data.csv");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public GUI() {
        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        createPayslipTable();
        initializeEmployeeTable();
        Attendance.loadAttendanceFromCSV();

        ButtonGroup payPeriodGroup = new ButtonGroup();
        payPeriodGroup.add(btn15);
        payPeriodGroup.add(btn30);

        btnSearch.addActionListener(new ActionListener() {
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

                        String monthName = cmbPayPeriod.getSelectedItem().toString();
                        int year = 2024;
                        int month = java.time.Month.valueOf(monthName.toUpperCase()).getValue();

                        LocalDate fromDate = btn15.isSelected()
                                ? LocalDate.of(year, month, 1)
                                : LocalDate.of(year, month, 16);
                        LocalDate toDate = btn15.isSelected()
                                ? LocalDate.of(year, month, 15)
                                : LocalDate.of(year, month, java.time.YearMonth.of(year, month).lengthOfMonth());

                        double totalHours = Attendance.getTotalWorkHours(empNum, fromDate, toDate);

                        Payroll payroll = new Payroll();
                        payroll.processPayroll(employee, fromDate, toDate, totalHours);

                        lblEmpNum1.setText("Employee Number: " + empNum);
                        lblEmpName1.setText("Employee Name: " + employee.getFullName());
                        lblBirthday1.setText("Birthday: " + employee.getBirthday());

                        DefaultTableModel model = (DefaultTableModel) txtPayslip.getModel();
                        model.setRowCount(0);
                        model.addRow(new Object[]{
                                fromDate,
                                toDate,
                                String.format("%.2f", totalHours),
                                String.format("%.2f", payroll.getGrossSalary()),
                                String.format("%.2f", payroll.getSss()),
                                String.format("%.2f", payroll.getPhilHealth()),
                                String.format("%.2f", payroll.getPagIbig()),
                                String.format("%.2f", payroll.getTax()),
                                String.format("%.2f", payroll.getSss() + payroll.getPhilHealth() + payroll.getPagIbig() + payroll.getTax()),
                                String.format("%.2f", payroll.getNetSalary())
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
            public void actionPerformed(ActionEvent e) {
                csvLoader = new MotorPHCSVLoader("src/Data.csv");
                List<Employee> employees = csvLoader.getAllEmployees();

                DefaultTableModel model = new DefaultTableModel(
                        new String[]{"ID", "Name", "SSS No.", "PhilHealth No.", "TIN", "Pag-ibig No."}, 0
                ) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                for (int i = 0; i < employees.size(); i++) {
                    Employee emp = employees.get(i);
                    model.addRow(new Object[]{
                            emp.getEmployeeNumber(),
                            emp.getFullName(),
                            emp.getGovernmentId().getSss(),
                            emp.getGovernmentId().getPhilhealth(),
                            emp.getGovernmentId().getTin(),
                            emp.getGovernmentId().getPagibig()
                    });
                }

                tblEmployees.setModel(model);
            }
        });

        btnViewEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tblEmployees.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an employee.");
                    return;
                }
                int empId = (int) tblEmployees.getValueAt(row, 0);
                Employee emp = csvLoader.getEmployee(empId);
                if (emp != null) {
                    new ViewEmployeeFrame(emp);
                }
            }
        });

        btnUpdateEmp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tblEmployees.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an employee.");
                    return;
                }
                int empId = (int) tblEmployees.getValueAt(row, 0);
                Employee emp = csvLoader.getEmployee(empId);
                if (emp != null) {
                    new UpdateEmployee(emp, csvLoader, tblEmployees, new UpdateEmployeeCallback() {
                        public void onEmployeeUpdated() {
                            refreshEmployeeTable();
                        }
                    });
                }
            }
        });

        btnAddEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewEmployeeCallback callback = new NewEmployeeCallback() {
                    public void onEmployeeAdded() {
                        csvLoader = new MotorPHCSVLoader("src/Data.csv");
                        refreshEmployeeTable();
                    }
                };
                new NewEmployee(tblEmployees, callback);
            }
        });

        btnDeleteEmp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tblEmployees.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an employee to delete.");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                int empID = (int) tblEmployees.getValueAt(row, 0);
                deleteEmployeeFromCSV(empID);
                refreshEmployeeTable();
                JOptionPane.showMessageDialog(null, "Employee deleted successfully.");
            }
        });
    }

    private void createPayslipTable() {
        txtPayslip.setModel(new DefaultTableModel(
                null,
                new String[]{"Pay Start", "Pay End", "Total Hours", "Gross Salary", "SSS", "PhilHealth", "Pag-IBIG", "Tax", "Total Deductions", "Net Pay"}
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void initializeEmployeeTable() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Name", "SSS No.", "PhilHealth No.", "TIN", "Pag-ibig No."}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmployees.setModel(model);
    }

    private void refreshEmployeeTable() {

    }

    private void deleteEmployeeFromCSV(int empID) {
        String filePath = "src/Data.csv";
        StringBuilder newContent = new StringBuilder();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                if (columns.length > 0) {
                    try {
                        int currentID = Integer.parseInt(columns[0].trim());
                        if (currentID == empID) continue;
                    } catch (NumberFormatException ignored) {
                    }
                }
                newContent.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(newContent.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing file: " + e.getMessage());
        }
    }
    public interface NewEmployeeCallback {
        void onEmployeeAdded();
    }
    public interface UpdateEmployeeCallback {
        void onEmployeeUpdated();
    }
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
    }
}
