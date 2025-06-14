import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class UpdateEmployee extends JFrame {
    private JPanel UpdateEmp;
    private JLabel lblUpdateRecord;
    private JButton updateButton;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtSss;
    private JTextField txtPhilHealth;
    private JTextField txtTin;
    private JTextField txtPagIBIG;
    private JButton cancelButton;
    private JLabel lblFirstName;
    private JLabel lblLastName;
    private JLabel lblSss;
    private JLabel lblPhilHealth;
    private JLabel lblTin;
    private JLabel lblPagibig;
    private JTextField txtBirthday;
    private JLabel lblBirthday;
    private JTextField txtAddress;
    private JLabel lblAddress;
    private JTextField txtPhoneNumber;
    private JLabel lblPhone;
    private JTextField txtStatus;
    private JLabel lblStatus;
    private JLabel lblPosition;
    private JTextField txtPosition;
    private JTextField txtSupervisor;
    private JLabel lblSupervisor;
    private JLabel lblBasicSalary;
    private JTextField txtBasicSalary;
    private JTextField txtRiceSubsidy;
    private JTextField txtPhoneAllowance;
    private JTextField txtClothingAllowance;
    private JLabel lblClothingAllowance;
    private JTextField txtGrossSemi;
    private JLabel lblSemiGross;
    private JTextField txtHourlyRate;
    private JLabel lblHourlyRate;
    private JLabel lblRiceSubsidy;
    private JLabel lblPhoneAllowance;

    // Supporting data and callbacks
    private Employee originalEmp;
    private MotorPHCSVLoader csvLoader;
    private JTable employeeTable;
    private GUI.UpdateEmployeeCallback callback;

    // Constructor (with optional callback)
    public UpdateEmployee(Employee emp, MotorPHCSVLoader loader, JTable table) {
        this(emp, loader, table, null);
    }

    public UpdateEmployee(Employee emp, MotorPHCSVLoader loader, JTable table, GUI.UpdateEmployeeCallback callback) {
        this.originalEmp = emp;
        this.csvLoader = loader;
        this.employeeTable = table;
        this.callback = callback;

        // New Frame
        setTitle("Update Employee");
        setContentPane(UpdateEmp);
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Load current employee data into form
        populateFields(emp);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validate required fields
                    if (txtFirstName.getText().trim().isEmpty() ||
                            txtLastName.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(UpdateEmployee.this,
                                "First Name and Last Name are required!");
                        return;
                    }

                    // Update the CSV file directly
                    updateEmployeeInCSV();

                    // Refresh the table if callback is provided
                    if (callback != null) {
                        callback.onEmployeeUpdated();
                    }

                    JOptionPane.showMessageDialog(UpdateEmployee.this, "Employee updated successfully!");
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(UpdateEmployee.this,
                            "Error updating employee: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Cancel button
                dispose();
            }
        });
    }
    // Populate all fields with current employee data
    private void populateFields(Employee emp) {
        txtFirstName.setText(emp.getName().getFirstName());
        txtLastName.setText(emp.getName().getLastName());
        txtBirthday.setText(emp.getBirthday());
        txtAddress.setText(emp.getContact().getAddress());
        txtPhoneNumber.setText(emp.getContact().getPhone());
        txtStatus.setText(emp.getStatus());
        txtPosition.setText(emp.getPosition().getPosition());
        txtSupervisor.setText(emp.getPosition().getSupervisor());
        txtBasicSalary.setText(Double.toString(emp.getPay().getBasicSalary()));
        txtRiceSubsidy.setText(Double.toString(emp.getPay().getRiceSubsidy()));
        txtPhoneAllowance.setText(Double.toString(emp.getPay().getPhoneAllowance()));
        txtClothingAllowance.setText(Double.toString(emp.getPay().getClothingAllowance()));
        txtGrossSemi.setText(Double.toString(emp.getPay().getSemiGross()));
        txtHourlyRate.setText(String.format("%.2f", emp.getPay().calculateHourlyRate()));
        txtSss.setText(emp.getGovernmentId().getSss());
        txtPhilHealth.setText(emp.getGovernmentId().getPhilhealth());
        txtTin.setText(emp.getGovernmentId().getTin());
        txtPagIBIG.setText(emp.getGovernmentId().getPagibig());
    }
    // Locate and update the specific employee record in CSV
    private void updateEmployeeInCSV() throws IOException {
        // Read all employees from CSV
        List<Employee> allEmployees = csvLoader.getAllEmployees();

        // Find and update the specific employee
        boolean employeeFound = false;
        for (int i = 0; i < allEmployees.size(); i++) {
            Employee emp = allEmployees.get(i);
            if (emp.getEmployeeNumber() == originalEmp.getEmployeeNumber()) {
                // Update the employee object with new values
                updateEmployeeObject(emp);
                employeeFound = true;
                break;
            }
        }

        if (!employeeFound) {
            throw new RuntimeException("Employee not found in CSV file");
        }

        // Write all employees back to CSV
        writeAllEmployeesToCSV(allEmployees);
    }
    // Apply all values to the selected Employee
    private void updateEmployeeObject(Employee emp) {
        //Person
        emp.getName().setFirstName(txtFirstName.getText().trim());
        emp.getName().setLastName(txtLastName.getText().trim());
        emp.setBirthday(txtBirthday.getText().trim());
        //Contact
        emp.getContact().setAddress(txtAddress.getText().trim());
        emp.getContact().setPhone(txtPhoneNumber.getText().trim());
        //Status
        emp.setStatus(txtStatus.getText().trim());
        //Position
        emp.getPosition().setPosition(txtPosition.getText().trim());
        emp.getPosition().setSupervisor(txtSupervisor.getText().trim());
        //Payroll
        emp.getPay().setBasicSalary(Double.parseDouble(txtBasicSalary.getText().trim()));
        emp.getPay().setRiceSubsidy(Double.parseDouble(txtRiceSubsidy.getText().trim()));
        emp.getPay().setPhoneAllowance(Double.parseDouble(txtPhoneAllowance.getText().trim()));
        emp.getPay().setClothingAllowance(Double.parseDouble(txtClothingAllowance.getText().trim()));
        emp.getPay().setSemiGross(Double.parseDouble(txtGrossSemi.getText().trim()));
        //GovernmentID
        emp.getGovernmentId().setSss(txtSss.getText().trim());
        emp.getGovernmentId().setPhilhealth(txtPhilHealth.getText().trim());
        emp.getGovernmentId().setTin(txtTin.getText().trim());
        emp.getGovernmentId().setPagibig(txtPagIBIG.getText().trim());
    }
    // Save all employees back into the CSV file
    private void writeAllEmployeesToCSV(List<Employee> employees) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/Data.csv"))) {
            // Write header
            bw.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate");
            bw.newLine();

            // Write employee data
            for (Employee emp : employees) {
                if (emp.getEmployeeNumber() == originalEmp.getEmployeeNumber()) {
                    // Write updated data for employee
                    writeUpdatedEmployeeRow(bw);
                } else {
                    // Write original data for other employees
                    writeOriginalEmployeeRow(bw, emp);
                }
            }
        }
    }

    private void writeUpdatedEmployeeRow(BufferedWriter bw) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Write updated employee row based on text fields
        String[] values = {
                String.valueOf(originalEmp.getEmployeeNumber()),
                txtLastName.getText().trim(),
                txtFirstName.getText().trim(),
                txtBirthday.getText().trim(),
                txtAddress.getText().trim(),
                txtPhoneNumber.getText().trim(),
                txtSss.getText().trim(),
                txtPhilHealth.getText().trim(),
                txtTin.getText().trim(),
                txtPagIBIG.getText().trim(),
                txtStatus.getText().trim(),
                txtPosition.getText().trim(),
                txtSupervisor.getText().trim(),
                txtBasicSalary.getText().trim(),
                txtRiceSubsidy.getText().trim(),
                txtPhoneAllowance.getText().trim(),
                txtClothingAllowance.getText().trim(),
                txtGrossSemi.getText().trim(),
                txtHourlyRate.getText().trim()
        };

        for (int i = 0; i < values.length; i++) {
            String cell = values[i];
            if (cell.contains(",") || cell.contains("\"")) {
                cell = "\"" + cell.replace("\"", "\"\"") + "\"";
            }
            sb.append(cell);
            if (i < values.length - 1) {
                sb.append(",");
            }
        }

        bw.write(sb.toString());
        bw.newLine();
    }

    private void writeOriginalEmployeeRow(BufferedWriter bw, Employee emp) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Build CSV row with original values
        String[] values = {
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
                String.valueOf(emp.getPay().getBasicSalary()),
                String.valueOf(emp.getPay().getRiceSubsidy()),
                String.valueOf(emp.getPay().getPhoneAllowance()),
                String.valueOf(emp.getPay().getClothingAllowance()),
                String.valueOf(emp.getPay().getSemiGross()),
                String.format("%.2f", emp.getPay().calculateHourlyRate())
        };

        for (int i = 0; i < values.length; i++) {
            String cell = values[i];
            if (cell.contains(",") || cell.contains("\"")) {
                cell = "\"" + cell.replace("\"", "\"\"") + "\"";
            }
            sb.append(cell);
            if (i < values.length - 1) {
                sb.append(",");
            }
        }

        bw.write(sb.toString());
        bw.newLine();
    }
}