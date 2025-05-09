import java.util.Scanner;

public class MotorPH {
    public static void main(String[] args){

        String loggedInUser = "Admin";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to MotorPH "+ loggedInUser + "!");

        while (true){
            System.out.println("\n====== Main Menu ======");
            System.out.println("1. View Employee List & Details");
            System.out.println("2. Add Employee");
            System.out.println("3. Update Employee Details");
            System.out.println("4. Delete Employee Details");
            System.out.println("5. Exit");
            System.out.println("Enter option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option){
                case 1:
                    EmployeeRecords.viewEmployeeDetails();
                    break;
                case 2:
                    EmployeeRecords.createEmployeeDetails(scanner);
                    break;
                case 3:
                    EmployeeRecords.updateEmployeeDetails(scanner);
                    break;
                case 4:
                    EmployeeRecords.deleteEmployeeDetails(scanner);
                    break;
                case 5:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid Option. Please try again");
            }

        }
    }


}


