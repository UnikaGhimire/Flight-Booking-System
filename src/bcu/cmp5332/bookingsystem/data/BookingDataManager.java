package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages persistence of booking data to/from text files.
 */
/**
 * Persists booking data to and from text storage.
 */
public class BookingDataManager implements DataManager {
    
    public final String RESOURCE = "./resources/data/bookings.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        File file = new File(RESOURCE);
        
        // Create file if it doesn't exist
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return;
        }
        
        try (Scanner sc = new Scanner(file)) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    String customerKey = properties[0];
                    String flightKey = properties[1];
                    LocalDate bookingDate = LocalDate.parse(properties[2]);
                    boolean cancelled = properties.length > 3 ? Boolean.parseBoolean(properties[3]) : false;
                    double price = properties.length > 4 ? Double.parseDouble(properties[4]) : 0.0;
                    double cancellationFee = properties.length > 5 ? Double.parseDouble(properties[5]) : 0.0;
                    
                    Customer customer;
                    Flight flight;
                    try {
                        customer = fbs.getCustomerByName(customerKey);
                    } catch (FlightBookingSystemException ex) {
                        int legacyCustomerId = Integer.parseInt(customerKey);
                        customer = fbs.getCustomerByID(legacyCustomerId);
                    }
                    try {
                        flight = fbs.getFlightByNumber(flightKey);
                    } catch (FlightBookingSystemException ex) {
                        int legacyFlightId = Integer.parseInt(flightKey);
                        flight = fbs.getFlightByID(legacyFlightId);
                    }
                    
                    // Create booking
                    Booking booking = new Booking(customer, flight, bookingDate);
                    booking.setCancelled(cancelled);
                    booking.setPrice(price);
                    booking.setCancellationFee(cancellationFee);
                    
                    // Add booking to customer
                    customer.addBooking(booking);
                    
                    // Add customer as passenger to flight if booking is not cancelled
                    if (!cancelled) {
                        try {
                            flight.addPassenger(customer);
                        } catch (FlightBookingSystemException e) {
                            // Passenger might already be added, that's okay
                        }
                    }
                    
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse booking on line " + line_idx + 
                            "\nError: " + ex);
                } catch (FlightBookingSystemException ex) {
                    // Customer or Flight not found - skip this booking
                    System.err.println("Warning: Skipping booking on line " + line_idx + ": " + ex.getMessage());
                }
                line_idx++;
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        File file = new File(RESOURCE);
        file.getParentFile().mkdirs();
        
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            // Iterate through all customers and their bookings
            for (Customer customer : fbs.getCustomers()) {
                for (Booking booking : customer.getBookings()) {
                    out.print(customer.getName() + SEPARATOR);
                    out.print(booking.getFlight().getFlightNumber() + SEPARATOR);
                    out.print(booking.getBookingDate() + SEPARATOR);
                    out.print(booking.isCancelled() + SEPARATOR);
                    out.print(booking.getPrice() + SEPARATOR);
                    out.print(booking.getCancellationFee() + SEPARATOR);
                    out.println();
                }
            }
        }
    }
    
}
