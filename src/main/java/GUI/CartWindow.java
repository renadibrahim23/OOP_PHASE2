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

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class CartWindow extends Application {

    private final CartService cartService;
    private final ObservableList<Product> cartProductList; // Observable list for cart products
    private TableView<Product> cartTable;
    private int userId; // Default user ID, can be made dynamic
    private Label totalLabel;
    private double total = 0.0;
    private Customer customer;

    public CartWindow(Customer customer) {// Pass the logged-in customer to the CartWindow
        this.customer = customer;
        this.cartService = new CartService();
        userId = customer.getCustomerId(); // Initialize the cart service
        this.cartProductList = FXCollections.observableArrayList(); // Observable list for cart items
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the database data and refresh cart data
            Database.initializeProducts();
            //refreshCartData();

            // Create main layout
            BorderPane mainLayout = new BorderPane();
            mainLayout.setStyle("-fx-background-color: white;");

            // Create sections
            VBox cartSection = createCartSection();
            VBox actionSection = createActionSection();

            // Layout setup
            mainLayout.setCenter(cartSection);
            mainLayout.setBottom(actionSection);

            Scene scene = new Scene(mainLayout, 800, 600);
            primaryStage.setTitle("Shopping Cart");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            showError("Initialization Error", e.getMessage());
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
        checkoutButton.setOnAction(e -> handleCheckout());

        section.getChildren().addAll(addButton, removeButton, checkoutButton);
        return section;
    }

    /**
     * Refresh the cart data in the UI.
     */
    private void refreshCartData() {
        cartProductList.setAll(cartService.viewProducts(userId));
        updateTotal();
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

        // Create product dropdown and quantity fields
        ComboBox<String> productDropdown = new ComboBox<>();
        for (Product product : Database.products) {
            productDropdown.getItems().add(product.getName());
        }
        TextField quantityField = new TextField();

        // Layout for the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(productDropdown, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    // Get the selected product name and quantity
                    String productName = productDropdown.getValue();
                    if (productName == null || productName.isEmpty()) {
                        throw new IllegalArgumentException("Please select a product.");
                    }
                    int quantity = Integer.parseInt(quantityField.getText().trim());
                    if (quantity <= 0) {
                        throw new IllegalArgumentException("Quantity must be greater than 0.");
                    }

                    // Add the product to the cart using CartService
                    cartService.addToCart(userId, productName, quantity);
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

    private void handleCheckout() {
        if (cartProductList.isEmpty()) {
            showError("Error", "Your cart is empty!");
            return;
        }

        // Show a dialog to get the payment method
        TextInputDialog paymentDialog = new TextInputDialog();
        paymentDialog.setTitle("Payment Method");
        paymentDialog.setHeaderText("Enter your payment method:");
        paymentDialog.setContentText("Payment Method:");

        Optional<String> result = paymentDialog.showAndWait();
        if (result.isPresent()) {
            try {
                // Place the order using CartService
                cartService.placeOrder(userId);
                refreshCartData(); // Clear cart after checkout
                showSuccess("Success", "Your order has been placed successfully!");
            } catch (Exception e) {
                showError("Error", e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}