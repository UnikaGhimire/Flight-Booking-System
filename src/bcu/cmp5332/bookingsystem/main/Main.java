package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.auth.AuthenticationService;
import bcu.cmp5332.bookingsystem.auth.Session;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CLI entry point for the Flight Booking System.
 * Handles login/register flow and delegates commands to {@link CommandParser}.
 */
public class Main {

    /**
     * Starts the application in CLI mode.
     * @param args command line args (unused)
     * @throws IOException if data loading fails
     * @throws FlightBookingSystemException if the system cannot initialize
     */
    public static void main(String[] args) throws IOException, FlightBookingSystemException {
        System.out.println("Loading Flight Booking System...");
        FlightBookingSystem fbs = FlightBookingSystemData.load();
        System.out.println("System loaded successfully!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            User user = promptLoginOrRegister(fbs, reader);
            if (user == null) {
                FlightBookingSystemData.store(fbs);
                System.out.println("Goodbye!");
                return;
            }

            Session.getInstance().login(user);
            System.out.println("Logged in as " + user.getUsername() + " (" + user.getRole() + ")");
            System.out.println("Type 'help' for commands, 'logout' to sign out, or 'exit' to quit.");

            while (Session.getInstance().isLoggedIn()) {
                System.out.print("fbs> ");
                String line = reader.readLine();
                if (line == null) {
                    FlightBookingSystemData.store(fbs);
                    return;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.equalsIgnoreCase("logout")) {
                    Session.getInstance().logout();
                    System.out.println("Logged out.");
                    break;
                }
                if (line.equalsIgnoreCase("exit")) {
                    FlightBookingSystemData.store(fbs);
                    System.out.println("Goodbye!");
                    return;
                }

                try {
                    CommandParser.parse(line).execute(fbs);
                } catch (FlightBookingSystemException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Prompts the user to login/register or exit.
     * @param fbs system instance
     * @param reader input reader
     * @return authenticated user or null if user chooses to exit
     * @throws IOException if reading from input fails
     */
    private static User promptLoginOrRegister(FlightBookingSystem fbs, BufferedReader reader)
            throws IOException {
        AuthenticationService authService = new AuthenticationService(fbs);

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Login");
            System.out.println("2. Register (Customer)");
            System.out.println("3. Exit");
            System.out.print("> ");

            String choice = reader.readLine();
            if (choice == null) {
                return null;
            }
            choice = choice.trim();

            if (choice.equals("1")) {
                System.out.print("Username: ");
                String username = reader.readLine();
                System.out.print("Password: ");
                String password = reader.readLine();
                try {
                    return authService.authenticate(username, password);
                } catch (FlightBookingSystemException ex) {
                    System.out.println("Login failed: " + ex.getMessage());
                }
            } else if (choice.equals("2")) {
                System.out.print("Username: ");
                String username = reader.readLine();
                System.out.print("Email: ");
                String email = reader.readLine();
                System.out.print("Phone: ");
                String phone = reader.readLine();
                System.out.print("Password: ");
                String password = reader.readLine();
                System.out.print("Confirm Password: ");
                String confirm = reader.readLine();

                if (!password.equals(confirm)) {
                    System.out.println("Passwords do not match.");
                    continue;
                }

                try {
                    User user = authService.registerCustomer(username, password, email, phone);
                    FlightBookingSystemData.store(fbs);
                    System.out.println("Registration successful. You are now logged in.");
                    return user;
                } catch (FlightBookingSystemException ex) {
                    System.out.println("Registration failed: " + ex.getMessage());
                }
            } else if (choice.equals("3")) {
                return null;
            } else {
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
}
