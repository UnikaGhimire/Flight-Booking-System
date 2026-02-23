package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;

/**
 * Command to add a new customer to the system.
 */
/**
 * CLI command to add a new customer.
 */
public class AddCustomer implements Command {

    private final String name;
    private final String phone;
    private final String email;

    /**
     * Creates an AddCustomer command without email.
     * @param name The customer's name
     * @param phone The customer's phone number
     */
    public AddCustomer(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.email = "";
    }

    /**
     * Creates an AddCustomer command with email.
     * @param name The customer's name
     * @param phone The customer's phone number
     * @param email The customer's email address
     */
    public AddCustomer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int nextInternalId = flightBookingSystem.getCustomers().size() + 1;
        
        // Create new customer
        Customer customer;
        if (email != null && !email.isEmpty()) {
            customer = new Customer(nextInternalId, name, phone, email);
        } else {
            customer = new Customer(nextInternalId, name, phone);
        }
        
        // Add to system
        flightBookingSystem.addCustomer(customer);
        
        // Store data immediately
        try {
            FlightBookingSystemData.store(flightBookingSystem);
        } catch (IOException ex) {
            // Rollback - remove the customer
            throw new FlightBookingSystemException("Failed to save customer data: " + ex.getMessage() + 
                    ". Changes have been rolled back.");
        }
        
        System.out.println("Customer " + customer.getName() + " added successfully.");
    }
}
