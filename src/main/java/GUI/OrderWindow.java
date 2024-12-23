package GUI;

import Entity.Order;
import Service.OrderService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class OrderWindow extends Application {
    private final OrderService orderService;
    private final ObservableList<Order> ordersList;
    private TableView<Order> orderTable;
    private final int userId = 1; // Default user ID

    public OrderWindow() {
        this.orderService = new OrderService();
        this.ordersList = FXCollections.observableArrayList();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load initial orders data
            refreshOrdersData();

            // Create main layout
            BorderPane mainLayout = new BorderPane();
            mainLayout.setStyle("-fx-background-color: white;");

            // Create main components
            VBox ordersSection = createOrdersSection();
            VBox actionSection = createActionSection();

            // Layout setup
            mainLayout.setCenter(ordersSection);
            mainLayout.setBottom(actionSection);

            Scene scene = new Scene(mainLayout, 1000, 600);
            primaryStage.setTitle("Order Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            showError("Initialization Error", e.getMessage());
        }
    }

    private VBox createOrdersSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));

        // Header
        Label headerLabel = new Label("Order Management");
        headerLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        // Orders table
        orderTable = createOrderTable();

        section.getChildren().addAll(headerLabel, orderTable);
        return section;
    }

    private TableView<Order> createOrderTable() {
        TableView<Order> table = new TableView<>();

        // Order ID column
        TableColumn<Order, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        idCol.setPrefWidth(100);

        // User ID column
        TableColumn<Order, Integer> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userIdCol.setPrefWidth(100);

        // Payment Method column
        TableColumn<Order, String> paymentCol = new TableColumn<>("Payment Method");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        paymentCol.setPrefWidth(150);

        // Discount column
        TableColumn<Order, Double> discountCol = new TableColumn<>("Discount");
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        discountCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double discount, boolean empty) {
                super.updateItem(discount, empty);
                setText(empty ? null : String.format("$%.2f", discount));
            }
        });
        discountCol.setPrefWidth(100);

        // Tax column
        TableColumn<Order, Double> taxCol = new TableColumn<>("Tax");
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));
        taxCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double tax, boolean empty) {
                super.updateItem(tax, empty);
                setText(empty ? null : String.format("$%.2f", tax));
            }
        });
        taxCol.setPrefWidth(100);

        // Shipping Fee column
        TableColumn<Order, Double> shippingCol = new TableColumn<>("Shipping Fee");
        shippingCol.setCellValueFactory(new PropertyValueFactory<>("shippingFee"));
        shippingCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double fee, boolean empty) {
                super.updateItem(fee, empty);
                setText(empty ? null : String.format("$%.2f", fee));
            }
        });
        shippingCol.setPrefWidth(100);

        // Total column
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("checkOutTotal"));
        totalCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                setText(empty ? null : String.format("$%.2f", total));
            }
        });
        totalCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, userIdCol, paymentCol, discountCol, taxCol, shippingCol, totalCol);
        table.setItems(ordersList);

        return table;
    }

    private VBox createActionSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));

        HBox buttonBox = new HBox(10);

        Button viewDetailsButton = createButton("View Details", "#4CAF50");
        Button updateButton = createButton("Update Order", "#2196F3");
        Button cancelButton = createButton("Cancel Order", "#f44336");

        viewDetailsButton.setOnAction(e -> showOrderDetails());
        updateButton.setOnAction(e -> showUpdateOrderDialog());
        cancelButton.setOnAction(e -> handleCancelOrder());

        buttonBox.getChildren().addAll(viewDetailsButton, updateButton, cancelButton);
        section.getChildren().add(buttonBox);

        return section;
    }

    private void showOrderDetails() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showError("Error", "Please select an order to view");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Order Details");
        dialog.setHeaderText("Order #" + selectedOrder.getOrderId());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        content.getChildren().addAll(
                new Label("User ID: " + selectedOrder.getUserId()),
                new Label("Payment Method: " + selectedOrder.getPaymentMethod()),
                new Label("Discount: $" + String.format("%.2f", selectedOrder.getDiscount())),
                new Label("Tax: $" + String.format("%.2f", selectedOrder.getTax())),
                new Label("Shipping Fee: $" + String.format("%.2f", selectedOrder.getShippingFee())),
                new Label("Total: $" + String.format("%.2f", selectedOrder.getCheckOutTotal()))
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showUpdateOrderDialog() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showError("Error", "Please select an order to update");
            return;
        }

        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Update Order");
        dialog.setHeaderText("Update Order #" + selectedOrder.getOrderId());

        // Create fields
        TextField paymentMethodField = new TextField(selectedOrder.getPaymentMethod());

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Payment Method:"), 0, 0);
        grid.add(paymentMethodField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    selectedOrder.setPaymentMethod(paymentMethodField.getText().trim());
                    orderService.updateOrder(selectedOrder);
                    refreshOrdersData();
                    showSuccess("Success", "Order updated successfully");
                } catch (Exception e) {
                    showError("Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void handleCancelOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showError("Error", "Please select an order to cancel");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Order");
        alert.setHeaderText("Cancel Order #" + selectedOrder.getOrderId());
        alert.setContentText("Are you sure you want to cancel this order?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    orderService.cancelOrder(selectedOrder.getOrderId());
                    refreshOrdersData();
                    showSuccess("Success", "Order cancelled successfully");
                } catch (Exception e) {
                    showError("Error", e.getMessage());
                }
            }
        });
    }

    private void refreshOrdersData() {
        try {
            ordersList.setAll(orderService.getAllOrders());
        } catch (IllegalStateException e) {
            // Handle empty orders case
            ordersList.clear();
        }
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