package Database;

import Entity.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Service.*;

public class Database {
    public static List<Customer> customers = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Cart> carts = new ArrayList<>();
    public static List<Admin> admins = new ArrayList<>();
    private static CartService cartService = new CartService();
    private static OrderService orderService = new OrderService();

    private static AdminService adminService = new AdminService();
    private static CustomerService customerService= new CustomerService();
    static {
        Date date = new Date();
        adminService.createNewAdmin("admin123", "HelloSupermarket67.", date, "manager", 5.5);
        customerService.createNewCustomer("renadibrahim23", "Password123.", date, "1 zamalek 1", Customer.Gender.FEMALE);
        customerService.createNewCustomer("john-ki29", "oop123.", date, "24 salah salem", Customer.Gender.MALE);
        customerService.createNewCustomer("Naira05", "dsa2424.", date, "12 heliopolis ", Customer.Gender.FEMALE);
        customerService.createNewCustomer("esraa1000", "neural123.", date, "35 madinet nasr", Customer.Gender.FEMALE);

        cartService.addToCart(1, "Apple", 2);
        cartService.addToCart(2, "Orange", 3);
        cartService.addToCart(3, "Banana", 1);
        cartService.addToCart(4, "sugar", 4);

    }





/*
    public static void loadCustomersFromFile(String filePath) throws IOException{
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // Read each line of the file and create a Customer object
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String username = parts[0];
                    String password = parts[1];
                    Date dateOfBirth = sdf.parse(parts[2]);
                    String address = parts[3];

                    // Create Customer and add to the static list
                    Customer customer = new Customer(username, password, dateOfBirth, address);
                    customers.add(customer);
                }
            }
        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {

                System.out.println("File not found!");

            }
        }

    }
    public static void appendCustomerToFile(Customer customer) throws IOException {
        // Prepare the string to append to the file
        String customerData = customer.getUsername() + "," +
                customer.getPassword() + "," +
                customer.getDateOfBirth() + "," +
                customer.getAddress() + "\n";

        // Use BufferedWriter to append the data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(customerData);
        }
    }*/

}