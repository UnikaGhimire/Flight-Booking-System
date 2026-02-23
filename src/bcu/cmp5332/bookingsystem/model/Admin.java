package bcu.cmp5332.bookingsystem.model;

/**
 * Admin user class.
 * OOP: INHERITANCE, POLYMORPHISM
 */
/**
 * Admin user with management privileges.
 */
public class Admin extends User {
    
    private String department;
    
    public Admin(int id, String username, String password, String email, String phone) {
        super(id, username, password, email, phone);
        this.department = "Administration";
    }
    
    public Admin(int id, String username, String password, String email, String phone, String department) {
        super(id, username, password, email, phone);
        this.department = department;
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String getRole() { return "ADMIN"; }
    
    @Override
    public String getDetails() {
        return String.format("Admin - %s\nEmail: %s\nPhone: %s\nDepartment: %s",
                getUsername(), getEmail(), getPhone(), department);
    }
}
