
package Service;
import Entity.Admin;
import Entity.Category;
import Entity.Product;
import DAO.*;
import Network.ClientHandler;
import Service.ProductService;
import Service.CategoryService;


import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class AdminService extends UserService {

    private final AdminDAO adminDAO= new AdminDAO();
    private final ProductService productService= new ProductService();
    private final CategoryService categoryService=new CategoryService();
    private final CustomerDAO customerDAO=new CustomerDAO();
    private final OrderDAO orderDAO=new OrderDAO();



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

    public void createNewAdmin(String username, String password, Date dateOfBirth, String role, int workingHours){
       adminDAO.createNewAdmin(username,password,dateOfBirth,role,workingHours);
    }

    //netowork
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