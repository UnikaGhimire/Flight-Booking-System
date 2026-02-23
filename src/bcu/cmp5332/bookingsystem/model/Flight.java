package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a flight in the booking system.
 * Contains flight information and manages passengers.
 */
/**
 * Represents a flight and manages passenger bookings and pricing.
 */
public class Flight {
    
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private int capacity;
    private double basePrice;
    private boolean deleted;

    private final Set<Customer> passengers;

    /**
     * Creates a new Flight instance with basic information.
     * @param id The unique identifier for the flight
     * @param flightNumber The flight number
     * @param origin The origin airport/city
     * @param destination The destination airport/city
     * @param departureDate The departure date
     */
    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = 100; // Default capacity
        this.basePrice = 50.0; // Default base price
        this.deleted = false;
        
        passengers = new HashSet<>();
    }

    /**
     * Creates a new Flight instance with capacity and price.
     * @param id The unique identifier for the flight
     * @param flightNumber The flight number
     * @param origin The origin airport/city
     * @param destination The destination airport/city
     * @param departureDate The departure date
     * @param capacity The maximum number of passengers
     * @param basePrice The base price for the flight
     */
    public Flight(int id, String flightNumber, String origin, String destination, 
                  LocalDate departureDate, int capacity, double basePrice) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.deleted = false;
        
        passengers = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * Gets the maximum capacity of the flight.
     * @return The capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the flight.
     * @param capacity The capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the base price of the flight.
     * @return The base price
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the base price of the flight.
     * @param basePrice The price to set
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Checks if this flight has been deleted.
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deletion status of this flight.
     * @param deleted true to mark as deleted, false otherwise
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the list of passengers on this flight.
     * @return List of passengers
     */
    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }

    /**
     * Gets the number of available seats.
     * @return Number of seats available
     */
    public int getAvailableSeats() {
        return capacity - passengers.size();
    }

    /**
     * Calculates the current price based on booking date, days until departure, and capacity.
     * @param bookingDate The date the booking is being made
     * @return The calculated price
     */
    /**
     * Calculates dynamic price based on booking date and occupancy.
     * @param bookingDate booking date
     * @return calculated price
     */
    public double getPrice(LocalDate bookingDate) {
        long daysUntilDeparture = ChronoUnit.DAYS.between(bookingDate, departureDate);
        int seatsLeft = getAvailableSeats();
        
        // Base calculation
        double price = basePrice;
        
        // Price increases as departure date approaches
        if (daysUntilDeparture <= 7) {
            price *= 1.5; // 50% increase for last week
        } else if (daysUntilDeparture <= 14) {
            price *= 1.3; // 30% increase for last 2 weeks
        } else if (daysUntilDeparture <= 30) {
            price *= 1.1; // 10% increase for last month
        }
        
        // Price increases as seats fill up
        double occupancyRate = 1.0 - ((double) seatsLeft / capacity);
        if (occupancyRate > 0.9) {
            price *= 1.4; // 40% increase when over 90% full
        } else if (occupancyRate > 0.75) {
            price *= 1.25; // 25% increase when over 75% full
        } else if (occupancyRate > 0.5) {
            price *= 1.1; // 10% increase when over 50% full
        }
        
        return Math.round(price * 100.0) / 100.0; // Round to 2 decimal places
    }

    /**
     * Gets the current price (convenience method using system date).
     * @return The current price
     */
    /**
     * Returns the base price for the flight.
     * @return base price
     */
    public double getPrice() {
        return basePrice;
    }
	
    /**
     * Gets a short description of the flight.
     * @return A string with basic flight details
     */
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Flight " + flightNumber + " - " + origin + " to "
                + destination + " on " + departureDate.format(dtf);
    }

    /**
     * Gets a detailed description of the flight including passengers.
     * @return A string with detailed flight information
     */
    public String getDetailsLong() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Flight Details:\n");
        sb.append("Flight Number: ").append(flightNumber).append("\n");
        sb.append("Origin: ").append(origin).append("\n");
        sb.append("Destination: ").append(destination).append("\n");
        sb.append("Departure Date: ").append(departureDate.format(dtf)).append("\n");
        sb.append("Capacity: ").append(capacity).append("\n");
        sb.append("Base Price: £").append(String.format("%.2f", basePrice)).append("\n");
        sb.append("Passengers: ").append(passengers.size()).append("/").append(capacity).append("\n");
        sb.append("Available Seats: ").append(getAvailableSeats()).append("\n");
        
        if (!passengers.isEmpty()) {
            sb.append("\nPassenger List:\n");
            for (Customer passenger : passengers) {
                sb.append("  - ").append(passenger.getDetailsShort()).append("\n");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Adds a passenger to this flight.
     * @param passenger The customer to add as a passenger
     * @throws FlightBookingSystemException if the flight is at full capacity
     */
    public void addPassenger(Customer passenger) throws FlightBookingSystemException {
        if (passengers.size() >= capacity) {
            throw new FlightBookingSystemException("Cannot add passenger. Flight is at full capacity.");
        }
        if (passengers.contains(passenger)) {
            throw new FlightBookingSystemException("Customer already booked on this flight.");
        }
        passengers.add(passenger);
    }

    /**
     * Removes a passenger from this flight.
     * @param passenger The customer to remove
     * @throws FlightBookingSystemException if the passenger is not on this flight
     */
    public void removePassenger(Customer passenger) throws FlightBookingSystemException {
        if (!passengers.remove(passenger)) {
            throw new FlightBookingSystemException("Passenger not found on this flight.");
        }
    }

    /**
     * Checks if the flight has departed based on the system date.
     * @param systemDate The current system date
     * @return true if the flight has departed, false otherwise
     */
    public boolean hasDeparted(LocalDate systemDate) {
        return departureDate.isBefore(systemDate);
    }

}
