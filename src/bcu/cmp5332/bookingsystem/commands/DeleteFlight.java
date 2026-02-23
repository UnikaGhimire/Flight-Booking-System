package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;

/**
 * Command to delete (hide) a flight from the system.
 */
/**
 * CLI command to delete (soft-delete) a flight.
 */
public class DeleteFlight implements Command {

    private final String flightNumber;

    public DeleteFlight(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Delete the flight
        flightBookingSystem.deleteFlight(flightNumber);
        
        // Store data immediately
        try {
            FlightBookingSystemData.store(flightBookingSystem);
        } catch (IOException ex) {
            throw new FlightBookingSystemException("Failed to save data after deleting flight: " + 
                    ex.getMessage() + ". Changes have been rolled back.");
        }
        
        System.out.println("Flight " + flightNumber + " deleted successfully.");
    }
}
