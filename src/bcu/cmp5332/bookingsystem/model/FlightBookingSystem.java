package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main system model.
 * OOP: ENCAPSULATION, COMPOSITION
 */
/**
 * Core system model holding flights, customers, and admins.
 */
public class FlightBookingSystem {
    
    private final LocalDate systemDate = LocalDate.now();
    
    private final Map<String, Customer> customers = new TreeMap<>();
    private final Map<String, Flight> flights = new TreeMap<>();
    private final Map<String, Admin> admins = new TreeMap<>();
    
    public LocalDate getSystemDate() { return systemDate; }
    
    public List<Flight> getFlights() {
        return new ArrayList<>(flights.values());
    }
    
    /**
     * @return list of non-deleted flights
     */
    public List<Flight> getActiveFlights() {
        return flights.values().stream()
                .filter(f -> !f.isDeleted())
                .collect(Collectors.toList());
    }
    
    /**
     * @return list of non-deleted flights that have not departed
     */
    public List<Flight> getFutureFlights() {
        return flights.values().stream()
                .filter(f -> !f.isDeleted() && !f.hasDeparted(systemDate))
                .collect(Collectors.toList());
    }
    
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    /**
     * @return list of non-deleted customers
     */
    public List<Customer> getActiveCustomers() {
        return customers.values().stream()
                .filter(c -> !c.isDeleted())
                .collect(Collectors.toList());
    }
    
    public List<Admin> getAdmins() {
        return new ArrayList<>(admins.values());
    }

    /**
     * @return all bookings across all customers
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        for (Customer customer : customers.values()) {
            bookings.addAll(customer.getBookings());
        }
        bookings.sort(Comparator.comparing(Booking::getBookingDate));
        return bookings;
    }

    private String key(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    public Flight getFlightByNumber(String flightNumber) throws FlightBookingSystemException {
        Flight flight = flights.get(key(flightNumber));
        if (flight == null) {
            throw new FlightBookingSystemException("No flight with number " + flightNumber);
        }
        return flight;
    }

    public Customer getCustomerByName(String name) throws FlightBookingSystemException {
        Customer customer = customers.get(key(name));
        if (customer == null) {
            throw new FlightBookingSystemException("No customer with name " + name);
        }
        return customer;
    }

    public Admin getAdminByUsername(String username) throws FlightBookingSystemException {
        Admin admin = admins.get(key(username));
        if (admin == null) {
            throw new FlightBookingSystemException("No admin with username " + username);
        }
        return admin;
    }

    // Legacy lookup support for old persisted booking rows that stored numeric IDs.
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        for (Flight flight : flights.values()) {
            if (flight.getId() == id) {
                return flight;
            }
        }
        throw new FlightBookingSystemException("No flight with legacy numeric key " + id);
    }

    // Legacy lookup support for old persisted booking rows that stored numeric IDs.
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        for (Customer customer : customers.values()) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        throw new FlightBookingSystemException("No customer with legacy numeric key " + id);
    }

    // Legacy lookup support for old persisted admin rows that stored numeric IDs.
    public Admin getAdminByID(int id) throws FlightBookingSystemException {
        for (Admin admin : admins.values()) {
            if (admin.getId() == id) {
                return admin;
            }
        }
        throw new FlightBookingSystemException("No admin with legacy numeric key " + id);
    }
    
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        String flightKey = key(flight.getFlightNumber());
        if (flightKey.isEmpty()) {
            throw new FlightBookingSystemException("Flight number cannot be empty");
        }
        if (flights.containsKey(flightKey)) {
            throw new FlightBookingSystemException("Duplicate flight number");
        }
        flights.put(flightKey, flight);
    }
    
    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        String customerKey = key(customer.getName());
        if (customerKey.isEmpty()) {
            throw new FlightBookingSystemException("Customer name cannot be empty");
        }
        if (customers.containsKey(customerKey)) {
            throw new FlightBookingSystemException("Duplicate customer name");
        }
        customers.put(customerKey, customer);
    }
    
    public void addAdmin(Admin admin) throws FlightBookingSystemException {
        String adminKey = key(admin.getUsername());
        if (adminKey.isEmpty()) {
            throw new FlightBookingSystemException("Admin username cannot be empty");
        }
        if (admins.containsKey(adminKey)) {
            throw new FlightBookingSystemException("Duplicate admin username");
        }
        admins.put(adminKey, admin);
    }
    
    public void deleteFlight(String flightNumber) throws FlightBookingSystemException {
        getFlightByNumber(flightNumber).setDeleted(true);
    }
    
    public void deleteCustomer(String customerName) throws FlightBookingSystemException {
        getCustomerByName(customerName).setDeleted(true);
    }
    
    public void issueBooking(Customer customer, Flight flight, LocalDate bookingDate) 
            throws FlightBookingSystemException {
        
        if (flight.hasDeparted(systemDate)) {
            throw new FlightBookingSystemException("Flight has departed");
        }
        
        flight.addPassenger(customer);
        
        double price = flight.getPrice(bookingDate);
        
        Booking booking = new Booking(customer, flight, bookingDate);
        booking.setPrice(price);
        
        customer.addBooking(booking);
    }
    
    public void cancelBooking(String customerName, String flightNumber) throws FlightBookingSystemException {
        Customer customer = getCustomerByName(customerName);
        Flight flight = getFlightByNumber(flightNumber);
        
        customer.cancelBookingForFlight(flight);
        flight.removePassenger(customer);
    }
    
    public void updateBooking(String customerName, String oldFlightNumber, String newFlightNumber) 
            throws FlightBookingSystemException {
        
        Customer customer = getCustomerByName(customerName);
        Flight oldFlight = getFlightByNumber(oldFlightNumber);
        Flight newFlight = getFlightByNumber(newFlightNumber);
        
        Booking oldBooking = null;
        for (Booking b : customer.getBookings()) {
            if (b.getFlight().getFlightNumber().equalsIgnoreCase(oldFlight.getFlightNumber()) && !b.isCancelled()) {
                oldBooking = b;
                break;
            }
        }
        
        if (oldBooking == null) {
            throw new FlightBookingSystemException("No active booking found");
        }
        
        if (newFlight.getAvailableSeats() <= 0) {
            throw new FlightBookingSystemException("New flight is full");
        }
        
        oldBooking.setCancelled(true);
        oldBooking.setCancellationFee(oldBooking.getPrice() * 0.10);
        oldFlight.removePassenger(customer);
        
        issueBooking(customer, newFlight, systemDate);
    }
}
