package bcu.cmp5332.bookingsystem.tests;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FlightCustomerTests {

    @Test
    void flightShouldStoreCapacityAndBasePrice() {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-20"), 150, 120.0);
        assertEquals(150, flight.getCapacity());
        assertEquals(120.0, flight.getBasePrice(), 0.0001);
    }

    @Test
    void dynamicPriceShouldIncreaseWhenCloseToDeparture() {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-20"), 100, 100.0);
        double price30Days = flight.getPrice(LocalDate.parse("2026-01-21"));
        double price5Days = flight.getPrice(LocalDate.parse("2026-02-15"));
        assertTrue(price5Days > price30Days, "Price should increase closer to departure");
    }

    @Test
    void customerShouldStoreEmail() {
        Customer customer = new Customer(1, "john", "pass", "john@example.com", "12345");
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test
    void availableSeatsShouldDecreaseWhenPassengerAdded() throws FlightBookingSystemException {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-20"), 2, 80.0);
        Customer customer = new Customer(1, "john", "pass", "john@example.com", "12345");
        assertEquals(2, flight.getAvailableSeats());
        flight.addPassenger(customer);
        assertEquals(1, flight.getAvailableSeats());
    }

    @Test
    void addingSamePassengerTwiceShouldFail() throws FlightBookingSystemException {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-20"), 2, 80.0);
        Customer customer = new Customer(1, "john", "pass", "john@example.com", "12345");
        flight.addPassenger(customer);
        assertThrows(FlightBookingSystemException.class, () -> flight.addPassenger(customer));
    }

    @Test
    void cannotExceedFlightCapacity() throws FlightBookingSystemException {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-20"), 1, 80.0);
        Customer c1 = new Customer(1, "john", "pass", "john@example.com", "12345");
        Customer c2 = new Customer(2, "amy", "pass", "amy@example.com", "67890");
        flight.addPassenger(c1);
        assertThrows(FlightBookingSystemException.class, () -> flight.addPassenger(c2));
    }

    @Test
    void bookingPriceShouldUseDynamicPriceAtBookingDate() {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-20"), 100, 100.0);
        Customer customer = new Customer(1, "john", "pass", "john@example.com", "12345");
        LocalDate bookingDate = LocalDate.parse("2026-02-15");
        Booking booking = new Booking(customer, flight, bookingDate);
        assertEquals(flight.getPrice(bookingDate), booking.getPrice(), 0.0001);
    }

    @Test
    void cancelledBookingShouldBeMarkedCancelledAndFeeApplied() throws FlightBookingSystemException {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-20"), 100, 100.0);
        Customer customer = new Customer(1, "john", "pass", "john@example.com", "12345");
        Booking booking = new Booking(customer, flight, LocalDate.parse("2026-02-10"));
        customer.addBooking(booking);

        customer.cancelBookingForFlight(flight);

        assertTrue(booking.isCancelled());
        assertEquals(booking.getPrice() * 0.10, booking.getCancellationFee(), 0.0001);
    }

    @Test
    void flightShouldBeConsideredDepartedIfBeforeSystemDate() {
        Flight flight = new Flight(1, "BA123", "LON", "PAR", LocalDate.parse("2026-02-01"), 100, 100.0);
        assertTrue(flight.hasDeparted(LocalDate.parse("2026-02-10")));
        assertFalse(flight.hasDeparted(LocalDate.parse("2026-01-31")));
    }


    @Test
    void detailsShortShouldIncludeKeyFields() {
        Flight flight = new Flight(5, "BA555", "NYC", "LAX", LocalDate.parse("2026-03-01"), 100, 150.0);
        String details = flight.getDetailsShort();
        assertTrue(details.contains("5"));
        assertTrue(details.contains("BA555"));
        assertTrue(details.contains("NYC"));
        assertTrue(details.contains("LAX"));
    }
}
