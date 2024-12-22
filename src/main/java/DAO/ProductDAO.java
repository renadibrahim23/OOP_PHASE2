package DAO;
import Database.Database;
import Entity.Category;
import Entity.Product;
import Exceptions.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements GenericDAO<Product> {

    // getting a certain product by id
    public Product getById(int id) {
        for (Product product : Database.products) {
            if (product.getId() == id) {
                return product;
            }

        }
        return null;
    }

    //adding a product to our database
    public void add(Product product) {
        Database.products.add(product);
    }
    public Product createProduct(String name, double price, Category category, int quantity){
        Product product=new Product(name,price,category,quantity);
        return product;
    }

    public Product createExistingProduct(int id,String name, double price, Category category, int quantity){
        Product product=new Product(id,name,price,category,quantity);
        return product;
    }

    public void update(Product updatedProduct) {
        for (Product product : Database.products) {
            if (product.getId() == updatedProduct.getId()) {
                int index = Database.products.indexOf(product);
                Database.products.set(index, updatedProduct);
                return; // once product is updated exit to avoid looping for nothing
            }

        }

    }

    //deleting a certain product from the database
    public void delete(int productId) {
        // Find the product in the backend list
        Product productToDelete = getById(productId);
        if (productToDelete != null) {
            Database.products.remove(productToDelete); // Remove from backend list
        } else {
            // If the product does not exist, throw an exception
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }
    }

    public List<Product> checkForRestock() {
        List<Product> needsRestock = new ArrayList<>();
        for (Product product : Database.products) {
            if (product.getQuantity() == 0) {
                needsRestock.add(product);
            }
        }
        return needsRestock;
    }

    public void getProductsInfo(List<Product> products) {
        for (Product product : products) {
            System.out.println("Product ID: " + product.getId() +
                    ", Name: " + product.getName() +
                    ", Current Quantity: " + product.getQuantity() +
                    ", price:"+ product.getPrice()  +
                    ",  Category: "+ product.getCategory());
        }


    }

    public List<Product> getAllProducts() {
        return Database.products;
    }
    public boolean isValidProduct(Product product) {
        return product != null && product.getId() >= 0 &&
                product.getName() != null && product.getQuantity() >= 0 &&
                product.getPrice() >= 0;
    }

    public int getProductId(Product product){
        return product.getId();
    }

    public String getProductName(Product product){
        return product.getName();
    }

    public Category getProductCategory(Product product){
        return product.getCategory();
    }

    public double getProductPrice(Product product){
        return product.getPrice();
    }

    public int getProductQuantity(Product product){
        return product.getQuantity();
    }



}