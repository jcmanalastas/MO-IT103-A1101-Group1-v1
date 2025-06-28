import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NewEmployee extends JFrame {
    private JPanel NewEmpPanel;
    private JLabel lblLastName;
    private JTextField txtLastName;
    private JLabel lblFirstName;
    private JTextField txtFirstName;
    private JLabel lblBirthday;
    private JTextField txtBirthday;
    private JLabel lblAddress;
    private JTextField txtAddress;
    private JLabel lblPhoneNumber;
    private JTextField txtPhoneNumber;
    private JLabel lblSss;
    private JTextField txtSss;
    private JLabel lblPhilHealthNumber;
    private JTextField txtPhilHealthNumber;
    private JLabel lblTin;
    private JTextField txtTIN;
    private JLabel lblPagIBIGNumber;
    private JTextField txtPagIBIG;
    private JLabel lblStatus;
    private JTextField txtStatus;
    private JLabel lblPosition;
    private JTextField txtPosition;
    private JLabel lblSupervisor;
    private JTextField txtSupervisor;
    private JLabel lblBasicSalary;
    private JTextField txtBasicSalary;
    private JLabel lblRiceSubsidy;
    private JTextField txtRiceSubsidy;
    private JLabel lblPhoneAllowance;
    private JTextField txtPhoneAllowance;
    private JLabel lblClothingAllowance;
    private JTextField txtClothingAllowance;
    private JLabel lblSemiGross;
    private JTextField txtSemiGross;
    private JLabel lblHourlyRate;
    private JTextField txtHourlyRate;
    private JButton btnAddEmployee;
    private JButton btnCancel;

    private GUI.NewEmployeeCallback callback;

    // Constructor with callback
    public NewEmployee(JTable employeeTable, GUI.NewEmployeeCallback callback) {
        this.callback = callback;
        initializeFrame(employeeTable);
    }

    // Backward compatibility constructor
    public NewEmployee(JTable employeeTable) {
        initializeFrame(employeeTable);
    }

    private void initializeFrame(JTable employeeTable) {
        setTitle("Add New Employee");
        setContentPane(NewEmpPanel);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        btnAddEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validate required fields
                    if (txtLastName.getText().trim().isEmpty() ||
                            txtFirstName.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(NewEmployee.this,
                                "Last Name and First Name are required!");
                        return;
                    }

                    DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
                    int newEmployeeID = getNextEmployeeID(model);

                    Object[] newRow = {
                            newEmployeeID,
                            txtLastName.getText().trim(),
                            txtFirstName.getText().trim(),
                            txtBirthday.getText().trim(),
                            txtAddress.getText().trim(),
                            txtPhoneNumber.getText().trim(),
                            txtSss.getText().trim(),
                            txtPhilHealthNumber.getText().trim(),
                            txtTIN.getText().trim(),
                            txtPagIBIG.getText().trim(),
                            txtStatus.getText().trim(),
                            txtPosition.getText().trim(),
                            txtSupervisor.getText().trim(),
                            txtBasicSalary.getText().trim(),
                            txtRiceSubsidy.getText().trim(),
                            txtPhoneAllowance.getText().trim(),
                            txtClothingAllowance.getText().trim(),
                            txtSemiGross.getText().trim(),
                            txtHourlyRate.getText().trim()
                    };

                    // Append to CSV file first
                    appendRowToCSV(newRow);

                    // Show success message
                    JOptionPane.showMessageDialog(NewEmployee.this, "New employee added successfully!");

                    // Call callback to refresh parent data
                    if (callback != null) {
                        callback.onEmployeeAdded();
                    }

                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(NewEmployee.this, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private int getNextEmployeeID(DefaultTableModel model) {
        int maxID = 0;
        // Also check the CSV file for the highest ID
        try {
            MotorPHCSVLoader tempLoader = new MotorPHCSVLoader("src/Data.csv");
            for (Employee emp : tempLoader.getAllEmployees()) {
                maxID = Math.max(maxID, emp.getEmployeeNumber());
            }
        } catch (Exception e) {
            // If CSV reading fails, fall back to table data
            for (int i = 0; i < model.getRowCount(); i++) {
                Object value = model.getValueAt(i, 0);
                if (value instanceof Integer) {
                    maxID = Math.max(maxID, (Integer) value);
                } else {
                    try {
                        int id = Integer.parseInt(value.toString());
                        maxID = Math.max(maxID, id);
                    } catch (NumberFormatException ex) {
                        // Skip non-numeric values
                    }
                }
            }
        }
        return maxID + 1;
    }

    private void appendRowToCSV(Object[] rowData) throws IOException {
        String filePath = "src/Data.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < rowData.length; i++) {
                Object value = rowData[i];
                String cell = value == null ? "" : value.toString();

                // Escape cells containing commas or quotes
                if (cell.contains(",") || cell.contains("\"")) {
                    cell = "\"" + cell.replace("\"", "\"\"") + "\"";
                }

                sb.append(cell);
                if (i < rowData.length - 1) {
                    sb.append(",");
                }
            }

            bw.write(sb.toString());
            bw.newLine();
        }
    }
}