
package Service;
import Entity.*;
import DAO.*;
import Network.ClientHandler;
import Service.ProductService;
import Service.CategoryService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class AdminService extends UserService {

    private final AdminDAO adminDAO= new AdminDAO();
    private final ProductService productService= new ProductService();
    private final CategoryService categoryService=new CategoryService();
    private final CustomerDAO customerDAO=new CustomerDAO();
    private final OrderDAO orderDAO=new OrderDAO();
    private final OrderService orderService=new OrderService();



    public boolean logIn(String username, String password)  {

        if(username==null || password==null){
            System.out.println("Please make sure to fill out all fields");
            return false;

        }


        Admin admin=adminDAO.getAdminByUsername(username);

        if(admin==null){
            System.out.println("Account not found, please make sure the username is correct or try to sign up if you're new here.");
            return false;
        }

        if(!((admin.getPassword()).equals(password))){
            System.out.println("Wrong password entered.");
            return false;
        }

        System.out.println("Login successful!");
        return true;

    }



    @Override
    public void signUp(String username, String password, Date dateOfBirth) {
        if(username==null || password==null || dateOfBirth==null){
            System.out.println("Please make sure to fill out all fields");
            return;
        }

        if(!(UserService.isValidUsername(username)))return;


        if(!(UserService.isValidPassword(password)))return;



        Admin admin= adminDAO.getAdminByUsername(username);

        if(admin!=null){
            System.out.println("Username is already taken, try another one, please");
        }





        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter role:");
        String role = scanner.nextLine();

        System.out.println("Enter working hours:");
        int workingHours;
        try {
            workingHours = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input for working hours.");
            return;
        }

        adminDAO.createNewAdmin(username,password,dateOfBirth,role,workingHours);
        System.out.println("Signup successful!");

    }
    public void CreateProduct(String name, double price, Category category, int quantity){
        productService.createProduct(name,price,category,quantity);
    }
    public void CreateCategory(String name, List<Product> products){
        categoryService.createCategory(name,products);
    }

    public void showAllProducts(){
        productService.getAllProducts();
    }

    public void showAllCustomers(){
        System.out.println(customerDAO.getAllCustomers());
    }

    public void showAllCategories(){
        categoryService.getAllCategories();
    }

    public void showAllOrders(){
        System.out.println(orderDAO.getAllOrders());
    }

    public void createAdmin(String username, String password, Date dateOfBirth,String role,double workingHours){
        if(isValidUsername(username) && isValidPassword(password) && role!=null && dateOfBirth!=null && workingHours>0){
            adminDAO.createNewAdmin(username,password,dateOfBirth,role,  workingHours);
        }
        else{
            System.out.println("Info invalid couldn't create the admin");
        }

    }

    public void deleteAdmin(int id){
        Admin admin=adminDAO.getById(id);
        if(admin!=null){
            adminDAO.delete(id);
        }
        else{
            System.out.println("Admin doesn't exist");
        }
    }

    public void getAdmin(int id){
        adminDAO.getById(id);
    }

    public Admin getAdminByUsername(String username){
        return adminDAO.getAdminByUsername(username);
    }

    public void updateAdmin(Admin admin){
        adminDAO.update(admin);
    }
    public void updateProduct(Product updatedProduct) {
        // Call ProductService's updateProduct method
        productService.updateProduct(updatedProduct);
    }

    // Method to delete a product (called from AdminService)
    public void deleteProduct(int id) {
        // Call ProductService's deleteProduct method
        productService.deleteProduct(id);
    }
    public void checkForRestock() {
        // Call ProductService's checkForRestock method
        productService.checkForRestock();
    }
    public void getAllCategories() {
        categoryService.getAllCategories();
    }

    public void updateCategory(Category updatedCategory) {
        categoryService.updateCategory(updatedCategory);
    }

    public void deleteCategory(int id) {
        categoryService.deleteCategory(id);
    }

    public String getAdminUsername(Admin admin){
        return adminDAO.getAdminUsername(admin);
    }

    public String getAdminPassword(Admin admin){
        return adminDAO.getAdminPassword(admin);
    }

    public double getAdminWorkingHours(Admin admin){
        return adminDAO.getAdminWorkingHours(admin);
    }

    public String getAdminRole(Admin admin){
        return adminDAO.getAdminRole(admin);
    }

    public void createNewAdmin(String username, String password, Date dateOfBirth, String role, double workingHours) throws IllegalArgumentException {
        // Validate Username
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (username.length() < 5) {
            throw new IllegalArgumentException("Username must be at least 5 characters long.");
        }

        // Validate Password
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must include at least one uppercase letter, one lowercase letter, and one number.");
        }

        // Validate Date of Birth
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of Birth cannot be null.");
        }
        Date today = new Date();
        if (dateOfBirth.after(today)) {
            throw new IllegalArgumentException("Date of Birth cannot be in the future.");
        }

        // Validate Role
        /*
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty.");
        }
        if (!role.equalsIgnoreCase("Admin")) {
            throw new IllegalArgumentException("Role must be 'Admin' only.");
        }*/

        // Validate Working Hours
        if (workingHours < 0 || workingHours > 24) {
            throw new IllegalArgumentException("Working hours must be between 0 and 24.");
        }

        // If validation passes, create the Admin
        adminDAO.createNewAdmin(username, password, dateOfBirth, role, workingHours);
    }

    public int getNumberOfCustomers(){
        return customerDAO.getNumberOfCustomers();
    }

    public int getNumberOfOrders(){
        return orderDAO.getNumberOfOrders();
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.getAllCustomers();
    }

    public String showCustomerInfo(Customer customer){
        return customerDAO.showCustomerInfo(customer);
    }

    public void deleteCustomer(Customer customer){
        customerDAO.delete(customerDAO.getCustomerId(customer));
    }

    public List<Order> showOrderInfo(Customer customer) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerDAO.getCustomerId(customer));
        if (orders == null) {
            orders = new ArrayList<>(); // Return an empty list if no orders found
        }
        return orders;
    }






    //network
    public static void sendMessageToCustomer(String customerUsername, String message) {
        ClientHandler customerHandler = ClientHandler.getClientHandlerByUsername(customerUsername);

        if (customerHandler != null) {
            try {
                customerHandler.getBufferedWriter().write("Message from Admin: " + message);
                customerHandler.getBufferedWriter().newLine();
                customerHandler.getBufferedWriter().flush();
            } catch (IOException e) {
                System.out.println("Failed to send message to " + customerUsername);
                e.printStackTrace();
            }
        } else {
            System.out.println("Customer " + customerUsername + " is not online.");
        }
    }


}