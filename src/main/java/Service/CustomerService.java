package Service;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import DAO.*;
import Entity.Customer;
import Entity.Order;
import Network.ClientHandler;

public class CustomerService extends UserService{


    private final CustomerDAO customerDAO= new CustomerDAO();
    private OrderDAO orderDAO =new OrderDAO();
    private OrderService orderService= new OrderService();
    private CartService cartService= new CartService();
    private String lastMessage = "";
    private String signUpMessage;

    public boolean signUp(String username, String password, Date dateOfBirth, String address, String genderInput) {
        // Validate username
        if (!UserService.isValidUsername(username)) {
            signUpMessage = UserService.getMessage();
            return false;
        }

        // Validate password
        if (!UserService.isValidPassword(password)) {
            signUpMessage = UserService.getMessage();
            return false;
        }


        // Check if username is already taken
        if (customerDAO.getCustomerByUsername(username) != null) {
            signUpMessage = "Username already taken.";
            return false;
        }


        // Validate date of birth
        if (dateOfBirth == null || dateOfBirth.after(new Date())) {
            signUpMessage = "Invalid date of birth.";
            return false;
        }

        // Validate gender
        Customer.Gender gender;
        try {
            gender = Customer.Gender.valueOf(genderInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            signUpMessage = "Invalid input. Please enter one of the following: " + Customer.Gender.getValidValues();
            return false;
        }

        // Create new customer and save to database
        customerDAO.createNewCustomer(username, password, dateOfBirth, address, gender);
        signUpMessage = "Signup successful!";
        return true;
    }


    public String getSignUpMessage(){
        return signUpMessage;
    }


    public boolean logIn(String username,String password){
        Customer customer=customerDAO.getCustomerByUsername(username);

        if(customer==null){
            lastMessage = "Account not found. Please sign up first!";
            return false;
        }

        if(!((customer.getPassword()).equals(password))){
            lastMessage="Wrong password entered.";
            return false;
        }

        lastMessage="Login successful!";
        return true;
    }
    public String getWarningMessage(){
        return lastMessage;
    }




    public CustomerService(){}
    public CustomerService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void signUp(String username, String password, Date dateOfBirth){

        if(!(UserService.isValidUsername(username)))return;


        if(!(UserService.isValidPassword(password)))return;

        Customer customer= customerDAO.getCustomerByUsername(username);
        if(customer!=null){
            System.out.println("Username already taken");
            return;
        }



        Scanner scanner = new Scanner(System.in);

        System.out.println("What is your address? ");
        String address=scanner.nextLine();


        while (true) {
            System.out.print("Enter gender (" + Customer.Gender.getValidValues() + "): ");
            String input = scanner.nextLine().trim().toUpperCase();

            try {
                Customer.Gender gender = Customer.Gender.valueOf(input);
                customerDAO.createNewCustomer(username, password, dateOfBirth, address,gender);
                break; // Exit loop if input is valid
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter one of the following: " + Customer.Gender.getValidValues());
            }




        }

    }
    /*
    public boolean logIn(String username,String password){



        Customer customer=customerDAO.getCustomerByUsername(username);

        if(customer==null){
            System.out.println("Account not found, please make sure the username is correct or try to sign up if you're new here.");
            return false;
        }

        if(!((customer.getPassword()).equals(password))){
            System.out.println("Wrong password entered.");
            return false;
        }

        System.out.println("Login successful!");
        return true;
    }*/



    public void addToCart(Customer customer,String productName,int quantity){
        int id= customerDAO.getCustomerId(customer);
        cartService.addToCart(id,productName,quantity);

    }

    public void removeFromCart(Customer customer,String productName,int quantity){
        int id=customerDAO.getCustomerId(customer);
        cartService.removeFromCart(id,productName,quantity);
    }


    public void placeOrder(Customer customer, Order order) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        orderService.placeOrder(order);

    }

    //network
    public static void sendMessageToAdmin(String adminUsername, String message) {
        ClientHandler adminHandler = ClientHandler.getClientHandlerByUsername(adminUsername);

        if (adminHandler != null) {
            try {
                adminHandler.getBufferedWriter().write("Message from Customer: " + message);
                adminHandler.getBufferedWriter().newLine();
                adminHandler.getBufferedWriter().flush();
            } catch (IOException e) {
                System.out.println("Failed to send message to " + adminUsername);
                e.printStackTrace();
            }
        } else {
            System.out.println("Admin " + adminUsername + " is not online.");
        }
    }

    public Customer createNewCustomer(String username, String password, Date dateOfBirth, String address, Customer.Gender gender) {
        return customerDAO.createNewCustomer(username,password,dateOfBirth,address,gender);
    }

    public String showCustomerInfo(Customer customer){
        return customerDAO.showCustomerInfo(customer);
    }

}