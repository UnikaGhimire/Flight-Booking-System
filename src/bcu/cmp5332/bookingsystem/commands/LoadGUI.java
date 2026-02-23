package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.gui.LoginWindow;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

/**
 * Command to load the GUI version of the application.
 */
/**
 * CLI command to launch the GUI (login window).
 */
public class LoadGUI implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        new LoginWindow(flightBookingSystem);
    }
    
}
