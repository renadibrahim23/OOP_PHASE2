package Service;

import DAO.CartDAO;
import DAO.OrderDAO;
import DAO.CustomerDAO;
import Entity.Cart;
import Entity.Product;
import Entity.Customer;
import Entity.Order;
import Database.Database;

import java.util.List;
import java.util.Scanner;

public class CartService {
    private CartDAO cartDAO = new CartDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private OrderDAO orderDAO = new OrderDAO();

    public CartService(){}

    //constructor
    public CartService(CartDAO cartDAO, CustomerDAO customerDAO) {
        this.cartDAO = cartDAO;
        this.customerDAO = customerDAO;
    }

    //changed
    public void addToCart(int userId, String productName, int quantity) {
        //get the cart of this user
        Cart userCart = cartDAO.getById(cartDAO.getDefaultCartId(userId));
        if (userCart == null) {
            throw new IllegalStateException("No cart found for user with ID " + userId);
        }

        //check if the product exists in the supermarket database
        Product databaseProduct = null;
        for (Product product : Database.products) {
            if (product.getName().equals(productName)) {
                databaseProduct = product;
                break;
            }
        }

        if (databaseProduct == null) {
            throw new IllegalArgumentException("Product " + productName + " not found in the supermarket.");
        }

        // add or update the product in the cart
        List<Product> products = userCart.getAddedProducts();
        boolean inCart = false;

        for (Product product : products) {
            if (product.getName().equals(productName)) {
                product.setQuantity(product.getQuantity() + quantity);
                inCart = true;
                break;
            }
        }

        if (!inCart) {
            Product newProduct = new Product();
            newProduct.setName(productName);
            newProduct.setPrice(databaseProduct.getPrice());
            newProduct.setQuantity(quantity);
            products.add(newProduct);
        }

        // update the cart's total price
        double updatedTotalPrice = 0.0;
        for (Product product : products) {
            updatedTotalPrice += product.getPrice() * product.getQuantity();
        }
        userCart.setTotalPrice(updatedTotalPrice);

        // update the cart in the database
        cartDAO.update(userCart);
    }


    public void removeFromCart(int userId, String productName, int quantity) {
        //get the cart of this user
        Cart userCart = cartDAO.getById(cartDAO.getDefaultCartId(userId));
        if (userCart == null) {
            throw new IllegalStateException("No cart found for user with ID " + userId);
        }

        // remove or update the product in the cart
        List<Product> products = userCart.getAddedProducts();
        Product productToRemove = null;

        for (Product product : products) {
            if (product.getName().equals(productName)) {
                if (product.getQuantity() > quantity) {
                    product.setQuantity(product.getQuantity() - quantity);
                } else {
                    productToRemove = product;
                }
                break;
            }
        }

        if (productToRemove != null) {
            products.remove(productToRemove);
        }

        // update the cart's total price
        double updatedTotalPrice = 0.0;
        for (Product product : products) {
            updatedTotalPrice += product.getPrice() * product.getQuantity();
        }
        userCart.setTotalPrice(updatedTotalPrice);

        //update the cart in the database
        cartDAO.update(userCart);
    }

    public List<Product> viewProducts(int userId) {
        //get the cart of this user
        Cart userCart = cartDAO.getById(cartDAO.getDefaultCartId(userId));
        if (userCart == null) {
            throw new IllegalStateException("No cart found for user with ID " + userId);
        }

        return userCart.getAddedProducts();
    }


    public void placeOrder(int userId) {
        //get the cart for this user
        Cart userCart = cartDAO.getById(cartDAO.getDefaultCartId(userId));
        if (userCart == null) {
            throw new IllegalStateException("No cart found for user with ID " + userId);
        }

        List<Product> products = userCart.getAddedProducts();
        if (products.isEmpty()) {
            throw new IllegalStateException("Cannot place an order. The cart is empty.");
        }

        // Calculate order totals
        double subtotal = userCart.getTotalPrice();
        double discount = calculateDiscount(subtotal);
        double tax = calculateTax(subtotal - discount);
        double shippingFee = calculateShippingFee();
        double checkoutTotal = subtotal - discount + tax + shippingFee;
        System.out.println("Please Enter the payment method");
        Scanner scanner = new Scanner(System.in);
        String paymentMethod=scanner.nextLine();

        // Create order entity to pass it to the DAO

        orderDAO.createNewOrder(userId,discount,tax,shippingFee,checkoutTotal,paymentMethod);



        // Clear the cart
        products.clear();
        userCart.setTotalPrice(0.0);

        // Update the cart in the database
        cartDAO.update(userCart);

        // Print the order details
        System.out.println("Order placed successfully!");
        System.out.println("Checkout Total: " + checkoutTotal);
    }


    //helper functions for place order
    private double calculateDiscount(double subtotal) {
        return subtotal > 100 ? subtotal * 0.10 : 0.0; //if the subtotal larger than 100 the discount will be applied
    }

    private double calculateTax(double amount) {
        return amount * 0.08;
    }

    private double calculateShippingFee() {
        return 5.0;
    }



    //update only interests
    //After placing the order we should update the balance

    public void updateCustomerDetails(int userId) {
        // get the user of this cart
        Cart userCart = cartDAO.getById(cartDAO.getDefaultCartId(userId));
        if (userCart == null) {
            throw new IllegalStateException("No cart found for user with ID " + userId);
        }

        // Fetch the customer associated with the user
        Customer customer = null;
        for (Customer c : Database.customers) {
            if (c.getCustomerId() == userId) {
                customer = c;
                break;
            }
        }

        if (customer == null) {
            throw new IllegalStateException("No customer found with ID " + userId);
        }

        // Update customer interests based on cart contents
        List<Product> products = userCart.getAddedProducts();
        for (Product product : products) {
            if (product.getCategory() != null) { // Ensure product category exists
                customerDAO.addInterest(product.getCategory().getName(),customer); // Assuming `addInterest` is implemented
            }
        }

        customerDAO.update(customer);
        System.out.println("Customer interests updated successfully for Customer ID: " + customer.getCustomerId());
    }
}