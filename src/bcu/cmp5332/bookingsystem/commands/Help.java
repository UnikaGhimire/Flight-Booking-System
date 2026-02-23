// Help.java
package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * CLI command to print the help message.
 */
public class Help implements Command {

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        System.out.println(Command.HELP_MESSAGE);
    }
}
