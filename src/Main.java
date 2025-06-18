import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoggingINDialog loginDialog = new LoggingINDialog();
            loginDialog.setVisible(true);
        });
    }
}
