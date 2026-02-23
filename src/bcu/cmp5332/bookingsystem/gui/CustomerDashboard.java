package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.Session;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Customer dashboard for viewing flights and managing bookings.
 */
public class CustomerDashboard extends JFrame implements ActionListener {

    private FlightBookingSystem fbs;
    private Customer customer;
    private JMenuBar menuBar;
    private JMenuItem viewFlightsItem, bookFlightItem;
    private JMenuItem myBookingsItem, cancelBookingItem;
    private JMenuItem logoutItem;

    public CustomerDashboard(FlightBookingSystem fbs) {
        this.fbs = fbs;
        try {
            this.customer = (Customer) Session.getInstance().getCurrentUser();
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(null, "Invalid session");
            System.exit(1);
        }
        initialize();
    }

    private void initialize() {
        setTitle("Customer Dashboard - " + customer.getName());
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuBar = new JMenuBar();

        JMenu flightsMenu = new JMenu("Flights");
        viewFlightsItem = new JMenuItem("View Available Flights");
        bookFlightItem = new JMenuItem("Book Flight");
        viewFlightsItem.addActionListener(this);
        bookFlightItem.addActionListener(this);
        flightsMenu.add(viewFlightsItem);
        flightsMenu.add(bookFlightItem);

        JMenu bookingsMenu = new JMenu("My Bookings");
        myBookingsItem = new JMenuItem("View My Bookings");
        cancelBookingItem = new JMenuItem("Cancel Booking");
        myBookingsItem.addActionListener(this);
        cancelBookingItem.addActionListener(this);
        bookingsMenu.add(myBookingsItem);
        bookingsMenu.add(cancelBookingItem);

        JMenu accountMenu = new JMenu("Account");
        logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(this);
        accountMenu.add(logoutItem);

        menuBar.add(flightsMenu);
        menuBar.add(bookingsMenu);
        menuBar.add(accountMenu);

        setJMenuBar(menuBar);

        displayWelcome();
        setVisible(true);
    }

    private void displayWelcome() {
        JPanel panel = new JPanel(new BorderLayout());

        String html = String.format("<html><center><h1>Welcome, %s!</h1>"
                + "<h3>Total Bookings: %d</h3>"
                + "<p>Use the menu above to manage your flights</p></center></html>",
                customer.getName(),
                customer.getBookings().size());

        JLabel label = new JLabel(html);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);

        setContentPane(panel);
        revalidate();
        repaint();
    }

    private void displayFlights() {
        java.util.List<Flight> flights = fbs.getFutureFlights();
        String[] columns = {"Flight No", "Origin", "Destination", "Date", "Available", "Status", "Price"};
        Object[][] data = new Object[flights.size()][7];

        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            double currentPrice = f.getPrice(fbs.getSystemDate());
            String status = f.getAvailableSeats() <= 0 ? "FULL" : "OPEN";

            data[i][0] = f.getFlightNumber();
            data[i][1] = f.getOrigin();
            data[i][2] = f.getDestination();
            data[i][3] = f.getDepartureDate();
            data[i][4] = f.getAvailableSeats();
            data[i][5] = status;
            data[i][6] = String.format("GBP %.2f", currentPrice);
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        setContentPane(scrollPane);
        revalidate();
        repaint();
    }

    private void displayMyBookings() {
        java.util.List<Booking> bookings = customer.getBookings();
        String[] columns = {"Flight", "Origin", "Destination", "Date", "Price", "Fee", "Status"};
        Object[][] data = new Object[bookings.size()][7];

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            Flight f = b.getFlight();
            data[i][0] = f.getFlightNumber();
            data[i][1] = f.getOrigin();
            data[i][2] = f.getDestination();
            data[i][3] = f.getDepartureDate();
            data[i][4] = String.format("GBP %.2f", b.getPrice());
            data[i][5] = b.getCancellationFee() > 0 ? String.format("GBP %.2f", b.getCancellationFee()) : "-";
            data[i][6] = b.isCancelled() ? "CANCELLED" : "ACTIVE";
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        setContentPane(scrollPane);
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewFlightsItem) {
            displayFlights();
        } else if (e.getSource() == bookFlightItem) {
            new BookFlightWindow(this, customer);
        } else if (e.getSource() == myBookingsItem) {
            displayMyBookings();
        } else if (e.getSource() == cancelBookingItem) {
            new CancelBookingWindow(this);
        } else if (e.getSource() == logoutItem) {
            try {
                FlightBookingSystemData.store(fbs);
                Session.getInstance().logout();
                this.dispose();
                new LoginWindow(fbs);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
            }
        }
    }

    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void refresh() {
        try {
            customer = fbs.getCustomerByName(customer.getName());
            displayWelcome();
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, "Error refreshing: " + ex.getMessage());
        }
    }
}
