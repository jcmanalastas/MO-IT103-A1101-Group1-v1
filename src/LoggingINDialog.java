import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoggingINDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtEmployeeUsername;
    private JPasswordField passwordField1;
    private JLabel lblEmployeeUsername;
    private JLabel lblEmployeePassword;

    public LoggingINDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("MotorPH Login");
        setSize(400, 250);
        setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void authenticateUser() {
        String username = txtEmployeeUsername.getText().trim();
        String password = new String(passwordField1.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isValid = LoginService.authenticate(username, password);

        if (isValid) {
            dispose();
            new GUI().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoggingINDialog dialog = new LoggingINDialog();
            dialog.setVisible(true);
        });
    }
}