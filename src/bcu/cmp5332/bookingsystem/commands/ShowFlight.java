package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to display detailed information about a specific flight.
 */
/**
 * CLI command to display a flight's details.
 */
public class ShowFlight implements Command {

    private final String flightNumber;

    public ShowFlight(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Flight flight = flightBookingSystem.getFlightByNumber(flightNumber);
        
        if (flight.isDeleted()) {
            System.out.println("This flight has been deleted.");
            return;
        }
        
        System.out.println(flight.getDetailsLong());
    }
}
