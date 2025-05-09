import java.io.*;
import java.util.*;

public class EmployeeRecords {

    public static void viewEmployeeDetails(){
        String fileName = "src/EmployeeList.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            boolean isFirstline = true;

            System.out.println("\n--- Employee Records ---");

            while ((line = br.readLine()) !=null){
                //Skips the header
                if (isFirstline){
                    isFirstline = false;
                    continue;
                }

                String[] data = line.split(",");
                String id = data[0];
                String lastName = data[1];
                String firstName = data[2];
                String birthday = data[3];

                System.out.println("\nEmployee ID: "+ id);
                System.out.println("Name: "+ firstName + " " + lastName);
                System.out.println("Birthday: " + birthday);
            }
        }catch (IOException e){
            System.out.println("Error reading employee records." + e.getMessage());
        }
    }

    public static void createEmployeeDetails(Scanner scanner) {
        System.out.println("\n=== Create a New Employee ===");
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Employee Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Employee First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Birthday (MM/DD/YYYY): ");
        String birthday = scanner.nextLine();

        try (FileWriter writer = new FileWriter("src/EmployeeList.csv", true)) {
            writer.write(id + "," + lastName + "," + firstName + "," + birthday + "\n");
            System.out.println("\nEmployee added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to CSV: " + e.getMessage());
        }
    }


    public static void updateEmployeeDetails(Scanner scanner){
        String fileName = "src/EmployeeList.csv";
        List<String[]> allLines = new ArrayList<>();

        System.out.println("Enter Employee Id to update");
        String targetId = scanner.nextLine();

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = br.readLine()) !=null){
                String[] data = line.split(",");
                if (data[0].equals(targetId)){
                    found = true;

                    System.out.println("\nEmployee Found!\n");
                    System.out.println("=== Current Employee Details === ");
                    System.out.println("Employee ID: "+ data[0]);
                    System.out.println("Last Name: " + data[1]);
                    System.out.println("First Name: "+ data[2]);
                    System.out.println("Birthday: "+ data[3]);

                    System.out.println("\nEnter new Last Name: ");
                    data[1] = scanner.nextLine();
                    System.out.println("Enter new First Name: ");
                    data[2] = scanner.nextLine();
                    System.out.println("Enter New Birthday (MM/DD/YYYY): ");
                    data[3] = scanner.nextLine();

                }
                allLines.add(data);
            }
        }catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (!found){
            System.out.println("Employee ID not found");
            return;
        }

        // Write the data back to CSV
        try (FileWriter fw = new FileWriter(fileName)){
            for (String[] line : allLines){
                fw.write(String.join("," , line) + "\n");
            }
            System.out.println("Employee record updated successfully!");
        } catch (IOException e){
            System.out.println("Error writing the file: "+ e.getMessage());
        }
    }


    public static void deleteEmployeeDetails(Scanner scanner) {
        System.out.println("\n=== Delete Employee Record ===");
        System.out.print("Enter the Employee ID to delete: ");
        String targetId = scanner.nextLine();

        String fileName = "src/EmployeeList.csv";
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    updatedLines.add(line);
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data[0].equals(targetId)) {
                    found = true;
                    continue;
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("No employee found with ID: " + targetId);
            return;
        }

        //Confirmation before deletion
        System.out.println("Are you sure you want to delete the record (Y/N)? ");
        String confirm = scanner.nextLine();

        if(!confirm.equalsIgnoreCase("Y")){
            System.out.println("Delete for employee has been cancelled");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            System.out.println("Employee record has been deleted successfully!");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

}
