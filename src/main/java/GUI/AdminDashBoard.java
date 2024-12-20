package GUI;


import Entity.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import Service.*;

import java.util.Date;

public class AdminDashBoard extends Application {

    private AdminService adminService = new AdminService();
    private ProductService productService= new ProductService();

    @Override
    public void start(Stage primaryStage) {
        // Left Pane: Admin Menu
        VBox leftPane = new VBox(15);
        leftPane.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;");

        Label adminMenuLabel = new Label("Admin Dashboard");
        adminMenuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Button customersBtn = new Button("Manage Customers");
        Button viewReportsBtn = new Button("View Reports");
        Button productsBtn = new Button("Manage Products");
        Button settingsBtn = new Button("Settings");

        customersBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");
        viewReportsBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");
        productsBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");
        settingsBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");

        leftPane.getChildren().addAll(adminMenuLabel, customersBtn, viewReportsBtn, productsBtn, settingsBtn);

        // Right Pane
        StackPane rightPane = new StackPane();
        rightPane.setStyle("-fx-background-color: #8ceaff;");

        Label cartIcon = new Label("🛒");
        cartIcon.setStyle("-fx-font-size: 100px; -fx-text-fill: #ff4081;");
        rightPane.getChildren().add(cartIcon);

        // Root Pane
        BorderPane root = new BorderPane();
        root.setLeft(leftPane);
        root.setCenter(rightPane);

        // Scene for Admin Dashboard
        Scene adminDashboardScene = new Scene(root, 900, 600);

        // Set Button Actions to Navigate to Other Pages
        customersBtn.setOnAction(e -> {
            Scene manageUsersScene = createManageUsersScene(primaryStage, adminDashboardScene);
            primaryStage.setScene(manageUsersScene);
        });

        viewReportsBtn.setOnAction(e -> {
            Scene reportsScene = createReportsScene(primaryStage, adminDashboardScene);
            primaryStage.setScene(reportsScene);
        });

        productsBtn.setOnAction(e -> {
            Scene productsScene = createProductsScene(primaryStage, adminDashboardScene);
            primaryStage.setScene(productsScene);
        });

        settingsBtn.setOnAction(e -> {
            Scene settingsScene = createSettingsScene(primaryStage, adminDashboardScene);
            primaryStage.setScene(settingsScene);
        });

        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(adminDashboardScene);
        primaryStage.show();
    }

    // Create "Manage Users" Scene
    private Scene createManageUsersScene(Stage stage, Scene previousScene) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");
        Label label = new Label("Manage Customers");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        ObservableList<Customer> customerList = FXCollections.observableArrayList(adminService.getAllCustomers());

        TableView<Customer> tableView = new TableView<>();
        tableView.setItems(customerList);

        TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Customer, Date> dateCol = new TableColumn<>("DateOfBirth");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, Customer.Gender> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Customer, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button viewButton = new Button("View");
            private final Button removeButton = new Button("Remove");

            {
                viewButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
                removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                viewButton.setOnAction(e -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    //adminService.showCustomerInfo(customer);
                    //adminService.showOrderInfo(customer);
                    Scene ordersScene = createCustomerOrdersScene(stage, previousScene, customer);
                    stage.setScene(ordersScene);

                });
                removeButton.setOnAction(e -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    adminService.deleteCustomer(customer);
                    customerList.remove(customer);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, viewButton, removeButton);
                    setGraphic(buttons);
                }
            }
        });

        tableView.getColumns().addAll(usernameCol, dateCol, addressCol, genderCol, actionColumn);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.setScene(previousScene));

        layout.getChildren().addAll(label, tableView, backButton);

        return new Scene(layout, 900, 600);
    }

    // Create "View Reports" Scene
    private Scene createReportsScene(Stage stage, Scene previousScene) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");
        Label label = new Label("View Reports");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.setScene(previousScene));
        layout.getChildren().addAll(label, backButton);

        return new Scene(layout, 900, 600);
    }

    // Create "Settings" Scene
    private Scene createSettingsScene(Stage stage, Scene previousScene) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");
        Label label = new Label("Settings");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.setScene(previousScene));
        layout.getChildren().addAll(label, backButton);

        return new Scene(layout, 900, 600);
    }

    private Scene createProductsScene(Stage stage, Scene previousScene) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

        Label label = new Label("Manage Products");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TableView<Product> productTable = new TableView<>();

        // Product ID column
        TableColumn<Product, String> productIdCol = new TableColumn<>("Product ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));

        // Product Name column
        TableColumn<Product, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Price column
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Stock column
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productTable.getColumns().addAll(productIdCol, productNameCol, priceCol, stockCol);

        // Add button
        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> {
            // Open the form to add a product
            openAddProductForm(stage);  // You need to implement this method
        });

        // Update button
        Button updateButton = new Button("Update Product");
        updateButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Open the form to update the selected product
                openUpdateProductForm(stage, selectedProduct);  // Implement this method
            }
        });

        // Delete button
        Button deleteButton = new Button("Delete Product");
        deleteButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Delete the selected product
                adminService.deleteProduct(productService.getProductId(selectedProduct));  // Ensure this method works as expected
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.setScene(previousScene));

        // Layout setup
        layout.getChildren().addAll(label, productTable, addButton, updateButton, deleteButton, backButton);

        return new Scene(layout, 900, 600);
    }


    private Scene createCustomerOrdersScene(Stage stage, Scene previousScene, Customer customer) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");
        Label label = new Label("Orders for Customer: " + customer.getUsername());
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Fetch orders for the customer
        ObservableList<Order> orderList = FXCollections.observableArrayList(adminService.showOrderInfo(customer));

        // If orderList is still empty, display a message
        if (orderList.isEmpty()) {
            Label noOrdersLabel = new Label("No orders found for this customer.");
            layout.getChildren().add(noOrdersLabel);
        } else {
            TableView<Order> tableView = new TableView<>();
            tableView.setItems(orderList);

            // Define the columns for displaying order details
            TableColumn<Order, String> orderIdCol = new TableColumn<>("Order ID");
            orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));


            TableColumn<Order, Double> totalAmountCol = new TableColumn<>("Total Amount");
            totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("checkOutTotal"));

            tableView.getColumns().addAll(orderIdCol, totalAmountCol);

            layout.getChildren().add(tableView);
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.setScene(previousScene));

        layout.getChildren().add(backButton);

        return new Scene(layout, 900, 600);
    }





    public static void main(String[] args) {
        launch(args);
    }
}
