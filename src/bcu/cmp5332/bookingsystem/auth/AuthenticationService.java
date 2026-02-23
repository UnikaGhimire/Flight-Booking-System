package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

/**
 * Authentication service for login and registration.
 * OOP: ENCAPSULATION, POLYMORPHISM
 */
public class AuthenticationService {
    
    private FlightBookingSystem system;
    
    /**
     * Creates a new authentication service.
     * @param system flight booking system instance
     */
    public AuthenticationService(FlightBookingSystem system) {
        this.system = system;
    }
    
    /**
     * Validates credentials and returns the matching user.
     * @param username username
     * @param password password
     * @return authenticated user
     * @throws FlightBookingSystemException if credentials are invalid
     */
    public User authenticate(String username, String password) throws FlightBookingSystemException {
        
        // Check admins
        for (Admin admin : system.getAdmins()) {
            if (admin.getUsername().equals(username) && !admin.isDeleted() &&
                admin.validatePassword(password)) {
                return admin;
            }
        }
        
        // Check customers
        for (Customer customer : system.getActiveCustomers()) {
            if (customer.getUsername().equals(username) && !customer.isDeleted() &&
                customer.validatePassword(password)) {
                return customer;
            }
        }
        
        throw new FlightBookingSystemException("Invalid username or password");
    }
    
    /**
     * Registers a new customer after uniqueness checks.
     * @param username username
     * @param password password
     * @param email email address
     * @param phone phone number
     * @return created customer
     * @throws FlightBookingSystemException if validation fails
     */
    public Customer registerCustomer(String username, String password, String email, String phone) 
            throws FlightBookingSystemException {
        
        ValidationService.validateCustomerNameUnique(system, username);
        ValidationService.validateEmailUnique(system, email);
        ValidationService.validatePhoneUnique(system, phone);
        int newId = system.getCustomers().size() + 1;
        Customer customer = new Customer(newId, username, password, email, phone);
        system.addCustomer(customer);
        
        return customer;
    }
}
