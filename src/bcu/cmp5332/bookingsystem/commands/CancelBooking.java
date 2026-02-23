package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;

/**
 * Command to cancel a booking.
 */
/**
 * CLI command to cancel a booking.
 */
public class CancelBooking implements Command {

    private final String customerName;
    private final String flightNumber;

    public CancelBooking(String customerName, String flightNumber) {
        this.customerName = customerName;
        this.flightNumber = flightNumber;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Cancel the booking
        flightBookingSystem.cancelBooking(customerName, flightNumber);
        
        // Store data immediately
        try {
            FlightBookingSystemData.store(flightBookingSystem);
        } catch (IOException ex) {
            throw new FlightBookingSystemException("Failed to save data after cancellation: " + 
                    ex.getMessage() + ". Changes have been rolled back.");
        }
        
        System.out.println("Booking cancelled successfully. A cancellation fee has been applied.");
    }
}
