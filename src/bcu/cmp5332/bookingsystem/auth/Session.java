package bcu.cmp5332.bookingsystem.auth;

import bcu.cmp5332.bookingsystem.model.User;

/**
 * Singleton session management.
 * OOP: ENCAPSULATION (Singleton pattern)
 */
public class Session {
    
    private static Session instance;
    private User currentUser;
    private boolean loggedIn;
    
    private Session() {
        this.loggedIn = false;
        this.currentUser = null;
    }
    
    /**
     * Returns the singleton session instance.
     * @return session
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    /**
     * Logs in the given user.
     * @param user authenticated user
     */
    public void login(User user) {
        this.currentUser = user;
        this.loggedIn = true;
    }
    
    /**
     * Clears the current session.
     */
    public void logout() {
        this.currentUser = null;
        this.loggedIn = false;
    }
    
    /**
     * @return true if a user is logged in
     */
    public boolean isLoggedIn() { return loggedIn; }
    /**
     * @return current user or null
     */
    public User getCurrentUser() { return currentUser; }
    
    /**
     * @return true if current user is an admin
     */
    public boolean isAdmin() {
        return loggedIn && currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
    
    /**
     * @return true if current user is a customer
     */
    public boolean isCustomer() {
        return loggedIn && currentUser != null && "CUSTOMER".equals(currentUser.getRole());
    }
}
