package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.io.*;
import java.util.Scanner;

/**
 * Persists admin data to and from text storage.
 */
public class AdminDataManager implements DataManager {
    
    private final String RESOURCE = "./resources/data/admins.txt";
    
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        File file = new File(RESOURCE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            // Create default admin
            Admin admin = new Admin(1, "admin", "admin123", "admin@fbs.com", "+44-1234-567890");
            fbs.addAdmin(admin);
            return;
        }
        
        try (Scanner sc = new Scanner(file)) {
            int nextInternalId = 1;
            while (sc.hasNextLine()) {
                String line = sc.hasNextLine() ? sc.nextLine() : "";
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(SEPARATOR, -1);
                int index = 0;
                try {
                    Integer.parseInt(parts[0]);
                    if (parts.length >= 7) {
                        index = 1;
                    }
                } catch (NumberFormatException ex) {
                    index = 0;
                }
                String username = parts[index];
                String password = parts.length > index + 1 ? parts[index + 1] : "admin123";
                String email = parts.length > index + 2 ? parts[index + 2] : "";
                String phone = parts.length > index + 3 ? parts[index + 3] : "";
                String dept = parts.length > index + 4 ? parts[index + 4] : "Administration";
                boolean deleted = parts.length > index + 5 && Boolean.parseBoolean(parts[index + 5]);
                
                Admin admin = new Admin(nextInternalId++, username, password, email, phone, dept);
                admin.setDeleted(deleted);
                fbs.addAdmin(admin);
            }
        }
    }
    
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Admin admin : fbs.getAdmins()) {
                out.print(admin.getUsername() + SEPARATOR);
                out.print(admin.getPassword() + SEPARATOR);
                out.print(admin.getEmail() + SEPARATOR);
                out.print(admin.getPhone() + SEPARATOR);
                out.print(admin.getDepartment() + SEPARATOR);
                out.print(admin.isDeleted() + SEPARATOR);
                out.println();
            }
        }
    }
}
