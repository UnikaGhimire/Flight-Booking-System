package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.List;

/**
 * Command to list all active flights in the system.
 */
/**
 * CLI command to list available flights with dynamic prices.
 */
public class ListFlights implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Flight> flights = flightBookingSystem.getFutureFlights();

        if (flights.isEmpty()) {
            System.out.println("No flights in the system.");
            return;
        }

        for (Flight flight : flights) {
            String status = flight.getAvailableSeats() <= 0 ? "FULL" : "OPEN";
            System.out.println(flight.getDetailsShort() +
                    " - Capacity: " + flight.getCapacity() +
                    " - Available: " + flight.getAvailableSeats() +
                    " - Status: " + status +
                    " - Price: £" + String.format("%.2f", flight.getPrice(flightBookingSystem.getSystemDate())));
        }
        System.out.println(flights.size() + " flight(s)");
    }
}
