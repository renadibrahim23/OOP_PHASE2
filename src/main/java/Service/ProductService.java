package Service;
import DAO.ProductDAO;
import Entity.Category;
import Entity.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    ProductDAO productDAO=new ProductDAO();
    public void addProduct(Product product){
        if(productDAO.isValidProduct(product) ) {
            if (productDAO.getById(product.getId()) == null) {
                productDAO.add(product);
                System.out.println("Product is added successfully");
            } else {
                System.out.println("This product already exists ");
            }
        }else{
            System.out.println("Product details are invalid");
        }
    }
    public void createProduct(String name, double price, Category category, int quantity){
        Product product=productDAO.createProduct(name,price,category,quantity);
        if(productDAO.isValidProduct(product)){
            productDAO.add(product);
        }
        else{
            System.out.println("Product details are invalid ");
            Product.setIdCounter(Product.getIdCounter()-1);
        }

    }
    public void updateProduct(Product updatedProduct){
        if(productDAO.isValidProduct(updatedProduct)) {
            if (productDAO.getById(updatedProduct.getId()) != null) {
                productDAO.update(updatedProduct);
                System.out.println("Product is updated successfully");
            } else {
                System.out.println("Product does not exist");
            }
        }else {
            System.out.println("Updated Product details are invalid ");
        }
    }
    public void deleteProduct(int id ){
        if(productDAO.getAllProducts().isEmpty()){
            System.out.println("No Products available to delete");
        }
        if(productDAO.getById(id)!=null){
            productDAO.delete(id);
            System.out.println("Product is deleted successfully");
        }else{
            System.out.println("The product you are trying to delete does not already exist");
        }

    }
    //need to figure out how we can link it to notify the admin or whatever
    public void checkForRestock(){
        if(productDAO.checkForRestock().isEmpty()){
            System.out.println("All Products are sufficiently stocked");
        }else{
            System.out.println("The following products needs restocking");
            productDAO.getProductsInfo(productDAO.checkForRestock());
        }
    }
    public void getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("There are no products available.");
        } else {
            System.out.println("The products available are:");
            productDAO.getProductsInfo(products); // Print each product
        }

    }
    public void searchProducts(String keyword) {
        if(keyword==null){
            System.out.println("Please enter the category you want");
            return;
        }
        if(productDAO.getAllProducts().isEmpty()){
            System.out.println("No Products available right now");
            return;
        }
        List<Product> searchResults = new ArrayList<>();
        for (Product product : productDAO.getAllProducts()) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(product);
            }
        }
        if(searchResults.isEmpty()){
            System.out.println("No products match your search");
        }else {
            productDAO.getProductsInfo(searchResults);
        }
    }
    public void filterByCategory(Category category) {
        if (category == null) {
            System.out.println("Category cannot be null");
            return;
        }

        if (productDAO.getAllProducts().isEmpty()) {
            System.out.println("No products available in the database.");
            return;
        }
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : productDAO.getAllProducts()) {
            if (product.getCategory().equals(category)) {
                filteredProducts.add(product);
            }
        }
        if(filteredProducts.isEmpty()){
            System.out.println("No Products available for this category");
        }else{
            productDAO.getProductsInfo(filteredProducts);
        }

    }

    public int getProductId(Product product){
        return productDAO.getProductId(product);

    }




}