package GUI;

import Database.Database;
import Entity.Cart;
import Entity.Category; // Assuming Category is an enum or class
import Entity.Customer;
import Entity.Product;
import Service.CartService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

import static javafx.application.Application.launch;

public class CartWindow extends Application {

    private Button checkoutButton;

    private final CartService cartService=new CartService();
    private final ObservableList<Product> cartProductList= FXCollections.observableArrayList(); // Observable list for cart products
    private TableView<Product> cartTable;
    private int userId; // Default user ID, can be made dynamic
    private Label totalLabel;
    private double total = 0.0;
    private Customer customer;

    public CartWindow(){}

    public CartWindow(Customer customer) {// Pass the logged-in customer to the CartWindow
        this.customer = customer;

        userId = customer.getCustomerId(); // Initialize the cart service
        // Observable list for cart items
    }

    public VBox createContent() {
        VBox root = new VBox();
        Label heading = new Label("Your Shopping Cart");
        Button button = new Button("Checkout");

        root.getChildren().addAll(heading, button);
        return root;
    }


    @Override
    public void start(Stage primaryStage) {
        Database.initializeProducts();
        Database.initializeCustomers();
        Scene scene = createCartScene(); // Reuse the new method
        if (scene != null) {
            primaryStage.setTitle("Shopping Cart"); // Set window title
            primaryStage.setScene(scene); // Set the scene to the stage
            primaryStage.show(); // Show the stage
        }
    }

    /**
     * Initialize the mock data for the database.
     //     */
//    private void refreshCartData() {
//        cartProductList.clear();
//        Cart cart = Database.getCartForUser(userId);
//        cart.getAddedProducts().forEach((product, quantity) -> {
//            product.setQuantity(quantity); // Update quantity to show in the cart
//            cartProductList.add(product);
//        });
//        updateTotal(); // Update total price
//    }

    /**
     * Create the cart section UI.
     */

    public Scene createCartScene() {
        try {
            // Initialize the database data and refresh cart data
            Database.initializeProducts();
            Database.initializeCustomers();


            // Create the main layout of the cart window
            BorderPane mainLayout = new BorderPane();
            mainLayout.setStyle("-fx-background-color: white;");

            // Create sections
            VBox cartSection = createCartSection();
            VBox actionSection = createActionSection();

            // Set layout components
            mainLayout.setCenter(cartSection);
            mainLayout.setBottom(actionSection);

            // Return a new scene with the main layout
            return new Scene(mainLayout, 800, 600);
        } catch (Exception e) {
            // Display error in case of issues
            showError("Initialization Error", e.getMessage());
            return null;
        }
    }

