package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Window for adding a new flight.
 */
public class AddFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private AdminDashboard adminDashboard;
    private FlightBookingSystem fbs;
    private JTextField flightNoText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField depDateText = new JTextField();
    private JTextField capacityText = new JTextField();
    private JTextField priceText = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }

    public AddFlightWindow(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        this.fbs = adminDashboard.getFlightBookingSystem();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Use default
        }

        setTitle("Add a New Flight");

        setSize(400, 300);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(8, 2));
        topPanel.add(new JLabel("Flight No : "));
        topPanel.add(flightNoText);
        topPanel.add(new JLabel("Origin : "));
        topPanel.add(originText);
        topPanel.add(new JLabel("Destination : "));
        topPanel.add(destinationText);
        topPanel.add(new JLabel("Departure Date (YYYY-MM-DD) : "));
        topPanel.add(depDateText);
        topPanel.add(new JLabel("System Date : "));
        topPanel.add(new JLabel(fbs.getSystemDate().toString()));
        topPanel.add(new JLabel("Capacity : "));
        topPanel.add(capacityText);
        topPanel.add(new JLabel("Price (£) : "));
        topPanel.add(priceText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(adminDashboard != null ? adminDashboard : mw);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
            this.dispose();
        }

    }

    private void addFlight() {
        try {
            String flightNumber = flightNoText.getText();
            String origin = originText.getText();
            String destination = destinationText.getText();
            
            if (flightNumber.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
                throw new FlightBookingSystemException("All fields must be filled.");
            }
            
            LocalDate departureDate = null;
            try {
                departureDate = LocalDate.parse(depDateText.getText());
            } catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
            }
            
            int capacity = 100; // Default
            try {
                if (!capacityText.getText().isEmpty()) {
                    capacity = Integer.parseInt(capacityText.getText());
                    if (capacity <= 0) {
                        throw new FlightBookingSystemException("Capacity must be positive.");
                    }
                }
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Capacity must be a valid number.");
            }
            
            double price = 50.0; // Default
            try {
                if (!priceText.getText().isEmpty()) {
                    price = Double.parseDouble(priceText.getText());
                    if (price <= 0) {
                        throw new FlightBookingSystemException("Price must be positive.");
                    }
                }
            } catch (NumberFormatException nfe) {
                throw new FlightBookingSystemException("Price must be a valid number.");
            }
            
            // create and execute the AddFlight Command
            Command addFlight = new AddFlight(flightNumber, origin, destination, departureDate, capacity, price);
            addFlight.execute(fbs);
            // refresh the view with the list of flights
            if (adminDashboard != null) {
                adminDashboard.displayFlights();
            }
            if (mw != null) {
                mw.displayFlights();
            }
            // hide (close) the AddFlightWindow
            this.setVisible(false);
            this.dispose();
            
            JOptionPane.showMessageDialog(this, "Flight added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
