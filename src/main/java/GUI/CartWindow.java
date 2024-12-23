package GUI;

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
import java.util.Optional;

public class CartWindow extends Application {
    private final CartService cartService;
    private final ObservableList<Product> cartProductList;
    private TableView<Product> cartTable;
    private final int userId;
    private Label totalLabel;
    private double total = 0.0;

    public CartWindow() {
        this.cartService = new CartService();
        this.userId = 1; // Default user ID
        this.cartProductList = FXCollections.observableArrayList();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
           /* // Load initial cart data
            refreshCartData();*/

            // Create main layout
            BorderPane mainLayout = new BorderPane();
            mainLayout.setStyle("-fx-background-color: white;");

            // Create main components
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

    private TableView<Product> createCartTable() {
        TableView<Product> table = new TableView<>();

        // Name column
        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        // Price column
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? null : String.format("$%.2f", price));
            }
        });
        priceCol.setPrefWidth(100);

        // Quantity column
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, priceCol, quantityCol);
        table.setItems(cartProductList);

        return table;
    }

    private VBox createActionSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));

        HBox buttonBox = new HBox(10);

        Button addButton = createButton("Add Product", "#4CAF50");
        Button removeButton = createButton("Remove Product", "#f44336");
        Button checkoutButton = createButton("Checkout", "#2196F3");

        addButton.setOnAction(e -> showAddProductDialog());
        removeButton.setOnAction(e -> handleRemoveProduct());
        checkoutButton.setOnAction(e -> handleCheckout());

        buttonBox.getChildren().addAll(addButton, removeButton, checkoutButton);
        section.getChildren().add(buttonBox);

        return section;
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Enter product details");

        // Create fields
        TextField nameField = new TextField();
        TextField quantityField = new TextField();

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    int quantity = Integer.parseInt(quantityField.getText().trim());

                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Product name cannot be empty");
                    }
                    if (quantity <= 0) {
                        throw new IllegalArgumentException("Quantity must be greater than 0");
                    }

                    cartService.addToCart(userId, name, quantity);
                    refreshCartData();
                    showSuccess("Success", "Product added to cart");
                } catch (Exception e) {
                    showError("Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void handleRemoveProduct() {
        Product selectedProduct = cartTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showError("Error", "Please select a product to remove");
            return;
        }

        try {
            cartService.removeFromCart(userId, selectedProduct.getName(), selectedProduct.getQuantity());
            refreshCartData();
            showSuccess("Success", "Product removed from cart");
        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    private void handleCheckout() {
        if (cartProductList.isEmpty()) {
            showError("Error", "Cart is empty");
            return;
        }

        // Payment method dialog
        TextInputDialog paymentDialog = new TextInputDialog();
        paymentDialog.setTitle("Payment Method");
        paymentDialog.setHeaderText("Enter payment method");
        paymentDialog.setContentText("Payment method:");

        Optional<String> result = paymentDialog.showAndWait();
        if (result.isPresent()) {
            try {
                cartService.placeOrder(userId);
                refreshCartData();
                showSuccess("Success", "Order placed successfully");
            } catch (Exception e) {
                showError("Error", e.getMessage());
            }
        }
    }

    private void refreshCartData() {
        cartProductList.setAll(cartService.viewProducts(userId));
        updateTotal();
    }

    private void updateTotal() {
        total = cartProductList.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 20;" +
                        "-fx-font-size: 14px;"
        );
        return button;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}