package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages persistence of flight data to/from text files.
 */
/**
 * Persists flight data to and from text storage.
 */
public class FlightDataManager implements DataManager {
    
    private final String RESOURCE = "./resources/data/flights.txt";
    
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
            int nextInternalId = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    int index;
                    // New format: flightNo::origin::destination::departureDate::capacity::price::deleted::
                    if (properties.length > 3) {
                        try {
                            LocalDate.parse(properties[3]);
                            index = 0;
                        } catch (Exception ex) {
                            // Legacy format: id::flightNo::origin::destination::departureDate::capacity::price::deleted::
                            if (properties.length > 4) {
                                LocalDate.parse(properties[4]);
                                index = 1;
                            } else {
                                throw new FlightBookingSystemException("Invalid flight record format on line " + line_idx);
                            }
                        }
                    } else {
                        throw new FlightBookingSystemException("Invalid flight record format on line " + line_idx);
                    }

                    String flightNumber = properties[index];
                    String origin = properties[index + 1];
                    String destination = properties[index + 2];
                    LocalDate departureDate = LocalDate.parse(properties[index + 3]);
                    int capacity = properties.length > index + 4 ? Integer.parseInt(properties[index + 4]) : 100;
                    double price = properties.length > index + 5 ? Double.parseDouble(properties[index + 5]) : 50.0;
                    boolean deleted = properties.length > index + 6 && Boolean.parseBoolean(properties[index + 6]);
                    
                    Flight flight = new Flight(nextInternalId++, flightNumber, origin, destination,
                            departureDate, capacity, price);
                    flight.setDeleted(deleted);
                    fbs.addFlight(flight);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse flight record " + properties[0] +
                            " on line " + line_idx + "\nError: " + ex);
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
            for (Flight flight : fbs.getFlights()) {
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                out.print(flight.getCapacity() + SEPARATOR);
                out.print(flight.getBasePrice() + SEPARATOR);
                out.print(flight.isDeleted() + SEPARATOR);
                out.println();
            }
        }
    }
}
