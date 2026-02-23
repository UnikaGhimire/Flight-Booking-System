package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.DeleteFlight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window to delete (soft-delete) a flight.
 */
public class DeleteFlightWindow extends JFrame implements ActionListener {
    private MainWindow mainWindow;
    private AdminDashboard adminDashboard;
    private FlightBookingSystem fbs;
    private JTextField flightNumberText = new JTextField();
    private JButton deleteBtn = new JButton("Delete");
    private JButton cancelBtn = new JButton("Cancel");

    public DeleteFlightWindow(AdminDashboard mw) {
        this.adminDashboard = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }


    public DeleteFlightWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.fbs = mainWindow.getFlightBookingSystem();
        initialize();
    }


    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Delete Flight");
        setSize(300, 150);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        topPanel.add(new JLabel("Flight No : "));
        topPanel.add(flightNumberText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(deleteBtn);
        bottomPanel.add(cancelBtn);

        deleteBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(adminDashboard != null ? adminDashboard : mainWindow);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteBtn) {
            deleteFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
            this.dispose();
        }
    }

    private void deleteFlight() {
        try {
            String flightNumber = flightNumberText.getText().trim();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete flight " + flightNumber + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
            if (confirm == JOptionPane.YES_OPTION) {
                Command deleteFlight = new DeleteFlight(flightNumber);
                deleteFlight.execute(fbs);
                if (adminDashboard != null) {
                    adminDashboard.displayFlights();
                }
                if (mainWindow != null) {
                    mainWindow.displayFlights();
                }
                this.setVisible(false);
                this.dispose();
                
                JOptionPane.showMessageDialog(this, "Flight deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
