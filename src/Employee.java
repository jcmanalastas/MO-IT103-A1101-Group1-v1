import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee Class
 * This shows employee personal details.
 * This also provides each employee's salary information
 */

public class Employee {
    private int employeeNumber; // Employee ID
    private Person name; // Employee Name under Person class
    private String birthday;
    private ContactInfo contact;
    private GovernmentID id;
    private String status;
    private Job position;
    private Compensation pay;
    private List<Attendance> attendanceRecords;



    public Employee(int employeeNumber, Person name, String birthday, ContactInfo contact, GovernmentID id, String status, Job position, Compensation pay) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.birthday = birthday;
        this.contact = contact;
        this.id = id;
        this.status = status;
        this.position = position;
        this.pay = pay;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public GovernmentID getGovernmentId() {
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getStatus() {
        return status;
    }

    public ContactInfo getContact() {
        return contact; }

    public Job getPosition() {
        return position;
    }

    public String getFullName() {
        return name.getFirstName() + " " + name.getLastName();
    }

    public Compensation getPay(){
        return pay;
    }

    public double getMonthlySalary(){
        return pay.getMonthlyGrossSalary();
    }

}
