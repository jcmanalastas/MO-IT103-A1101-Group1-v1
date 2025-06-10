import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

    private Employee originalEmp;
    private int employeeRowIndex;
    private JTable employeeTable;

    public UpdateEmployee(Employee emp, MotorPHCSVLoader loader, JTable table) {
        this.originalEmp = emp;

        setTitle("Update Employee");
        setContentPane(UpdateEmp);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Fill in existing data
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


        employeeRowIndex = findRowIndexByEmployeeNumber(table, emp.getEmployeeNumber());

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();

                    if (employeeRowIndex >= 0) {
                        int firstNameCol = 2;
                        int lastNameCol = 1;
                        int birthdayCol =3;
                        int addressCol = 4;
                        int phoneNumberCol = 5;
                        int statusCol = 10;
                        int positionCol = 11;
                        int supervisorCol = 12;
                        int basicCol = 13;
                        int riceCol = 14;
                        int phoneAllowanceCol = 15;
                        int clothingAllowance = 16;
                        int grossSemi = 17;
                        int hourlyRateCol = 18;
                        int sssCol = 6;
                        int philhealthCol = 7;
                        int tinCol = 8;
                        int pagibigCol = 9;


                        model.setValueAt(txtFirstName.getText().trim(), employeeRowIndex, firstNameCol);
                        model.setValueAt(txtLastName.getText().trim(), employeeRowIndex, lastNameCol);
                        model.setValueAt(txtSss.getText().trim(), employeeRowIndex, sssCol);
                        model.setValueAt(txtPhilHealth.getText().trim(), employeeRowIndex, philhealthCol);
                        model.setValueAt(txtTin.getText().trim(), employeeRowIndex, tinCol);
                        model.setValueAt(txtPagIBIG.getText().trim(), employeeRowIndex, pagibigCol);
                        model.setValueAt(txtBirthday.getText().trim(),employeeRowIndex,birthdayCol);
                        model.setValueAt(txtAddress.getText().trim(),employeeRowIndex,addressCol);
                        model.setValueAt(txtPhoneNumber.getText().trim(),employeeRowIndex,phoneNumberCol);
                        model.setValueAt(txtStatus.getText().trim(),employeeRowIndex,statusCol);
                        model.setValueAt(txtPosition.getText().trim(),employeeRowIndex,positionCol);
                        model.setValueAt(txtSupervisor.getText().trim(),employeeRowIndex,supervisorCol);
                        model.setValueAt(txtBasicSalary.getText().trim(),employeeRowIndex,basicCol);
                        model.setValueAt(txtRiceSubsidy.getText().trim(),employeeRowIndex,riceCol);
                        model.setValueAt(txtPhoneAllowance.getText().trim(),employeeRowIndex,phoneAllowanceCol);
                        model.setValueAt(txtClothingAllowance.getText().trim(),employeeRowIndex,clothingAllowance);
                        model.setValueAt(txtGrossSemi.getText().trim(),employeeRowIndex,grossSemi);
                        model.setValueAt(txtHourlyRate.getText().trim(),employeeRowIndex,hourlyRateCol);

                        writeTableToCSV(table);
                        loader.reload();

                        DefaultTableModel mainModel = (DefaultTableModel) employeeTable.getModel();
                        mainModel.setRowCount(0);

                        JOptionPane.showMessageDialog(UpdateEmployee.this, "Employee updated successfully!");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(UpdateEmployee.this, "Employee not found in table.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(UpdateEmployee.this, "Error updating employee: " + ex.getMessage());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private int findRowIndexByEmployeeNumber(JTable table, int empNum) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            Object val = model.getValueAt(row, 0);
            if (val != null && val instanceof Integer && ((Integer) val) == empNum) {
                return row;
            }
            if (val != null && val.toString().equals(String.valueOf(empNum))) {
                return row;
            }
        }
        return -1;
    }
    private void writeTableToCSV(JTable table) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/Data.csv"));
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for (int i = 0; i < model.getColumnCount(); i++) {
            bw.write(model.getColumnName(i));
            if (i < model.getColumnCount() - 1) {
                bw.write(",");
            }
        }
        bw.newLine();

        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(row, col);
                String cell = value == null ? "" : value.toString();
                if (cell.contains(",")) {
                    cell = "\"" + cell + "\"";
                }
                bw.write(cell);
                if (col < model.getColumnCount() - 1) {
                    bw.write(",");
                }
            }
            bw.newLine();
        }
        bw.close();
    }
}
