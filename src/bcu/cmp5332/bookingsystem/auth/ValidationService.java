package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

/**
 * Validation service for uniqueness checks.
 * OOP: ENCAPSULATION
 */
public class ValidationService {
    
    /**
     * Ensures that the email is unique across admins and customers.
     * @param system system instance
     * @param email email to check
     * @throws FlightBookingSystemException if email is already used
     */
    public static void validateEmailUnique(FlightBookingSystem system, String email) 
            throws FlightBookingSystemException {
        
        String emailLower = email.toLowerCase();
        
        for (Admin admin : system.getAdmins()) {
            if (!admin.isDeleted() && admin.getEmail().toLowerCase().equals(emailLower)) {
                throw new FlightBookingSystemException("Email already registered");
            }
        }
        
        for (Customer customer : system.getCustomers()) {
            if (!customer.isDeleted() && customer.getEmail().toLowerCase().equals(emailLower)) {
                throw new FlightBookingSystemException("Email already registered");
            }
        }
    }
    
    /**
     * Ensures that the phone is unique across admins and customers.
     * @param system system instance
     * @param phone phone to check
     * @throws FlightBookingSystemException if phone is already used
     */
    public static void validatePhoneUnique(FlightBookingSystem system, String phone) 
            throws FlightBookingSystemException {
        
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        
        for (Admin admin : system.getAdmins()) {
            if (!admin.isDeleted()) {
                String adminPhone = admin.getPhone().replaceAll("[^0-9]", "");
                if (adminPhone.equals(cleanPhone)) {
                    throw new FlightBookingSystemException("Phone already registered");
                }
            }
        }
        
        for (Customer customer : system.getCustomers()) {
            if (!customer.isDeleted()) {
                String custPhone = customer.getPhone().replaceAll("[^0-9]", "");
                if (custPhone.equals(cleanPhone)) {
                    throw new FlightBookingSystemException("Phone already registered");
                }
            }
        }
    }

    /**
     * Ensures that the customer name is unique (case-insensitive).
     * @param system system instance
     * @param name customer name
     * @throws FlightBookingSystemException if name is already used
     */
    public static void validateCustomerNameUnique(FlightBookingSystem system, String name)
            throws FlightBookingSystemException {
        String normalized = name == null ? "" : name.trim().toLowerCase();
        if (normalized.isEmpty()) {
            throw new FlightBookingSystemException("Customer name cannot be empty");
        }
        for (Customer customer : system.getCustomers()) {
            if (!customer.isDeleted() && customer.getName().trim().toLowerCase().equals(normalized)) {
                throw new FlightBookingSystemException("Customer name already exists");
            }
        }
    }
}
