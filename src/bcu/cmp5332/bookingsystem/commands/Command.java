package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public interface Command {

    public static final String HELP_MESSAGE = "Commands:\n"
        + "\tlistflights                               print all flights\n"
        + "\tlistcustomers                             print all customers\n"
        + "\taddflight                                 add a new flight\n"
        + "\taddcustomer                               add a new customer\n"
        + "\tshowflight [flight-no]                    show flight details\n"
        + "\tshowcustomer [customer-name]              show customer details\n"
        + "\taddbooking [customer-name] [flight-no]    add a new booking\n"
        + "\tcancelbooking [customer-name] [flight-no] cancel a booking\n"
        + "\teditbooking [customer-name] [old flight-no] [new flight-no]    update a booking\n"
        + "\tdeleteflight [flight-no]                  delete a flight\n"
        + "\tdeletecustomer [customer-name]            delete a customer\n"
        + "\tloadgui                                   loads the GUI version of the app\n"
        + "\thelp                                      prints this help message\n"
        + "\tlogout                                    logs out of the current session\n"
        + "\texit                                      exits the program";

    
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;
    
}
