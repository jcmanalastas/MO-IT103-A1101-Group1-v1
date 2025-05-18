import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.regex.*;

public class MotorPH {
    public static void main(String[] args) {
        String path = "Data.csv";
        List<String[]> data = new ArrayList<>();
        String csvRegex = "\"([^\"]*)\"|([^,]+)";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                List<String> values = new ArrayList<>();
                Matcher matcher = Pattern.compile(csvRegex).matcher(line);

                while (matcher.find()){
                    values.add(matcher.group(1) !=null ? matcher.group(1) : matcher.group(2));
                }

                data.add(values.toArray(new String[0]));
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        } catch (IOException e) {
            System.out.println("Error reading file.");
            return;
        }

        // Get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Employee ID: ");
        String inputID = scanner.nextLine();
        scanner.close();

        // Search for Employee ID


        boolean found = false;

        for (String[] row : data) { // Single loop to find matching Employee ID
            if (row.length > 0 && row[0].equals(inputID)) {
                // Print only specific columns
                System.out.printf("%-15s %-20s %-25s\n", "Employee Number", "Employee Name", "Employee Address");
                System.out.printf("%-15s %-20s %-25s\n",
                        row[0].replace("\"", ""), // Employee Number
                        row[1].replace("\"", "") + " " + row[2].replace("\"", ""), // Employee Name
                        row[4].replace("\"", "") // Employee Address
                );
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Employee ID not found.");
        }
    }
}


