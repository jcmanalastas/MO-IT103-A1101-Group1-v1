import java.io.InputStream;
import java.util.Scanner;

public class LoginService {
    public static boolean authenticate(String username, String password) {
        try (InputStream is = LoginService.class.getResourceAsStream("/login_credentials.csv");
             Scanner scanner = new Scanner(is)) {

            if (scanner.hasNextLine()) scanner.nextLine(); // Skip header

            while (scanner.hasNextLine()) {
                String[] credentials = scanner.nextLine().split(",");
                if (credentials.length >= 3 &&
                        credentials[1].trim().equals(username) &&
                        credentials[2].trim().equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}