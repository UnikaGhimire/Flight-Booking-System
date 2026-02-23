package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Represents a booking made by a customer for a flight.
 * Contains information about the customer, flight, booking date, and cancellation status.
 */
/**
 * Represents a customer booking for a flight.
 */
public class Booking {
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private boolean cancelled;
    private double price;
    private double cancellationFee;

    /**
     * Creates a new Booking instance.
     * @param customer The customer who made the booking
     * @param flight The flight being booked
     * @param bookingDate The date the booking was made
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.cancelled = false;
        this.cancellationFee = 0.0;
        // Calculate price based on flight's current price at booking time
        this.price = flight.getPrice(bookingDate);
    }

    /**
     * Gets the customer who made this booking.
     * @return The customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer for this booking.
     * @param customer The customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets the flight for this booking.
     * @return The flight
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * Sets the flight for this booking.
     * @param flight The flight to set
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    /**
     * Gets the date this booking was made.
     * @return The booking date
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * Sets the booking date.
     * @param bookingDate The date to set
     */
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Checks if this booking has been cancelled.
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation status of this booking.
     * @param cancelled true to mark as cancelled, false otherwise
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the price paid for this booking.
     * @return The price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price for this booking.
     * @param price The price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the cancellation fee for this booking.
     * @return The cancellation fee
     */
    public double getCancellationFee() {
        return cancellationFee;
    }

    /**
     * Sets the cancellation fee for this booking.
     * @param cancellationFee The fee to set
     */
    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    /**
     * Gets a detailed description of this booking.
     * @return A string with booking details
     */
    public String getDetailsLong() {
        String status = cancelled ? " [CANCELLED]" : "";
        String feeInfo = cancellationFee > 0 ? String.format(" (Cancellation Fee: £%.2f)", cancellationFee) : "";
        return String.format("Booking for %s on %s - Price: £%.2f%s%s",
                flight.getDetailsShort(),
                bookingDate,
                price,
                feeInfo,
                status);
    }
}
