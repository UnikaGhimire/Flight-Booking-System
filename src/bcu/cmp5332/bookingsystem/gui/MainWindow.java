package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

/**
 * Legacy admin GUI window (pre-login flow) with basic management actions.
 */
public class MainWindow extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JMenu adminMenu;
    private JMenu flightsMenu;
    private JMenu bookingsMenu;
    private JMenu customersMenu;

    private JMenuItem adminExit;

    private JMenuItem flightsView;
    private JMenuItem flightsAdd;
    private JMenuItem flightsDel;
    
    private JMenuItem bookingsIssue;
    private JMenuItem bookingsUpdate;
    private JMenuItem bookingsCancel;

    private JMenuItem custView;
    private JMenuItem custAdd;
    private JMenuItem custDel;

    private FlightBookingSystem fbs;

    public MainWindow(FlightBookingSystem fbs) {
        initialize();
        this.fbs = fbs;
    }
    
    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    private void initialize() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Use default look and feel
        }

        setTitle("Flight Booking Management System");

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        adminMenu = new JMenu("Admin");
        menuBar.add(adminMenu);

        adminExit = new JMenuItem("Exit");
        adminMenu.add(adminExit);
        adminExit.addActionListener(this);

        flightsMenu = new JMenu("Flights");
        menuBar.add(flightsMenu);

        flightsView = new JMenuItem("View");
        flightsAdd = new JMenuItem("Add");
        flightsDel = new JMenuItem("Delete");
        flightsMenu.add(flightsView);
        flightsMenu.add(flightsAdd);
        flightsMenu.add(flightsDel);
        for (int i = 0; i < flightsMenu.getItemCount(); i++) {
            flightsMenu.getItem(i).addActionListener(this);
        }
        
        bookingsMenu = new JMenu("Bookings");
        menuBar.add(bookingsMenu);
        
        bookingsIssue = new JMenuItem("Issue");
        bookingsUpdate = new JMenuItem("Update");
        bookingsCancel = new JMenuItem("Cancel");
        bookingsMenu.add(bookingsIssue);
        bookingsMenu.add(bookingsUpdate);
        bookingsMenu.add(bookingsCancel);
        for (int i = 0; i < bookingsMenu.getItemCount(); i++) {
            bookingsMenu.getItem(i).addActionListener(this);
        }

        customersMenu = new JMenu("Customers");
        menuBar.add(customersMenu);

        custView = new JMenuItem("View");
        custAdd = new JMenuItem("Add");
        custDel = new JMenuItem("Delete");

        customersMenu.add(custView);
        customersMenu.add(custAdd);
        customersMenu.add(custDel);
        custView.addActionListener(this);
        custAdd.addActionListener(this);
        custDel.addActionListener(this);

        setSize(800, 500);

        setVisible(true);
        setAutoRequestFocus(true);
        toFront();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == adminExit) {
            try {
                FlightBookingSystemData.store(fbs);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
            
        } else if (ae.getSource() == flightsView) {
            displayFlights();
            
        } else if (ae.getSource() == flightsAdd) {
            new AddFlightWindow(this);
            
        } else if (ae.getSource() == flightsDel) {
            new DeleteFlightWindow(this);
            
        } else if (ae.getSource() == bookingsIssue) {
            new IssueBookingWindow(this);
            
        } else if (ae.getSource() == bookingsUpdate) {
            new UpdateBookingWindow(this);
            
        } else if (ae.getSource() == bookingsCancel) {
            new CancelBookingWindow(this);
            
        } else if (ae.getSource() == custView) {
            displayCustomers();
            
        } else if (ae.getSource() == custAdd) {
            new AddCustomerWindow(this);
            
        } else if (ae.getSource() == custDel) {
            new DeleteCustomerWindow(this);
        }
    }

    /**
     * Displays the list of flights in a table.
     */
    public void displayFlights() {
        List<Flight> flightsList = fbs.getFutureFlights();
        String[] columns = new String[]{"Flight No", "Origin", "Destination", "Departure Date",
                                        "Capacity", "Available", "Price"};

        Object[][] data = new Object[flightsList.size()][7];
        for (int i = 0; i < flightsList.size(); i++) {
            Flight flight = flightsList.get(i);
            data[i][0] = flight.getFlightNumber();
            data[i][1] = flight.getOrigin();
            data[i][2] = flight.getDestination();
            data[i][3] = flight.getDepartureDate();
            data[i][4] = flight.getCapacity();
            data[i][5] = flight.getAvailableSeats();
            data[i][6] = String.format("GBP %.2f", flight.getPrice(fbs.getSystemDate()));
        }

        JTable table = new JTable(data, columns);
        this.getContentPane().removeAll();
        this.getContentPane().add(new JScrollPane(table));
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Displays the list of customers in a table.
     */
    public void displayCustomers() {
        List<Customer> customersList = fbs.getActiveCustomers();
        String[] columns = new String[]{"Name", "Phone", "Email", "Bookings"};

        Object[][] data = new Object[customersList.size()][4];
        for (int i = 0; i < customersList.size(); i++) {
            Customer customer = customersList.get(i);
            data[i][0] = customer.getName();
            data[i][1] = customer.getPhone();
            data[i][2] = customer.getEmail();
            data[i][3] = customer.getBookings().size();
        }

        JTable table = new JTable(data, columns);
        this.getContentPane().removeAll();
        this.getContentPane().add(new JScrollPane(table));
        this.revalidate();
        this.repaint();
    }
}
