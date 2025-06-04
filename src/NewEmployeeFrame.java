import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NewEmployeeFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField txtId;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtBirthday;
    private JTextField txtAddress;
    private JTextField txtPhone;
    private JTextField txtSSS;
    private JTextField txtPhilHealth;
    private JTextField txtTIN;
    private JTextField txtPagIbig;
    private JTextField txtPosition;
    private JTextField txtRate;
    private JButton btnSubmit;

    private MotorPHCSVLoader loader;
    private JTable employeeTable;

    public NewEmployeeFrame(MotorPHCSVLoader loader, JTable employeeTable) {
        this.loader = loader;
        this.employeeTable = employeeTable;

        setContentPane(mainPanel);
        setTitle("New Employee");
        setSize(400, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnSubmit.addActionListener(new SubmitButtonListener());

        setVisible(true);
    }

    private class SubmitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String line = String.join(",",
                        String.valueOf(id),
                        txtLastName.getText().trim(),
                        txtFirstName.getText().trim(),
                        txtBirthday.getText().trim(),
                        txtAddress.getText().trim(),
                        txtPhone.getText().trim(),
                        txtSSS.getText().trim(),
                        txtPhilHealth.getText().trim(),
                        txtTIN.getText().trim(),
                        txtPagIbig.getText().trim(),
                        "Active",
                        txtPosition.getText().trim(),
                        "N/A", "0", "0", "0", "0",
                        "0",
                        txtRate.getText().trim()
                );

                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter("src/Data.csv", true));
                    bw.write(line);
                    bw.newLine();
                } finally {
                    if (bw != null) {
                        bw.close();
                    }
                }

                loader.reload();

                DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
                model.addRow(new Object[]{
                        id,
                        txtFirstName.getText() + " " + txtLastName.getText(),
                        txtSSS.getText(),
                        txtPhilHealth.getText(),
                        txtTIN.getText(),
                        txtPagIbig.getText()
                });

                JOptionPane.showMessageDialog(NewEmployeeFrame.this, "Employee added!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(NewEmployeeFrame.this, "Error: " + ex.getMessage());
            }
        }
    }
}
