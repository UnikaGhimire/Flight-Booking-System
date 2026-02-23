package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;

/**
 * Interface for data persistence managers.
 */
public interface DataManager {
    
    public static final String SEPARATOR = "::";
    
    /**
     * Loads data into the flight booking system.
     * @param fbs The flight booking system
     * @throws IOException If there's an I/O error
     * @throws FlightBookingSystemException If there's a system error
     */
    /**
     * Loads data into the given system.
     * @param fbs system to populate
     * @throws IOException if storage access fails
     * @throws FlightBookingSystemException if parsing fails
     */
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException;
    
    /**
     * Stores data from the flight booking system.
     * @param fbs The flight booking system
     * @throws IOException If there's an I/O error
     */
    /**
     * Stores data from the given system.
     * @param fbs system to persist
     * @throws IOException if storage access fails
     */
    public void storeData(FlightBookingSystem fbs) throws IOException;
    
}
