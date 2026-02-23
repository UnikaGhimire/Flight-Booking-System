package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

/**
 * Booking window that shows dynamic pricing and seat availability.
 */
public class BookFlightWindow extends JFrame implements ActionListener {

    private CustomerDashboard dashboard;
    private Customer customer;
    private JTextField flightNumberField;
    private JTextArea priceInfoArea;
    private JButton bookBtn, cancelBtn, calculateBtn;

    public BookFlightWindow(CustomerDashboard dashboard, Customer customer) {
        this.dashboard = dashboard;
        this.customer = customer;
        initialize();
    }

    private void initialize() {
        setTitle("Book Flight");
        setSize(500, 400);
        setLocationRelativeTo(dashboard);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        topPanel.add(new JLabel("Flight No:"));
        flightNumberField = new JTextField();
        topPanel.add(flightNumberField);

        topPanel.add(new JLabel("System Date:"));
        topPanel.add(new JLabel(dashboard.getFlightBookingSystem().getSystemDate().toString()));

        panel.add(topPanel, BorderLayout.NORTH);

        priceInfoArea = new JTextArea(10, 40);
        priceInfoArea.setEditable(false);
        priceInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        priceInfoArea.setBorder(BorderFactory.createTitledBorder("Price Breakdown"));
        panel.add(new JScrollPane(priceInfoArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        calculateBtn = new JButton("Calculate Price");
        bookBtn = new JButton("Book Flight");
        cancelBtn = new JButton("Cancel");

        calculateBtn.addActionListener(this);
        bookBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        bottomPanel.add(calculateBtn);
        bottomPanel.add(bookBtn);
        bottomPanel.add(cancelBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateBtn) {
            calculatePrice();
        } else if (e.getSource() == bookBtn) {
            bookFlight();
        } else if (e.getSource() == cancelBtn) {
            this.dispose();
        }
    }

    private void calculatePrice() {
        try {
            String flightNumber = flightNumberField.getText().trim();
            Flight flight = dashboard.getFlightBookingSystem().getFlightByNumber(flightNumber);

            LocalDate bookingDate = dashboard.getFlightBookingSystem().getSystemDate();
            double basePrice = flight.getBasePrice();
            double currentPrice = flight.getPrice(bookingDate);
            double finalPrice = currentPrice;

            double priceRatio = currentPrice / basePrice;
            String priceIndicator;
            if (priceRatio < 1.2) priceIndicator = "[LOW PRICE - GREEN]";
            else if (priceRatio < 1.5) priceIndicator = "[MEDIUM PRICE - YELLOW]";
            else if (priceRatio < 2.0) priceIndicator = "[HIGH PRICE - ORANGE]";
            else priceIndicator = "[VERY HIGH PRICE - RED]";

            StringBuilder sb = new StringBuilder();
            sb.append("=== PRICE CALCULATION ===\n\n");
            sb.append(String.format("Flight: %s\n", flight.getDetailsShort()));
            sb.append(String.format("Available Seats: %d / %d\n\n", flight.getAvailableSeats(), flight.getCapacity()));
            sb.append(String.format("Base Price:        £%.2f\n", basePrice));
            sb.append(String.format("Current Price:     £%.2f  (%.1fx)\n", currentPrice, priceRatio));
            sb.append("------------------------------\n");
            sb.append(String.format("FINAL PRICE:       £%.2f\n\n", finalPrice));
            sb.append(priceIndicator).append("\n");
            if (flight.getAvailableSeats() <= 0) {
                sb.append("STATUS: FULL - cannot book");
            } else {
                sb.append("STATUS: OPEN");
            }

            priceInfoArea.setText(sb.toString());

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bookFlight() {
        try {
            String flightNumber = flightNumberField.getText().trim();
            Flight flight = dashboard.getFlightBookingSystem().getFlightByNumber(flightNumber);
            if (flight.getAvailableSeats() <= 0) {
                JOptionPane.showMessageDialog(this, "This flight is full. You cannot book it.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new AddBooking(customer.getName(), flightNumber).execute(dashboard.getFlightBookingSystem());

            JOptionPane.showMessageDialog(this, "Flight booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refresh();
            this.dispose();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
