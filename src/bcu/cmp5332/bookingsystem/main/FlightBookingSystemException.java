package bcu.cmp5332.bookingsystem.main;

/**
 * FlightBookingSystemException extends {@link Exception} class and is a custom exception
 * that is used to notify the user about errors or invalid commands.
 */
/**
 * Domain-specific exception for Flight Booking System operations.
 */
public class FlightBookingSystemException extends Exception {

    /**
     * Creates a new FlightBookingSystemException with a message.
     * @param message The error message
     */
    /**
     * Creates a new exception with the given message.
     * @param message error details
     */
    public FlightBookingSystemException(String message) {
        super(message);
    }
}
