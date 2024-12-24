package GUI;

import Entity.Order;
import Entity.Product;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class OrderWindow extends Application {
    private final ObservableList<Product> cartItems;
    private TableView<Product> cartTable;

    public OrderWindow() {
        this.cartItems = FXCollections.observableArrayList();
    }

    public OrderWindow(List<Product> cartItems) {
        this.cartItems = FXCollections.observableArrayList(cartItems);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create main layout
            BorderPane mainLayout = new BorderPane();
            mainLayout.setStyle("-fx-background-color: white;");

            // Create main components
            VBox cartSection = createCartSection();
            HBox actionSection = createActionSection();

            // Layout setup
            mainLayout.setCenter(cartSection);
            mainLayout.setBottom(actionSection);

            Scene scene = new Scene(mainLayout, 1000, 600);
            primaryStage.setTitle("Order Management");
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
        Label headerLabel = new Label("Cart Items");
        headerLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        // Cart table
        cartTable = createCartTable();

        section.getChildren().addAll(headerLabel, cartTable);
        return section;
    }

    private TableView<Product> createCartTable() {
        TableView<Product> table = new TableView<>();

        // Product Name column
        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        // Price column
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        // Quantity column
        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, priceCol, quantityCol);
        table.setItems(cartItems);

        return table;
    }

    private HBox createActionSection() {
        HBox section = new HBox(10);
        section.setPadding(new Insets(20));

        Button placeOrderButton = createButton("Place Order", "#4CAF50");
        Button cancelButton = createButton("Cancel Order", "#f44336");

        placeOrderButton.setOnAction(e -> handlePlaceOrder());
        cancelButton.setOnAction(e -> handleCancelOrder());

        section.getChildren().addAll(placeOrderButton, cancelButton);
        return section;
    }

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

    private void handlePlaceOrder() {
        // Assuming placeOrder method logic is here
        if (cartItems.isEmpty()) {
            showError("Error", "Your cart is empty!");
            return;
        }

        // Place the order logic here
        showSuccess("Success", "Order placed successfully!");
    }

    private void handleCancelOrder() {
        // Enhanced validation before canceling the order
        if (cartItems.isEmpty()) {
            showError("Error", "Your cart is already empty!");
            return;
        }

        // Confirm cancellation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Cancellation");
        confirmationAlert.setHeaderText("Are you sure you want to cancel the order?");
        confirmationAlert.setContentText("All items in your cart will be removed.");

        // Wait for user confirmation
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cartItems.clear();
            cartTable.refresh();
            showSuccess("Success", "Order canceled successfully!");
        }
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
