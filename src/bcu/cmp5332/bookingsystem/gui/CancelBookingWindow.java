package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window to cancel an existing booking.
 */
public class CancelBookingWindow extends JFrame implements ActionListener {
    private MainWindow mainWindow;
    private AdminDashboard adminDashboard;
    private CustomerDashboard customerDashboard;
    private FlightBookingSystem fbs;
    private JTextField customerNameText = new JTextField();
    private JTextField flightNumberText = new JTextField();
    private JButton cancelBookingBtn = new JButton("Cancel Booking");
    private JButton closeBtn = new JButton("Close");

    public CancelBookingWindow(MainWindow mw) {
        this.mainWindow = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }

    public CancelBookingWindow(CustomerDashboard customerDashboard) {
        this.customerDashboard = customerDashboard;
        this.fbs = customerDashboard.getFlightBookingSystem();
        initialize();
        customerNameText.setText(customerDashboard.getCustomer().getName());
        customerNameText.setEditable(false);
    }

    public CancelBookingWindow(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        this.fbs = adminDashboard.getFlightBookingSystem();
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Cancel Booking");
        setSize(350, 180);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2));
        topPanel.add(new JLabel("Customer Name : "));
        topPanel.add(customerNameText);
        topPanel.add(new JLabel("Flight No : "));
        topPanel.add(flightNumberText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(cancelBookingBtn);
        bottomPanel.add(closeBtn);

        cancelBookingBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(customerDashboard != null ? customerDashboard
                : (adminDashboard != null ? adminDashboard : mainWindow));
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == cancelBookingBtn) {
            cancelBooking();
        } else if (ae.getSource() == closeBtn) {
            this.setVisible(false);
            this.dispose();
        }
    }

    private void cancelBooking() {
        try {
            String customerName = customerNameText.getText().trim();
            String flightNumber = flightNumberText.getText().trim();
            if (customerName.isEmpty() || flightNumber.isEmpty()) {
                throw new FlightBookingSystemException("Customer name and flight number are required.");
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to cancel this booking? A cancellation fee will be applied.",
                    "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                    
            if (confirm == JOptionPane.YES_OPTION) {
                Command cancelBooking = new CancelBooking(customerName, flightNumber);
                cancelBooking.execute(fbs);

                this.setVisible(false);
                this.dispose();
                if (customerDashboard != null) {
                    customerDashboard.refresh();
                }
                if (adminDashboard != null) {
                    adminDashboard.displayBookings();
                }

                JOptionPane.showMessageDialog(this, 
                        "Booking cancelled successfully! A cancellation fee has been applied.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
