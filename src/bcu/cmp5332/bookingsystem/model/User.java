package bcu.cmp5332.bookingsystem.model;

/**
 * Abstract base class for all users.
 * OOP: ABSTRACTION, ENCAPSULATION, INHERITANCE, POLYMORPHISM
 */
/**
 * Base class for all users in the system.
 */
public abstract class User {
    
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean deleted;
    
    public User(int id, String username, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.deleted = false;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    
    // POLYMORPHISM: Abstract methods
    /**
     * @return the user role name
     */
    public abstract String getRole();
    /**
     * @return a detailed, human-readable description of the user
     */
    public abstract String getDetails();
    
    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}
