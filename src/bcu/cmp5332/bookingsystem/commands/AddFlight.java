package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Command to add a new flight to the system.
 */
/**
 * CLI command to add a new flight.
 */
public class AddFlight implements  Command {

    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final int capacity;
    private final double price;

    /**
     * Creates an AddFlight command with default capacity and price.
     * @param flightNumber The flight number
     * @param origin The origin airport/city
     * @param destination The destination airport/city
     * @param departureDate The departure date
     */
    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = 100; // Default capacity
        this.price = 50.0; // Default price
    }

    /**
     * Creates an AddFlight command with specified capacity and price.
     * @param flightNumber The flight number
     * @param origin The origin airport/city
     * @param destination The destination airport/city
     * @param departureDate The departure date
     * @param capacity The maximum number of passengers
     * @param price The base price for the flight
     */
    public AddFlight(String flightNumber, String origin, String destination, 
                     LocalDate departureDate, int capacity, double price) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.price = price;
    }
    
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        LocalDate systemDate = flightBookingSystem.getSystemDate();
        if (departureDate == null) {
            throw new FlightBookingSystemException("Departure date is required.");
        }
        if (departureDate.isBefore(systemDate)) {
            throw new FlightBookingSystemException(
                    "Departure date cannot be in the past. System date is " + systemDate + ".");
        }
        if (!departureDate.isAfter(systemDate.plusDays(2))) {
            throw new FlightBookingSystemException(
                    "Departure date must be at least 3 days after the current system date (" + systemDate
                            + "). Flights within 2 days are not allowed.");
        }
        if (capacity <= 0) {
            throw new FlightBookingSystemException("Capacity must be positive.");
        }
        if (price <= 0) {
            throw new FlightBookingSystemException("Price must be positive.");
        }

        int nextInternalId = flightBookingSystem.getFlights().size() + 1;
        
        // Create new flight
        Flight flight = new Flight(nextInternalId, flightNumber, origin, destination,
                                   departureDate, capacity, price);
        
        // Add to system
        flightBookingSystem.addFlight(flight);
        
        // Store data immediately
        try {
            FlightBookingSystemData.store(flightBookingSystem);
        } catch (IOException ex) {
            // Rollback - this is handled by throwing an exception
            throw new FlightBookingSystemException("Failed to save flight data: " + ex.getMessage() + 
                    ". Changes have been rolled back.");
        }
        
        System.out.println("Flight " + flight.getFlightNumber() + " added successfully.");
    }
}
