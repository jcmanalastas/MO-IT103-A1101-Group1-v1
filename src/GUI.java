import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GUI extends JFrame {
    // 1. === Fields ===
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
    private JButton btnViewEmployee;
    private JButton btnUpdateEmp;
    private JButton btnAddEmployee;
    private JButton btnDeleteEmp;
    private JTextField lastNameText;
    private JTextField firstNameText;
    private JTextField birthdayText;
    private JTextField addressText;
    private JTextField phoneText;
    private JTextField SSSText;
    private JTextField philHealthText;
    private JLabel lastNameLabel;
    private JLabel firstNameLabel;
    private JLabel birthdayLabel;
    private JLabel addressLabel;
    private JLabel phoneNumberLabel;
    private JLabel SSSLabel;
    private JLabel philHealthLabel;
    private JTextField TINText;
    private JLabel TINLabel;
    private JLabel pagIBIGLabel;
    private JTextField pagIBIGText;
    private JTextField statusText;
    private JLabel statusLabel;
    private JLabel positionLabel;
    private JTextField positionText;
    private JTextField immediateSupervisorText;
    private JLabel immediateSupervisorLabel;
    private JTextField basicSalaryText;
    private JLabel basicSalaryLabel;
    private JTextField riceSubsidyText;
    private JLabel riceSubsidyLabel;
    private JLabel phoneAllowanceLabel;
    private JTextField phoneAllowanceText;
    private JTextField clothingAllowanceText;
    private JLabel clothingAllowanceLabel;
    private JTextField grossSemiMonthlyRateText;
    private JLabel grossSemiMonthlyRateLabel;
    private JTextField hourlyRateText;
    private JLabel hourlyRateLabel;
    private JButton btnSaveChanges;

    private Employee currentlySelectedEmployee;
    private MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("src/Data.csv");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public GUI() {
        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initializeEmployeeTable();
        refreshEmployeeTable();
        Attendance.loadAttendanceFromCSV();

        btnUpdateEmp.setEnabled(false);
        btnDeleteEmp.setEnabled(false);

        // Add listeners
        addListeners();
    }

    // === LISTENERS ===
    private void addListeners() {
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
                    populateEmployeeFields(emp);
                    currentlySelectedEmployee = emp;
                }
            }
        });

        btnSaveChanges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentlySelectedEmployee == null) {
                    JOptionPane.showMessageDialog(null, "No employee selected for update.");
                    return;
                }
                try {
                    updateEmployeeObject(currentlySelectedEmployee);
                    updateEmployeeInCSV(currentlySelectedEmployee);
                    JOptionPane.showMessageDialog(null, "Employee updated successfully!");
                    refreshEmployeeTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error saving changes: " + ex.getMessage());
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
                new NewEmployee(tblEmployees, callback); // pass table and callback
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

    //Populate form
    private void populateEmployeeFields(Employee emp) {
        lastNameText.setText(emp.getName().getLastName());
        firstNameText.setText(emp.getName().getFirstName());
        birthdayText.setText(emp.getBirthday());
        addressText.setText(emp.getContact().getAddress());
        phoneText.setText(emp.getContact().getPhone());
        SSSText.setText(emp.getGovernmentId().getSss());
        philHealthText.setText(emp.getGovernmentId().getPhilhealth());
        TINText.setText(emp.getGovernmentId().getTin());
        pagIBIGText.setText(emp.getGovernmentId().getPagibig());
        statusText.setText(emp.getStatus());
        positionText.setText(emp.getPosition().getPosition());
        immediateSupervisorText.setText(emp.getPosition().getSupervisor());
        basicSalaryText.setText(String.valueOf(emp.getPay().getBasicSalary()));
        riceSubsidyText.setText(String.valueOf(emp.getPay().getRiceSubsidy()));
        phoneAllowanceText.setText(String.valueOf(emp.getPay().getPhoneAllowance()));
        clothingAllowanceText.setText(String.valueOf(emp.getPay().getClothingAllowance()));
        grossSemiMonthlyRateText.setText(String.valueOf(emp.getPay().getSemiGross()));
        hourlyRateText.setText(String.format("%.2f", emp.getPay().calculateHourlyRate()));
    }

    //Update Employee
    private void updateEmployeeObject(Employee emp) {
        emp.getName().setLastName(lastNameText.getText().trim());
        emp.getName().setFirstName(firstNameText.getText().trim());
        emp.setBirthday(birthdayText.getText().trim());
        emp.getContact().setAddress(addressText.getText().trim());
        emp.getContact().setPhone(phoneText.getText().trim());
        emp.getGovernmentId().setSss(SSSText.getText().trim());
        emp.getGovernmentId().setPhilhealth(philHealthText.getText().trim());
        emp.getGovernmentId().setTin(TINText.getText().trim());
        emp.getGovernmentId().setPagibig(pagIBIGText.getText().trim());
        emp.setStatus(statusText.getText().trim());
        emp.getPosition().setPosition(positionText.getText().trim());
        emp.getPosition().setSupervisor(immediateSupervisorText.getText().trim());
        emp.getPay().setBasicSalary(Double.parseDouble(basicSalaryText.getText().trim()));
        emp.getPay().setRiceSubsidy(Double.parseDouble(riceSubsidyText.getText().trim()));
        emp.getPay().setPhoneAllowance(Double.parseDouble(phoneAllowanceText.getText().trim()));
        emp.getPay().setClothingAllowance(Double.parseDouble(clothingAllowanceText.getText().trim()));
        emp.getPay().setSemiGross(Double.parseDouble(grossSemiMonthlyRateText.getText().trim()));
    }

    // === UPDATE IN CSV ===

    private void updateEmployeeInCSV(Employee updatedEmployee) throws IOException {
        List<Employee> allEmployees = csvLoader.getAllEmployees();
        File file = new File("src/Data.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        // Write CSV header
        writer.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate\n");

        for (Employee emp : allEmployees) {
            if (emp.getEmployeeNumber() == updatedEmployee.getEmployeeNumber()) {
                emp = updatedEmployee;
            }

            String[] data = new String[]{
                    String.valueOf(emp.getEmployeeNumber()),
                    emp.getName().getLastName(),
                    emp.getName().getFirstName(),
                    emp.getBirthday(),
                    emp.getContact().getAddress(),
                    emp.getContact().getPhone(),
                    emp.getGovernmentId().getSss(),
                    emp.getGovernmentId().getPhilhealth(),
                    emp.getGovernmentId().getTin(),
                    emp.getGovernmentId().getPagibig(),
                    emp.getStatus(),
                    emp.getPosition().getPosition(),
                    emp.getPosition().getSupervisor(),
                    String.format("%.2f", emp.getPay().getBasicSalary()),
                    String.format("%.2f", emp.getPay().getRiceSubsidy()),
                    String.format("%.2f", emp.getPay().getPhoneAllowance()),
                    String.format("%.2f", emp.getPay().getClothingAllowance()),
                    String.format("%.2f", emp.getPay().getSemiGross()),
                    String.format("%.2f", emp.getPay().calculateHourlyRate())
            };
            // Safely write escaped data
            for (int i = 0; i < data.length; i++) {
                String cell = escapeCSV(data[i]);
                writer.write(cell);
                if (i < data.length - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
        }
        writer.close();
    }
    private String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
    // === EMPLOYEE TABLE SETUP ===
    private void initializeEmployeeTable() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Employee Number", "Last Name", "First Name", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmployees.setModel(model);
        tblEmployees.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Ensure this event fires only once per selection change
                    int selectedRow = tblEmployees.getSelectedRow();
                    boolean isRowSelected = selectedRow != -1;

                    // Enable/disable update and delete buttons based on selection
                    btnUpdateEmp.setEnabled(isRowSelected);
                    btnDeleteEmp.setEnabled(isRowSelected);

                    //Logic to populate Fields
                    if (isRowSelected) {
                        int empId = (int) tblEmployees.getValueAt(selectedRow, 0); // Get employee ID from the table
                        Employee emp = csvLoader.getEmployee(empId); // Retrieve the full Employee object
                        if (emp != null) {
                            populateEmployeeFields(emp); // Populate the text fields
                            currentlySelectedEmployee = emp; // Also set currentlySelectedEmployee
                        }
                    } else {
                        // Clear text fields if no row is selected
                        clearEmployeeFields(); // You'll need to add this new helper method
                        currentlySelectedEmployee = null;
                    }
                }
            }
        });
    }
    //Helper method to clear fields
    private void clearEmployeeFields() {
        lastNameText.setText("");
        firstNameText.setText("");
        birthdayText.setText("");
        addressText.setText("");
        phoneText.setText("");
        SSSText.setText("");
        philHealthText.setText("");
        TINText.setText("");
        pagIBIGText.setText("");
        statusText.setText("");
        positionText.setText("");
        immediateSupervisorText.setText("");
        basicSalaryText.setText("");
        riceSubsidyText.setText("");
        phoneAllowanceText.setText("");
        clothingAllowanceText.setText("");
        grossSemiMonthlyRateText.setText("");
        hourlyRateText.setText("");
    }
    // === REFRESH EMPLOYEE TABLE ===
    private void refreshEmployeeTable() {
        csvLoader = new MotorPHCSVLoader("src/Data.csv");
        List<Employee> employees = csvLoader.getAllEmployees();

        DefaultTableModel model = (DefaultTableModel) tblEmployees.getModel();
        model.setRowCount(0);

        for (Employee emp : employees) {
            model.addRow(new Object[]{
                    emp.getEmployeeNumber(),
                    emp.getName().getLastName(),
                    emp.getName().getFirstName(),
                    emp.getGovernmentId().getSss(),
                    emp.getGovernmentId().getPhilhealth(),
                    emp.getGovernmentId().getTin(),
                    emp.getGovernmentId().getPagibig()
            });
        }

        tblEmployees.clearSelection();
        btnUpdateEmp.setEnabled(false);
        btnDeleteEmp.setEnabled(false);
    }

    // === DELETE EMPLOYEE FROM CSV ===
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
                    } catch (NumberFormatException ignored) {}
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

    // === INTERFACES ===
    public interface NewEmployeeCallback {
        void onEmployeeAdded();
    }

    public interface UpdateEmployeeCallback {
        void onEmployeeUpdated();
    }

    // === MAIN ===
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
    }
}
