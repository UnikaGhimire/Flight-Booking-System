package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates all data managers for loading and storing system state.
 */
public class FlightBookingSystemData {
    
    private static final List<DataManager> dataManagers = new ArrayList<>();
    
    static {
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new AdminDataManager());
        dataManagers.add(new BookingDataManager());
    }
    
    /**
     * Loads data from storage into a new system instance.
     * @return populated system
     * @throws FlightBookingSystemException if parsing fails
     * @throws IOException if storage fails
     */
    public static FlightBookingSystem load() throws FlightBookingSystemException, IOException {
        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager dm : dataManagers) {
            dm.loadData(fbs);
        }
        return fbs;
    }
    
    /**
     * Stores the current system state to storage.
     * @param fbs system to store
     * @throws IOException if storage fails
     */
    public static void store(FlightBookingSystem fbs) throws IOException {
        for (DataManager dm : dataManagers) {
            dm.storeData(fbs);
        }
    }
}
