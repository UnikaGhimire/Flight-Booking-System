package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;

/**
 * Command to delete (hide) a customer from the system.
 */
/**
 * CLI command to delete (soft-delete) a customer.
 */
public class DeleteCustomer implements Command {

    private final String customerName;

    public DeleteCustomer(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Delete the customer
        flightBookingSystem.deleteCustomer(customerName);
        
        // Store data immediately
        try {
            FlightBookingSystemData.store(flightBookingSystem);
        } catch (IOException ex) {
            throw new FlightBookingSystemException("Failed to save data after deleting customer: " + 
                    ex.getMessage() + ". Changes have been rolled back.");
        }
        
        System.out.println("Customer " + customerName + " deleted successfully.");
    }
}