    private VBox createCartSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));

        // Header
        Label headerLabel = new Label("Shopping Cart");
        headerLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        // Cart table
        cartTable = createCartTable();

        // Total display
        totalLabel = new Label(String.format("Total: $%.2f", total));
        totalLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        section.getChildren().addAll(headerLabel, cartTable, totalLabel);
        return section;
    }

    /**
     * Create the TableView for displaying cart products.
     */
    private TableView<Product> createCartTable() {
        TableView<Product> table = new TableView<>();

        // Name column
        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        // Price column
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price ($)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        // Quantity column
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, priceCol, quantityCol);
        table.setItems(cartProductList);

        return table;
    }

    /**
     * Create the action section UI.
     */
    private VBox createActionSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));

        Button addButton = createButton("Add Product", "#4CAF50");
        Button removeButton = createButton("Remove Product", "#f44336");
        Button checkoutButton = createButton("Checkout", "#2196F3");

        // Button actions
        addButton.setOnAction(e -> showAddProductDialog());
        removeButton.setOnAction(e -> handleRemoveProduct());
        checkoutButton.setOnAction(event -> {
            // Pass primaryStage to handleCheckout method
            handleCheckout((Stage) checkoutButton.getScene().getWindow());
        });

        section.getChildren().addAll(addButton, removeButton, checkoutButton);
        return section;


    }

    /**
     * Refresh the cart data in the UI.
     */
    private void refreshCartData() {
        List<Product> products = cartService.viewProducts(userId); // Get products from service

        if (products == null || products.isEmpty()) {
            System.out.println("Cart is empty or products list is null.");
            cartProductList.clear(); // Clear the UI list if cart is empty
        } else {
            cartProductList.setAll(products); // Update the UI list with available products
        }

        updateTotal(); // Update the total price
    }

    /**
     * Update the total price display.
     */
    private void updateTotal() {
        total = cartProductList.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    /**
     * Create a styled button.
     */
    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px 20px;"
        );
        return button;
    }

    /**
     * Show error alert.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show success alert.
     */
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Select product and enter quantity:");

        // Create product TreeView for categories and products
        TreeView<String> productTree = new TreeView<>();
        TreeItem<String> rootItem = new TreeItem<>("Categories");
        rootItem.setExpanded(true);

        // Manually group products by Category type
        for (Product product : Database.products) {
            Category category = product.getCategory(); // Assume Product has getCategory() method
            TreeItem<String> categoryItem = null;

            // Check if the category already exists in the tree
            for (TreeItem<String> item : rootItem.getChildren()) {
                if (item.getValue().equals(category.getName())) { // Assume Category has getName() method
                    categoryItem = item;
                    break;
                }
            }

            // If category doesn't exist, create it
            if (categoryItem == null) {
                categoryItem = new TreeItem<>(category.getName());
                rootItem.getChildren().add(categoryItem);
            }

            // Add the product under its category
            categoryItem.getChildren().add(new TreeItem<>(product.getName() + " - $" + product.getPrice()));
        }
        productTree.setRoot(rootItem);
        productTree.setShowRoot(false); // Hide the root "Categories"

        // Adjust the size of the TreeView to make it smaller
        productTree.setPrefSize(250, 150); // Width: 250px, Height: 150px

        TextField quantityField = new TextField();

        // Layout for the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(productTree, 1, 0); // Add the TreeView to the grid
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    // Get the selected product and quantity
                    TreeItem<String> selectedItem = productTree.getSelectionModel().getSelectedItem();
                    if (selectedItem == null || !selectedItem.isLeaf()) {
                        throw new IllegalArgumentException("Please select a product.");
                    }

                    // Extract product name from the selected item
                    String selectedProductName = selectedItem.getValue().split(" - ")[0];
                    int quantity = Integer.parseInt(quantityField.getText().trim());
                    if (quantity <= 0) {
                        throw new IllegalArgumentException("Quantity must be greater than 0.");
                    }

                    // Find the Product object using the product name
                    Product selectedProduct = Database.products.stream()
                            .filter(product -> product.getName().equals(selectedProductName))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Product not found."));

                    // Add the product to the cart using CartService
                    cartService.addToCart(userId, selectedProduct.getName(), quantity);
                    refreshCartData(); // Update UI with the new cart data
                    showSuccess("Success", "Product added to cart successfully!");

                } catch (NumberFormatException e) {
                    showError("Input Error", "Quantity must be a valid number!");
                } catch (Exception e) {
                    showError("Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


    private void handleRemoveProduct() {
        // Get the selected product from the table
        Product selectedProduct = cartTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showError("Error", "Please select a product to remove!");
            return;
        }

        try {
            // Remove the product from the cart using CartService
            cartService.removeFromCart(userId, selectedProduct.getName(), selectedProduct.getQuantity());
            refreshCartData(); // Update UI with the new cart data
            showSuccess("Success", "Product removed from cart successfully!");
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    private void handleCheckout(Stage stage) {
        // Check if the cart is empty
        if (cartProductList.isEmpty()) {
            showError("Error", "Your cart is empty! Please add products before checking out.");
            return;
        }


        // Pass the selected products to the OrderWindow constructor
        try {

            OrderWindow orderWindow = new OrderWindow(cartProductList); // Pass cart items
            orderWindow.start(stage);
        } catch (Exception e) {
            showError("Error", "Failed to navigate to Order Window: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}