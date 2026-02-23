package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to display detailed information about a specific customer.
 */
/**
 * CLI command to display a customer's details.
 */
public class ShowCustomer implements Command {

    private final String customerName;

    public ShowCustomer(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByName(customerName);
        
        if (customer.isDeleted()) {
            System.out.println("This customer has been deleted.");
            return;
        }
        
        System.out.println(customer.getDetailsLong());
    }
}
