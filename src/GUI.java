import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

    public GUI() {
        setTitle("MotorPH Payroll System");
        setContentPane(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
        setLocationRelativeTo(null);
        createTable();
    }

    private void createTable() {
        txtPayslip.setModel(new DefaultTableModel(
                null,
                new String[]{"Pay Start", "Pay End", "Total Hours", "Gross Salary", "SSS", "PhilHealth", "Pag-IBIG", "Tax", "Total Deductions", "Net Pay"}
        ));
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
