import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MotorPH_GUI extends JFrame {
    private JPanel mainPanel;
    private JTextField empID;
    private JTextField payCoverageField;
    private JButton btnclick;
    private JScrollPane resultarea;

    public MotorPH_GUI() {
        btnclick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredEmpID = empID.getText().trim();
                String payCoverage = payCoverageField.getText().trim();

                JOptionPane.showMessageDialog(btnclick,
                        "Employee ID: " + enteredEmpID + "\nPay Coverage: " + payCoverage,
                        "Entered Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
}
    public static void main(String[] args) {
        MotorPH_GUI h = new MotorPH_GUI();
        h.setContentPane(h.mainPanel);
        h.setTitle("MotorPH Employee Lookup");
        h.setBounds(600, 200, 300, 400);
        h.setSize(300, 400);
        h.setVisible(true);
        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}