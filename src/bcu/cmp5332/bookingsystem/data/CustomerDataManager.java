package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.io.*;
import java.util.Scanner;

/**
 * Persists customer data to and from text storage.
 */
public class CustomerDataManager implements DataManager {
    
    private final String RESOURCE = "./resources/data/customers.txt";
    
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        File file = new File(RESOURCE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return;
        }
        
        try (Scanner sc = new Scanner(file)) {
            int nextInternalId = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(SEPARATOR, -1);
                int index = 0;
                try {
                    Integer.parseInt(parts[0]);
                    if (parts.length >= 6) {
                        index = 1;
                    }
                } catch (NumberFormatException ex) {
                    index = 0;
                }

                String username = parts[index];
                String password = parts.length > index + 1 ? parts[index + 1] : "password";
                String email = parts.length > index + 2 ? parts[index + 2] : "";
                String phone = parts.length > index + 3 ? parts[index + 3] : "";
                boolean deleted = parts.length > index + 4 && Boolean.parseBoolean(parts[index + 4]);
                
                Customer customer = new Customer(nextInternalId++, username, password, email, phone);
                customer.setDeleted(deleted);
                fbs.addCustomer(customer);
            }
        }
    }
    
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Customer customer : fbs.getCustomers()) {
                out.print(customer.getUsername() + SEPARATOR);
                out.print(customer.getPassword() + SEPARATOR);
                out.print(customer.getEmail() + SEPARATOR);
                out.print(customer.getPhone() + SEPARATOR);
                out.print(customer.isDeleted() + SEPARATOR);
                out.println();
            }
        }
    }
}
