package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * Command to list all active customers in the system.
 */
/**
 * CLI command to list active customers.
 */
public class ListCustomers implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Customer> customers = flightBookingSystem.getActiveCustomers();
        
        if (customers.isEmpty()) {
            System.out.println("No customers in the system.");
            return;
        }
        
        for (Customer customer : customers) {
            int bookingCount = customer.getBookings().size();
            System.out.println(customer.getDetailsShort() + 
                    " - Bookings: " + bookingCount);
        }
        System.out.println(customers.size() + " customer(s)");
    }
}
