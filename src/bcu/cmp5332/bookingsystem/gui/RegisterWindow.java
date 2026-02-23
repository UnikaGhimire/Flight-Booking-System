package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.*;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * GUI window to register a new customer.
 */
public class RegisterWindow extends JFrame implements ActionListener {
    
    private FlightBookingSystem fbs;
    private LoginWindow loginWindow;
    private JTextField usernameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerBtn, cancelBtn;
    
    public RegisterWindow(FlightBookingSystem fbs, LoginWindow loginWindow) {
        this.fbs = fbs;
        this.loginWindow = loginWindow;
        initialize();
    }
    
    private void initialize() {
        setTitle("Register New Customer");
        setSize(400, 350);
        setLocationRelativeTo(loginWindow);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Customer Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Confirm Password:"), gbc);
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        row++;
        
        registerBtn = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = row;
        registerBtn.addActionListener(this);
        panel.add(registerBtn, gbc);
        
        cancelBtn = new JButton("Cancel");
        gbc.gridx = 1;
        cancelBtn.addActionListener(this);
        panel.add(cancelBtn, gbc);
        
        add(panel);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerBtn) {
            handleRegister();
        } else if (e.getSource() == cancelBtn) {
            this.dispose();
        }
    }
    
    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            AuthenticationService authService = new AuthenticationService(fbs);
            Customer customer;
            try {
                customer = authService.registerCustomer(username, password, email, phone);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            FlightBookingSystemData.store(fbs);
            
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
