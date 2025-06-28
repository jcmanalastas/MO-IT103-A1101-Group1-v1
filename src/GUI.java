import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

// Main GUI class for MotorPH Payroll System
public class GUI extends JFrame {
    private JPanel main;
    private JTable tblEmployees;
    private JButton btnViewEmployee;
    private JButton btnUpdateEmp;
    private JButton btnAddEmployee;
    private JButton btnDeleteEmp;
    private JTextField txtLastName;
    private Employee currentEmployee;
    private JTextField txtFirstName;
    private JTextField txtBirthday;
    private JTextField txtAddress;
    private JTextField txtPhoneNumber;
    private JTextField txtSss;
    private JTextField txtPhilhealth;
    private JTextField txtTin;
    private JTextField txtPagIbig;
    private JTextField txtStatus;
    private JTextField txtPosition;
    private JTextField txtImmediateSupervisor;
    private JTextField txtBasicSalary;
    private JTextField txtRiceSubsidy;
    private JTextField txtPhoneAllowance;
    private JTextField txtClothingAllowance;
    private JButton btnSave;
    private JTextField txtMonthlyRate;
    private JTextField txtHourlyRate;
    private JLabel lblLastName;
    private JLabel lblFirstName;
    private JLabel lblBirthday;
    private JLabel lblAddress;
    private JLabel lblPhoneNumber;
    private JLabel lblSss;
    private JLabel lblPhilhealth;
    private JLabel lblTin;
    private JLabel lblPagIbig;
    private JLabel lblStatus;
    private JLabel lblPosition;
    private JLabel lblImmediateSupervisor;
    private JLabel lblBasicSalary;
    private JLabel lblRiceSubsidy;
    private JLabel lblPhoneAllowance;
    private JLabel lblClothingAllowance;
    private JLabel lblMonthlyRate;
    private JLabel lblHourlyRate;

    // Loads employee data from CSV file
    private MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("src/Data.csv");
    // Formatter for displaying dates
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    //Constructor: Set up GUI window
    public GUI() {
        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        refreshEmployeeTable(); // Automatically load employee list
        Attendance.loadAttendanceFromCSV(); // Load attendance data

        // Initially set disabled by default
        btnUpdateEmp.setEnabled(false);
        btnDeleteEmp.setEnabled(false);
        // View selected employee details in a new window
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
        // Add new employee
        btnAddEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewEmployeeCallback callback = new NewEmployeeCallback() {
                    public void onEmployeeAdded() {
                        csvLoader = new MotorPHCSVLoader("src/Data.csv");
                        refreshEmployeeTable(); // refresh the table after update
                    }
                };
                new NewEmployee(tblEmployees, callback);
            }
        });
        // Delete selected employee
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
                deleteEmployeeFromCSV(empID); // Remove employee from csv
                refreshEmployeeTable(); // refresh the table after update
                JOptionPane.showMessageDialog(null, "Employee deleted successfully.");
            }
        });
                btnUpdateEmp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = tblEmployees.getSelectedRow();
                        if (selectedRow == -1) {
                            JOptionPane.showMessageDialog(null, "Please select an employee.");
                            return;
                        }

                        int empId = (int) tblEmployees.getValueAt(selectedRow, 0);
                        Employee selectedEmp = csvLoader.getEmployee(empId);

                        if (selectedEmp == null) {
                            JOptionPane.showMessageDialog(null, "Employee not found.");
                            return;
                        }

                        // Populate fields
                        txtLastName.setText(selectedEmp.getName().getLastName());
                        txtFirstName.setText(selectedEmp.getName().getFirstName());
                        txtBirthday.setText(selectedEmp.getBirthday());
                        txtAddress.setText(selectedEmp.getContact().getAddress());
                        txtPhoneNumber.setText(selectedEmp.getContact().getPhone());
                        txtSss.setText(selectedEmp.getGovernmentId().getSss());
                        txtPhilhealth.setText(selectedEmp.getGovernmentId().getPhilhealth());
                        txtTin.setText(selectedEmp.getGovernmentId().getTin());
                        txtPagIbig.setText(selectedEmp.getGovernmentId().getPagibig());
                        txtRiceSubsidy.setText(String.valueOf(selectedEmp.getPay().getRiceSubsidy()));
                        txtImmediateSupervisor.setText(selectedEmp.getPosition().getSupervisor());
                        txtStatus.setText(selectedEmp.getStatus());
                        txtPosition.setText(selectedEmp.getPosition().getPosition());
                        txtBasicSalary.setText(String.valueOf(selectedEmp.getPay().getBasicSalary()));
                        txtPhoneAllowance.setText(String.valueOf(selectedEmp.getPay().getPhoneAllowance()));
                        txtClothingAllowance.setText(String.valueOf(selectedEmp.getPay().getClothingAllowance()));
                        txtMonthlyRate.setText(String.valueOf(selectedEmp.getPay().getMonthlyGrossSalary()));
                        txtHourlyRate.setText(String.valueOf(selectedEmp.getPay().getHourlyRate()));
                    }
                });
    }
    // Refreshes the employee table by reloading from csv file
    private void refreshEmployeeTable() {
        csvLoader = new MotorPHCSVLoader("src/Data.csv");
        List<Employee> employees = csvLoader.getAllEmployees();


        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Employee Number", "Last Name","First Name","SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            model.addRow(new Object[]{
                    emp.getEmployeeNumber(),
                    emp.getName().getLastName(),emp.getName().getFirstName(),
                    emp.getGovernmentId().getSss(),
                    emp.getGovernmentId().getPhilhealth(),
                    emp.getGovernmentId().getTin(),
                    emp.getGovernmentId().getPagibig()
            });
        }

        tblEmployees.setModel(model);

        tblEmployees.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean isRowSelected = tblEmployees.getSelectedRow() != -1;
                    btnUpdateEmp.setEnabled(isRowSelected);
                    btnDeleteEmp.setEnabled(isRowSelected);
                }
            }
        });

        tblEmployees.clearSelection();
        btnUpdateEmp.setEnabled(false);
        btnDeleteEmp.setEnabled(false);
    }


    // Deletes an employee from the CSV file based on their Employee ID
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
                        if (currentID == empID) // Skip the line to be deleted
                            continue;
                    } catch (NumberFormatException ignored) {
                    }
                }
                newContent.append(line).append("\n"); // Add line to new content
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(newContent.toString()); // Overwrite file with updated content
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing file: " + e.getMessage());
        }
    }
    // Callback interface for notifying when a new employee is added
    public interface NewEmployeeCallback {
        void onEmployeeAdded();
    }
    // Callback interface for notifying when an employee is updated
    public interface UpdateEmployeeCallback {
        void onEmployeeUpdated();
    }
    //main
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
    }
}

