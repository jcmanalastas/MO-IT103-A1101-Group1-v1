import java.util.List;

/**
 * Employee Class
 * This shows employee personal details.
 * This also provides each employee's salary information
 */
public class Employee {

    private int employeeNumber;
    private Person name;
    private String birthday;
    private ContactInfo contact;
    private GovernmentID id;
    private String status;
    private Job position;
    private Compensation pay;

    public Employee(int employeeNumber, Person name, String birthday, ContactInfo contact,
                    GovernmentID id, String status, Job position, Compensation pay) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.birthday = birthday;
        this.contact = contact;
        this.id = id;
        this.status = status;
        this.position = position;
        this.pay = pay;
    }

    // Getters
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public Person getName() {
        return name;
    }

    public String getFullName() {
        return name.getFirstName() + " " + name.getLastName();
    }

    public String getBirthday() {
        return birthday;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public GovernmentID getGovernmentId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Job getPosition() {
        return position;
    }

    public Compensation getPay() {
        return pay;
    }

    public double getMonthlySalary() {
        return pay.getMonthlyGrossSalary();
    }



    // Setters
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
