import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MotorPHGUI extends JFrame {
    private JTextField empIDField;
    private JTextField payCoverageField;
    private JTextArea resultArea;

    public MotorPHGUI() {
        setTitle("MotorPH Employee Lookup");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel empIDLabel = new JLabel("Employee ID:");
        empIDField = new JTextField(20);

        JLabel payCoverageLabel = new JLabel("Pay Coverage:");
        payCoverageField = new JTextField(20);

        JButton searchButton = new JButton("Search");
        resultArea = new JTextArea(8, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        add(empIDLabel, gbc);
        gbc.gridx = 1;
        add(empIDField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(payCoverageLabel, gbc);
        gbc.gridx = 1;
        add(payCoverageField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        add(searchButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(scrollPane, gbc);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        setVisible(true);
    }

    private void handleSearch() {
        String inputID = empIDField.getText().trim();
        String payCoverage = payCoverageField.getText().trim();
        resultArea.setText("");

        if (inputID.isEmpty() || payCoverage.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both Employee ID and Pay Coverage.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File file = new File("Data.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Data.csv not found!", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");

                if (row.length > 0 && row[0].replace("\"", "").equals(inputID)) {
                    String empNum = row[0].replace("\"", "");
                    String empName = row[1].replace("\"", "") + " " + row[2].replace("\"", "");
                    String empAddress = row[4].replace("\"", "");

                    resultArea.setText("Employee Number: " + empNum + "\n"
                            + "Employee Name: " + empName + "\n"
                            + "Employee Address: " + empAddress + "\n"
                            + "Pay Coverage: " + payCoverage);
                    found = true;
                    break;
                }
            }

            if (!found) {
                resultArea.setText("Employee ID not found.");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file.", "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MotorPHGUI());
    }
}
