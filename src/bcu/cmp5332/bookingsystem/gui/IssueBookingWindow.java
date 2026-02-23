package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window to issue a new booking for a customer.
 */
public class IssueBookingWindow extends JFrame implements ActionListener {
    private MainWindow mw;
    private AdminDashboard adminDashboard;
    private JTextField customerNameText = new JTextField();
    private JTextField flightNumberText = new JTextField();
    private JButton issueBtn = new JButton("Issue Booking");
    private JButton cancelBtn = new JButton("Cancel");

    public IssueBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    public IssueBookingWindow(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Issue Booking");
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
        bottomPanel.add(issueBtn);
        bottomPanel.add(cancelBtn);

        issueBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(adminDashboard != null ? adminDashboard : mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == issueBtn) {
            issueBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
            this.dispose();
        }
    }

    private void issueBooking() {
        try {
            String customerName = customerNameText.getText().trim();
            String flightNumber = flightNumberText.getText().trim();
            if (customerName.isEmpty() || flightNumber.isEmpty()) {
                throw new FlightBookingSystemException("Customer name and flight number are required.");
            }
            
            Command addBooking = new AddBooking(customerName, flightNumber);
            addBooking.execute(adminDashboard != null
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
            
            JOptionPane.showMessageDialog(this, "Booking issued successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                    
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
