package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands from the command line interface.
 */
/**
 * Parses CLI commands and creates command objects.
 */
public class CommandParser {
    
    /**
     * Parses a command line input and returns the corresponding Command object.
     * @param line The input line to parse
     * @return The Command object
     * @throws IOException If there's an error reading input
     * @throws FlightBookingSystemException If the command is invalid
     */
    /**
     * Parses a command line input and returns the corresponding Command object.
     * @param line input line
     * @return command instance
     * @throws IOException if input reading fails
     * @throws FlightBookingSystemException if command is invalid
     */
    public static Command parse(String line) throws IOException, FlightBookingSystemException {
        try {
            String[] parts = line.split(" ", 4);
            String cmd = parts[0];
            
            // Commands with interactive input
            if (cmd.equals("addflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Flight Number: ");
                String flightNumber = reader.readLine();
                System.out.print("Origin: ");
                String origin = reader.readLine();
                System.out.print("Destination: ");
                String destination = reader.readLine();
                LocalDate departureDate = parseDateWithAttempts(reader);
                System.out.print("Capacity: ");
                int capacity = Integer.parseInt(reader.readLine());
                System.out.print("Price: ");
                double price = Double.parseDouble(reader.readLine());
                return new AddFlight(flightNumber, origin, destination, departureDate, capacity, price);
                
            } else if (cmd.equals("addcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Name: ");
                String name = reader.readLine();
                System.out.print("Phone: ");
                String phone = reader.readLine();
                System.out.print("Email (optional): ");
                String email = reader.readLine();
                return new AddCustomer(name, phone, email);
                
            } else if (cmd.equals("loadgui")) {
                return new LoadGUI();
                
            // Commands with no arguments
            } else if (parts.length == 1) {
                if (line.equals("listflights")) {
                    return new ListFlights();
                } else if (line.equals("listcustomers")) {
                    return new ListCustomers();
                } else if (line.equals("help")) {
                    return new Help();
                }
                
            // Commands with one argument
            } else if (parts.length == 2) {
                if (cmd.equals("showflight")) {
                    return new ShowFlight(parts[1]);
                } else if (cmd.equals("showcustomer")) {
                    return new ShowCustomer(parts[1]);
                } else if (cmd.equals("deleteflight")) {
                    return new DeleteFlight(parts[1]);
                } else if (cmd.equals("deletecustomer")) {
                    return new DeleteCustomer(parts[1]);
                }
                
            // Commands with two arguments
            } else if (parts.length == 3) {
                if (cmd.equals("addbooking")) {
                    return new AddBooking(parts[1], parts[2]);
                } else if (cmd.equals("cancelbooking")) {
                    return new CancelBooking(parts[1], parts[2]);
                }
                
            // Commands with three arguments
            } else if (parts.length == 4) {
                if (cmd.equals("editbooking")) {
                    return new EditBooking(parts[1], parts[2], parts[3]);
                }
            }
        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Invalid number format in command.");
        }
        
        throw new FlightBookingSystemException("Invalid command. Type 'help' to see available commands.");
    }
    
    /**
     * Prompts the user to enter a date with multiple attempts.
     * @param br BufferedReader for input
     * @param attempts Number of attempts allowed
     * @return The parsed LocalDate
     * @throws IOException If there's an error reading input
     * @throws FlightBookingSystemException If all attempts fail
     */
    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts) 
            throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher than 0");
        }
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (YYYY-MM-DD format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }
        
        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }
    
    /**
     * Prompts the user to enter a date with 3 attempts.
     * @param br BufferedReader for input
     * @return The parsed LocalDate
     * @throws IOException If there's an error reading input
     * @throws FlightBookingSystemException If all attempts fail
     */
    private static LocalDate parseDateWithAttempts(BufferedReader br) 
            throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}
