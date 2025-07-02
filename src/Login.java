import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Login extends JFrame {
    private JTextField txtusername;
    private JPasswordField txtpassword;
    private JButton logInButton;
    private JLabel lblusername;
    private JLabel lblpassword;
    private JPanel loginPanel;

    public Login() {
        setTitle("MotorPH Login");
        setContentPane(loginPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtusername.getText().trim();
                String password = new String(txtpassword.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter username and password.");
                    return;
                }

                if (validateCredentials(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose(); // close login window
                    new GUI().setVisible(true); // open main app
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            }
        });
    }
    private boolean validateCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/Usernamepassword.csv"))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] column = line.split(",", -1);
                if (column.length >= 5) {
                    String fileUsername = column[3].trim();
                    String filePassword = column[4].trim();
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading login file: " + e.getMessage());
        }
        return false;
    }
    public static void main(String[] args) {
        new Login(); // start with login
    }
}
