package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.EditBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window to update an existing booking to a new flight.
 */
public class UpdateBookingWindow extends JFrame implements ActionListener {
    private MainWindow mw;
    private AdminDashboard adminDashboard;
    private JTextField customerNameText = new JTextField();
    private JTextField oldFlightNumberText = new JTextField();
    private JTextField newFlightNumberText = new JTextField();
    private JButton updateBtn = new JButton("Update");
    private JButton cancelBtn = new JButton("Cancel");

    public UpdateBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    public UpdateBookingWindow(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Update Booking");
        setSize(350, 200);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 2));
        topPanel.add(new JLabel("Customer Name : "));
        topPanel.add(customerNameText);
        topPanel.add(new JLabel("Current Flight No : "));
        topPanel.add(oldFlightNumberText);
        topPanel.add(new JLabel("New Flight No : "));
        topPanel.add(newFlightNumberText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(updateBtn);
        bottomPanel.add(cancelBtn);

        updateBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(adminDashboard != null ? adminDashboard : mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateBtn) {
            updateBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
            this.dispose();
        }
    }

    private void updateBooking() {
        try {
            String customerName = customerNameText.getText().trim();
            String oldFlightNumber = oldFlightNumberText.getText().trim();
            String newFlightNumber = newFlightNumberText.getText().trim();
            if (customerName.isEmpty() || oldFlightNumber.isEmpty() || newFlightNumber.isEmpty()) {
                throw new FlightBookingSystemException("Customer name, current flight, and new flight are required.");
            }
            
            Command editBooking = new EditBooking(customerName, oldFlightNumber, newFlightNumber);
            editBooking.execute(adminDashboard != null
                    ? adminDashboard.getFlightBookingSystem()
                    : mw.getFlightBookingSystem());
            
            this.setVisible(false);
            this.dispose();
            if (adminDashboard != null) {
                adminDashboard.displayBookings();
            }
            if (mw != null) {
                mw.displayCustomers();
            }
            
            JOptionPane.showMessageDialog(this, 
                    "Booking updated successfully! A rebooking fee has been applied.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                    
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
