import java.io.*;
import java.time.LocalDate;

/**
 * Employee Class
 * This shows employee personal details.
 * This also provides each employee's salary information
 */

public class Employee {
    private int employeeNumber; // Employee ID
    private Person name; // Employee Name under Person class
    private LocalDate birthday;
    private ContactInfo contact;
    private GovernmentID id;
    private String status;
    private Job position;

    public String getFullName()
    {
        return name.getFirstName() +" " + name.getLastName();\
    }

}