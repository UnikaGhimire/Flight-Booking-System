package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.commands.DeleteCustomer;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window to delete (soft-delete) a customer.
 */
public class DeleteCustomerWindow extends JFrame implements ActionListener {
    private MainWindow mainWindow;
    private AdminDashboard adminDashboard;
    private FlightBookingSystem fbs;
    private JTextField customerNameText = new JTextField();
    private JButton deleteBtn = new JButton("Delete");
    private JButton cancelBtn = new JButton("Cancel");

    public DeleteCustomerWindow(MainWindow mw) {
        this.mainWindow = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }

    public DeleteCustomerWindow(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        this.fbs = adminDashboard.getFlightBookingSystem();
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        setTitle("Delete Customer");
        setSize(300, 150);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        topPanel.add(new JLabel("Customer Name : "));
        topPanel.add(customerNameText);

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
            deleteCustomer();
        } else if (ae.getSource() == cancelBtn) {
            this.setVisible(false);
            this.dispose();
        }
    }

    private void deleteCustomer() {
        try {
            String customerName = customerNameText.getText().trim();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete customer " + customerName + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
            if (confirm == JOptionPane.YES_OPTION) {
                Command deleteCustomer = new DeleteCustomer(customerName);
                deleteCustomer.execute(fbs);
                if (adminDashboard != null) {
                    adminDashboard.displayCustomers();
                }
                if (mainWindow != null) {
                    mainWindow.displayCustomers();
                }
                this.setVisible(false);
                this.dispose();
                
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
