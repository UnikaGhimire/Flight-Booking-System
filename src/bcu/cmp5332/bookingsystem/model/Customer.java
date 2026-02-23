package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer class.
 * OOP: INHERITANCE, POLYMORPHISM, COMPOSITION, ENCAPSULATION
 */
public class Customer extends User {

    private final List<Booking> bookings = new ArrayList<>();

    public Customer(int id, String name, String phone) {
        super(id, name, "cust" + id, name + "@example.com", phone);
    }

    public Customer(int id, String name, String phone, String email) {
        super(id, name, "cust" + id, email, phone);
    }

    public Customer(int id, String username, String password, String email, String phone) {
        super(id, username, password, email, phone);
    }

    public String getName() { return getUsername(); }
    public void setName(String name) { setUsername(name); }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    @Override
    public String getRole() { return "CUSTOMER"; }

    @Override
    public String getDetails() {
        return String.format("Customer - %s\nEmail: %s\nPhone: %s\nBookings: %d",
                getName(), getEmail(), getPhone(), bookings.size());
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void cancelBookingForFlight(Flight flight) throws FlightBookingSystemException {
        for (Booking booking : bookings) {
            if (booking.getFlight().getFlightNumber().equalsIgnoreCase(flight.getFlightNumber()) && !booking.isCancelled()) {
                booking.setCancelled(true);
                double fee = booking.getPrice() * 0.10;
                booking.setCancellationFee(fee);
                return;
            }
        }
        throw new FlightBookingSystemException("No active booking found");
    }

    public String getDetailsShort() {
        return String.format("Customer %s - %s", getName(), getPhone());
    }

    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDetails());
        if (!bookings.isEmpty()) {
            sb.append("\n\n=== BOOKINGS ===\n");
            for (int i = 0; i < bookings.size(); i++) {
                sb.append((i + 1) + ". " + bookings.get(i).getDetailsLong() + "\n");
            }
        }
        return sb.toString();
    }
}
