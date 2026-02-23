package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.auth.*;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI login window with navigation to registration and dashboards.
 */
public class LoginWindow extends JFrame implements ActionListener {
    
    private FlightBookingSystem fbs;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JButton registerBtn;
    
    public LoginWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }
    
    private void initialize() {
        setTitle("Flight Booking System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Flight Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        loginBtn = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginBtn.addActionListener(this);
        panel.add(loginBtn, gbc);
        
        registerBtn = new JButton("Register");
        gbc.gridx = 1;
        registerBtn.addActionListener(this);
        panel.add(registerBtn, gbc);
        
        add(panel);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            handleLogin();
        } else if (e.getSource() == registerBtn) {
            new RegisterWindow(fbs, this);
        }
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            AuthenticationService authService = new AuthenticationService(fbs);
            User user = authService.authenticate(username, password);
            Session.getInstance().login(user);
            
            this.dispose();
            
            if (user.getRole().equals("ADMIN")) {
                new AdminDashboard(fbs);
            } else {
                new CustomerDashboard(fbs);
            }
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
