import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUI extends JFrame {
    // GUI Components
    private JPanel main;
    private JTextField txtEmpNum;
    private JComboBox<String> cmbPayPeriod;
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
    private JLabel lblEmployeeName;
    private JTextField txtEmpName;
    private JTextField txtBirthday;
    private JTextField txtSSS;
    private JTextField txtPhilHealth;
    private JTextField txtTIN;
    private JTextField txtPagIbig;
    private JButton btnUpdate;
    private JButton btnDelete;

    // Business Logic Components
    private MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("src/Data.csv");
    private static Map<Integer, Map<LocalDate, Double>> attendanceMap = new HashMap<>();
    private static final String CSV_FILE = "src/AttendanceRecords.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    public GUI() {
        initializeFrame();
        initializeComponents();
        setupEventListeners();
        createTable();
    }

    private void initializeFrame() {
        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        Attendance.loadAttendanceFromCSV();
    }

    private void initializeComponents() {
        // Initialize buttons
        btnUpdate = new JButton("Update Employee");
        btnDelete = new JButton("Delete Employee");
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        // Setup radio buttons
        ButtonGroup payPeriodGroup = new ButtonGroup();
        payPeriodGroup.add(btn15);
        payPeriodGroup.add(btn30);

        // Initialize combo box with months
        cmbPayPeriod.setModel(new DefaultComboBoxModel<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        }));
    }

    private void setupEventListeners() {
        setupSearchButton();
        setupSelectionListener();
        setupUpdateButton();
        setupDeleteButton();
        setupViewAllButton();
        setupViewEmployeeButton();
        setupAddEmployeeButton();
    }

    private void setupSearchButton() {
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchAction();
            }
        });
    }

    private void handleSearchAction() {
        String empNumText = txtEmpNum.getText().trim();

        if (empNumText.isEmpty()) {
            showMessage("Please enter Employee ID");
            return;
        }

        if (!btn15.isSelected() && !btn30.isSelected()) {
            showMessage("Please select pay coverage (15th or 30th)");
            return;
        }

        try {
            int empNum = Integer.parseInt(empNumText);
            Employee employee = csvLoader.getEmployee(empNum);

            if (employee == null) {
                showMessage("Employee not found");
                return;
            }

            processPayroll(employee);
        } catch (NumberFormatException ex) {
            showMessage("Invalid employee number. Please enter digits only.");
        } catch (Exception ex) {
            showMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void processPayroll(Employee employee) {
        String monthName = cmbPayPeriod.getSelectedItem().toString();
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

        double totalHours = Attendance.getTotalWorkHours(employee.getEmployeeNumber(), fromDate, toDate);
        Payroll payroll = new Payroll();
        payroll.processPayroll(employee, fromDate, toDate, totalHours);

        updateEmployeeDisplay(employee);
        updatePayslipTable(payroll, fromDate, toDate, totalHours);
    }

    private void updateEmployeeDisplay(Employee employee) {
        lblEmpNum1.setText("Employee Number: " + employee.getEmployeeNumber());
        lblEmpName1.setText("Employee Name: " + employee.getFullName());
        lblBirthday1.setText("Birthday: " + employee.getBirthday());
    }

    private void updatePayslipTable(Payroll payroll, LocalDate fromDate, LocalDate toDate, double totalHours) {
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
                String.format("%.2f", payroll.getTotalDeductions()),
                String.format("%.2f", payroll.getNetSalary())
        });
    }

    private void setupSelectionListener() {
        tblEmployees.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleRowSelection();
            }
        });
    }

    private void handleRowSelection() {
        int selectedRow = tblEmployees.getSelectedRow();
        boolean rowSelected = selectedRow != -1;

        btnUpdate.setEnabled(rowSelected);
        btnDelete.setEnabled(rowSelected);

        if (rowSelected) {
            loadSelectedEmployee(selectedRow);
        }
    }

    private void loadSelectedEmployee(int selectedRow) {
        int empId = (int) tblEmployees.getValueAt(selectedRow, 0);
        Employee employee = csvLoader.getEmployee(empId);

        if (employee != null) {
            txtEmpName.setText(employee.getFullName());
            txtBirthday.setText(employee.getBirthday());
            txtSSS.setText(employee.getGovernmentId().getSss());
            txtPhilHealth.setText(employee.getGovernmentId().getPhilhealth());
            txtTIN.setText(employee.getGovernmentId().getTin());
            txtPagIbig.setText(employee.getGovernmentId().getPagibig());
        }
    }

    private void setupUpdateButton() {
        btnUpdate.addActionListener(e -> {
            int selectedRow = tblEmployees.getSelectedRow();
            if (selectedRow == -1) return;

            int empId = (int) tblEmployees.getValueAt(selectedRow, 0);
            Employee employee = csvLoader.getEmployee(empId);

            if (employee != null) {
                updateEmployeeData(employee);
                boolean success = csvLoader.updateEmployee(employee);

                if (success) {
                    showMessage("Employee updated successfully!");
                    refreshEmployeeTable();
                } else {
                    showMessage("Failed to update employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateEmployeeData(Employee employee) {
        employee.setFullName(txtEmpName.getText());
        employee.setBirthday(txtBirthday.getText());
        employee.getGovernmentId().setSss(txtSSS.getText());
        employee.getGovernmentId().setPhilhealth(txtPhilHealth.getText());
        employee.getGovernmentId().setTin(txtTIN.getText());
        employee.getGovernmentId().setPagibig(txtPagIbig.getText());
    }

    private void setupDeleteButton() {
        btnDelete.addActionListener(e -> {
            int selectedRow = tblEmployees.getSelectedRow();
            if (selectedRow == -1) return;

            int empId = (int) tblEmployees.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete employee " + empId + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = csvLoader.deleteEmployee(empId);
                if (success) {
                    showMessage("Employee deleted.");
                    refreshEmployeeTable();
                    clearEditFields();
                } else {
                    showMessage("Failed to delete employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setupViewAllButton() {
        btnViewAll.addActionListener(e -> refreshEmployeeTable());
    }

    private void setupViewEmployeeButton() {
        btnViewEmployee.addActionListener(e -> {
            int selectedRow = tblEmployees.getSelectedRow();
            if (selectedRow == -1) {
                showMessage("Please select an employee.");
                return;
            }

            int empId = (int) tblEmployees.getValueAt(selectedRow, 0);
            Employee emp = csvLoader.getEmployee(empId);

            if (emp == null) {
                showMessage("Selected employee not found.");
                return;
            }

            new ViewEmployeeFrame(emp);
        });
    }

    private void setupAddEmployeeButton() {
        btnAddEmployee.addActionListener(e -> new NewEmployeeFrame(csvLoader, tblEmployees));
    }

    private void refreshEmployeeTable() {
        List<Employee> employees = csvLoader.getAllEmployees();
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Name", "SSS No.", "PhilHealth No.", "TIN", "Pag-ibig No."},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Employee emp : employees) {
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

    private void clearEditFields() {
        txtEmpName.setText("");
        txtBirthday.setText("");
        txtSSS.setText("");
        txtPhilHealth.setText("");
        txtTIN.setText("");
        txtPagIbig.setText("");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void createTable() {
        txtPayslip.setModel(new DefaultTableModel(
                null,
                new String[]{
                        "Pay Start", "Pay End", "Total Hours", "Gross Salary",
                        "SSS", "PhilHealth", "Pag-IBIG", "Tax",
                        "Total Deductions", "Net Pay"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
