import java.util.*;


public class MotorPH {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize CSV Loader
        MotorPHCSVLoader csvLoader = new MotorPHCSVLoader("Data.csv");

        System.out.print("Enter Employee ID: ");
        String inputID = scanner.nextLine();

        csvLoader.displayEmployee(inputID); // Call method to display details

        scanner.close();
    }
}


