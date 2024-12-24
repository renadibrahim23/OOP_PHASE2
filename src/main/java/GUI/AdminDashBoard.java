package GUI;


import Database.Database;
import Entity.*;
import Exceptions.ProductNotFoundException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;
import Service.*;
import DAO.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

public class AdminDashBoard extends Application {

    private AdminService adminService = new AdminService();
    private ProductService productService= new ProductService();
    private CategoryService categoryService=new CategoryService();
    private ObservableList<Product>productList;
    private TableView<Product> productTable;
    private ObservableList<Admin>adminsList=FXCollections.observableArrayList(adminService.getAllAdmins());
    private TableView<Admin>adminsTable;



    public AdminDashBoard() {}

    public Scene adminDashboardScene;

    public VBox createContent() {
        VBox root = new VBox();
        Label heading = new Label("Admin Dashboard");
        Button button = new Button("Admin Action");

        root.getChildren().addAll(heading, button);
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        Database.initializeCustomers();
        Database.initializeProducts();
        productList = FXCollections.observableArrayList(productService.getProducts());
        // Left Pane: Admin Menu
        VBox leftPane = new VBox(15);
        leftPane.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;");

        Label adminMenuLabel = new Label("Admin Dashboard");
        adminMenuLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Button customersBtn = new Button("Manage Customers");
        Button viewReportsBtn = new Button("View Reports");
        Button productsBtn = new Button("Manage Products");
        Button adminsBtn = new Button("Manage Admins");

        //Button settingsBtn = new Button("Settings");

        customersBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");
        viewReportsBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");
        productsBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");
        adminsBtn.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white; -fx-padding: 10px;");


        leftPane.getChildren().addAll(adminMenuLabel, customersBtn, viewReportsBtn, productsBtn,adminsBtn);

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
        adminDashboardScene = new Scene(root, 900, 600);

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

        adminsBtn.setOnAction(e -> {
            Scene adminsScene = createAdminsScene(primaryStage, adminDashboardScene);
            primaryStage.setScene(adminsScene);
        });

        /*
        settingsBtn.setOnAction(e -> {
            Scene settingsScene = createSettingsScene(primaryStage, adminDashboardScene);
            primaryStage.setScene(settingsScene);
        });*/

        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(adminDashboardScene);
        primaryStage.show();
    }

    // Create "Manage Users" Scene
    //complete
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

    private Scene createAdminsScene(Stage stage, Scene previousScene) {
        VBox layout = new VBox(15); // Root layout
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

        Label label = new Label("Manage Admins");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // TableView for displaying admins
        adminsTable = new TableView<>();
        TableColumn<Admin, String> adminIdCol = new TableColumn<>("Admin ID");
        adminIdCol.setCellValueFactory(new PropertyValueFactory<>("adminId"));

        TableColumn<Admin, String> adminUserNameCol = new TableColumn<>("Username");
        adminUserNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Admin, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Admin, Double> workingHoursCol = new TableColumn<>("Working Hours");
        workingHoursCol.setCellValueFactory(new PropertyValueFactory<>("workingHours"));

        adminsTable.getColumns().addAll(adminIdCol, adminUserNameCol, roleCol, workingHoursCol);
        adminsTable.setItems(adminsList); // Bind the admin list to the table

        // Buttons for actions
        Button addButton = new Button("Add Admin");
        addButton.setOnAction(e -> openAddAdminForm(stage));

        Button deleteButton = new Button("Delete Selected Admin");
        deleteButton.setOnAction(e -> {
            // Get the selected admin
            Admin selectedAdmin = adminsTable.getSelectionModel().getSelectedItem();
            if (selectedAdmin != null) {
                // Show confirmation dialog
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Admin");
                confirmationAlert.setHeaderText("Are you sure you want to delete this admin?");
                confirmationAlert.setContentText("Admin: " + selectedAdmin.getUsername());
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Remove admin from the list
                    adminsList.remove(selectedAdmin);
                }
            } else {
                // Show warning if no admin is selected
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("No Selection");
                warningAlert.setHeaderText("No admin selected");
                warningAlert.setContentText("Please select an admin to delete.");
                warningAlert.showAndWait();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.setScene(previousScene)); // Navigate back to the previous scene

        // Layout setup
        HBox buttonBox = new HBox(10, addButton, deleteButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
       // adminsTable.getColumns().addAll(adminIdCol, adminUserNameCol, roleCol, workingHoursCol);
        adminsTable.setItems(adminsList);

        layout.getChildren().addAll(label, adminsTable, buttonBox);

        return new Scene(layout, 800, 600); // Return the final scene
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
    /*
    private Scene createSettingsScene(Stage stage, Scene previousScene,Admin currentAdmin) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");
        Label label = new Label("Settings");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username: ");
        TextField usernameField = new TextField(adminService.getAdminUsername(currentAdmin));
        usernameField.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");

        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setText(adminService.getAdminPassword(currentAdmin));
        passwordField.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");

        Label roleLabel=new Label("Role: ");
        TextField roleField=new TextField(adminService.getAdminRole(currentAdmin));
        roleField.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");

        Label workingHoursLabel=new Label("Working Hours: ");
        TextField workingHoursField = new TextField(String.valueOf(currentAdmin.getWorkingHours()));
        workingHoursField.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        Button saveButton = new Button("Save Changes");
        saveButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 16px;");
        saveButton.setPrefWidth(150);


        saveButton.setOnAction(e -> {
                    String username = usernameField.getText().trim();
                    String password = passwordField.getText().trim();
                    String role = roleField.getText().trim();
                    String workingHoursText = workingHoursField.getText().trim();

        });


        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff4081; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.setScene(adminDashboardScene));
        layout.getChildren().addAll(label, backButton);

        return new Scene(layout, 900, 600);
    }

     */



    //complete
    private Scene createProductsScene(Stage stage, Scene previousScene) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

        Label label = new Label("Manage Products");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        productTable = new TableView<>();

        // Product ID column
        TableColumn<Product, String> productIdCol = new TableColumn<>("Product ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Product Name column
        TableColumn<Product, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> productCategoryCol = new TableColumn<>("Product Category");
        productCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Price column
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Stock column
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productTable.getColumns().addAll(productIdCol, productNameCol,productCategoryCol, priceCol, stockCol);
        productTable.setItems(productList);


        // Add button
        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> {
            // Open the form to add a product
           openAddProductForm(stage);
        });

        // Update button
        Button updateButton = new Button("Update Product");
        updateButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Open the form with the selected product's details
                openUpdateProductForm(stage, selectedProduct);
            } else {
                // Show warning if no product is selected
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("No Selection");
                warning.setContentText("Please select a product to update.");
                warning.showAndWait();
            }
        });

        // Delete button
        Button deleteButton = new Button("Delete Product");
        deleteButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                try {
                    // Attempt to delete the product from the backend
                    adminService.deleteProduct(productService.getProductId(selectedProduct));

                    // Remove the product from the ObservableList if deletion was successful
                    productList.remove(selectedProduct);

                    // Show confirmation to the user
                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setTitle("Product Deleted");
                    confirmation.setContentText("The product has been removed successfully!");
                    confirmation.showAndWait();
                } catch (ProductNotFoundException ex) {
                    // Handle the case where the product does not exist in the backend
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText(ex.getMessage()); // Display the exception message
                    error.showAndWait();
                } catch (Exception ex) {
                    // Handle any unexpected errors
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("An error occurred while deleting the product: " + ex.getMessage());
                    error.showAndWait();
                }
            } else {
                // If no product is selected, show a warning
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("No Selection");
                warning.setContentText("Please select a product to delete.");
                warning.showAndWait();
            }
        });
        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.setScene(adminDashboardScene));

        // Layout setup
        layout.getChildren().addAll(label, productTable, addButton, updateButton, deleteButton, backButton);

        return new Scene(layout, 900, 600);
    }


    //complete
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


    //complete
    private void openAddProductForm(Stage stage) {
        // Create a new layout for the Add Product Form
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

        Label formTitle = new Label("Add New Product");
        formTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Create input fields for product details
        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField categoryField=new TextField();
        categoryField.setPromptText("Category");

        TextField stockField = new TextField();
        stockField.setPromptText("Stock Quantity");



        // Error label for validation feedback
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Submit button
        Button submitButton = new Button("Add Product");
        submitButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            // Validate the fields before submitting
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String categoryText=categoryField.getText().trim();
            String stockText = stockField.getText().trim();



            if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);
                Category category= categoryService.parseCategory(categoryText);

                if (price < 0 || stock < 0) {
                    errorLabel.setText("Price and stock must be positive.");
                    return;
                }

                Product newProduct=productService.createProduct(name,price,category,stock);
                productList.add(newProduct);


                // Show confirmation to the user
                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("Product Added");
                confirmation.setContentText("The product has been added successfully!");
                confirmation.showAndWait();

                // Go back to the previous scene (e.g., product management scene)
                Scene productsScene = createProductsScene(stage, stage.getScene());
                stage.setScene(productsScene);

            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter valid numbers for price and stock.");
            }
        });

        // Cancel button to go back without saving
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> {
            // Go back to the previous scene
            Scene productsScene = createProductsScene(stage, stage.getScene());
            stage.setScene(productsScene);
        });

        // Layout setup
        HBox buttonBox = new HBox(10, submitButton, cancelButton);
        layout.getChildren().addAll(formTitle, nameField, priceField,categoryField, stockField, errorLabel, buttonBox);

        // Create a new scene for the Add Product Form
        Scene addProductScene = new Scene(layout, 600, 400);
        stage.setScene(addProductScene);
    }

    //complete
    private void openUpdateProductForm(Stage stage, Product product) {
        // Create a new layout for the Update Product Form
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");

        Label formTitle = new Label("Update Product");
        formTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Pre-filled input fields with product details
        TextField idField=new TextField(String.valueOf(productService.getProductId(product)));

        TextField nameField = new TextField(productService.getProductName(product));
        nameField.setPromptText("Product Name");

        TextField priceField = new TextField(Double.toString(productService.getProductPrice(product)));
        priceField.setPromptText("Price");

        TextField categoryField = new TextField(String.valueOf(productService.getProductCategory(product)));
        categoryField.setPromptText("Category");

        TextField stockField = new TextField(String.valueOf(productService.getProductQuantity(product)));
        stockField.setPromptText("Stock Quantity");

        // Error label for validation feedback
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Submit button
        Button submitButton = new Button("Update Product");
        submitButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            // Validate the fields before submitting
            String idText=idField.getText().trim();
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String categoryText = categoryField.getText().trim();
            String stockText = stockField.getText().trim();

            if (name.isEmpty() || priceText.isEmpty() ||categoryText.isEmpty()|| stockText.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            try {
                int id;
                double price;
                int stock;
                Category category;
                try {
                    id = Integer.parseInt(idText); // Parse the ID
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Invalid product ID. It must be an integer.");
                    return; // Stop here
                }

                try {
                    price = Double.parseDouble(priceText); // Parse price
                    if (price < 0) {
                        errorLabel.setText("Price must be a positive number.");
                        return; // Stop if price is invalid
                    }
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Invalid price. It must be a valid number.");
                    return; // Stop here
                }

                try {
                    stock = Integer.parseInt(stockText); // Parse stock
                    if (stock < 0) {
                        errorLabel.setText("Stock must be a positive integer.");
                        return; // Stop if stock is invalid
                    }
                } catch (NumberFormatException ex) {
                    errorLabel.setText("Invalid stock quantity. It must be an integer.");
                    return; // Stop here
                }

                // Parse category (custom validation for category parsing)
                try {
                    category = categoryService.parseCategory(categoryText); // Category parsing validation
                } catch (Exception ex) {
                    errorLabel.setText("Invalid category: " + ex.getMessage());
                    return; // Stop if parsing fails
                }




                Product updatedProduct=productService.createExistingProduct(id,name,price,category,stock);



                // Update the product in the backend
                adminService.updateProduct(updatedProduct);

                int index = productList.indexOf(product); // Find the index of the product in the list
                if (index != -1) {
                    productList.set(index, updatedProduct); // Replace the product object
                }

                // Refresh the TableView by updating the ObservableList (the list reflects changes automatically)
                productTable.refresh();

                // Show confirmation to the user
                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("Product Updated");
                confirmation.setContentText("The product has been updated successfully!");
                confirmation.showAndWait();

                // Go back to the product management scene
                Scene productsScene = createProductsScene(stage, stage.getScene());
                stage.setScene(productsScene);

            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter valid numbers for price and stock.");
            } catch (Exception ex) {
                errorLabel.setText("An error occurred: " + ex.getMessage());
            }
        });

        // Cancel button to go back without saving
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> {
            // Go back to the previous product management scene
            Scene productsScene = createProductsScene(stage, stage.getScene());
            stage.setScene(productsScene);
        });

        // Layout setup
        HBox buttonBox = new HBox(10, submitButton, cancelButton);
        layout.getChildren().addAll(formTitle, nameField, priceField, categoryField, stockField, errorLabel, buttonBox);

        // Create a new scene for the Update Product Form
        Scene updateProductScene = new Scene(layout, 600, 400);
        stage.setScene(updateProductScene);
    }

    private void openAddAdminForm(Stage stage) {
        Stage formStage = new Stage(); // New window for the add admin form
        formStage.setTitle("Add Admin");

        VBox formLayout = new VBox(10); // Layout with spacing
        formLayout.setStyle("-fx-padding: 20px;");

        // Form Fields
        /*
        TextField adminIdField = new TextField();
        adminIdField.setPromptText("Enter Admin ID");*/

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        TextField roleField = new TextField();
        roleField.setPromptText("Enter Role");

        TextField workingHoursField = new TextField();
        workingHoursField.setPromptText("Enter Working Hours");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date of Birth");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter Password");

        // Error labels for validation messages
        Label usernameErrorLabel = new Label();
        Label passwordErrorLabel = new Label();
        Label roleErrorLabel = new Label();
        Label workingHoursErrorLabel = new Label();
        Label dateErrorLabel = new Label();

        // Style error labels (optional)
        usernameErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        passwordErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        roleErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        workingHoursErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        dateErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");

        // Save button logic
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Clear all error labels on each save attempt
            usernameErrorLabel.setText("");
            passwordErrorLabel.setText("");
            roleErrorLabel.setText("");
            workingHoursErrorLabel.setText("");
            dateErrorLabel.setText("");

            // Retrieve form values
            /*
            String adminId = adminIdField.getText();*/ // Admin ID isn't validated in this example
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();
            String workingHoursText = workingHoursField.getText();
            LocalDate selectedDate = datePicker.getValue();

            // Basic empty field validation
            if ( username.isEmpty() || password.isEmpty() || role.isEmpty() || workingHoursText.isEmpty() || selectedDate == null) {
                if (username.isEmpty()) usernameErrorLabel.setText("Username is required.");
                if (password.isEmpty()) passwordErrorLabel.setText("Password is required.");
                if (role.isEmpty()) roleErrorLabel.setText("Role is required.");
                if (workingHoursText.isEmpty()) workingHoursErrorLabel.setText("Working Hours are required.");
                if (selectedDate == null) dateErrorLabel.setText("Date of Birth is required.");
                return; // Stop execution until all fields are filled
            }

            boolean usernameExists = adminsList.stream()
                    .anyMatch(admin -> admin.getUsername().equalsIgnoreCase(username));

            if (usernameExists) {
                usernameErrorLabel.setText("Username is already taken. Please choose another.");
                return; // Stop creation if the username is taken
            }


            try {
                // Parse working hours
                double workingHours = Double.parseDouble(workingHoursText);

                // Convert LocalDate to Date
                Date dateOfBirth = java.sql.Date.valueOf(selectedDate);

                // Call service method
                Admin newAdmin=adminService.createNewAdmin(username, password, dateOfBirth, role, workingHours);
                adminsList.add(newAdmin);
                adminsTable.refresh();

                // Show success dialog
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Admin Created");
                successAlert.setContentText("Admin " + username + " has been successfully created.");
                successAlert.showAndWait();

                formStage.close(); // Close the form if a new admin is created successfully

            } catch (NumberFormatException ex) {
                // Handle invalid working hours input
                workingHoursErrorLabel.setText("Working Hours must be a valid number.");
            } catch (IllegalArgumentException ex) {
                // Handle validation errors from adminService.createNewAdmin
                String errorMessage = ex.getMessage();

                // Map error messages to corresponding labels
                if (errorMessage.contains("Username")) {
                    usernameErrorLabel.setText(errorMessage);
                } else if (errorMessage.contains("Password")) {
                    passwordErrorLabel.setText(errorMessage);
                } else if (errorMessage.contains("Date of Birth")) {
                    dateErrorLabel.setText(errorMessage);
                } else if (errorMessage.contains("Role")) {
                    roleErrorLabel.setText(errorMessage);
                } else if (errorMessage.contains("Working hours")) {
                    workingHoursErrorLabel.setText(errorMessage);
                }
            } catch (Exception ex) {
                // Handle unexpected exceptions
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Unexpected Error");
                errorAlert.setContentText("An unexpected error occurred while saving the admin. Please try again.");
                errorAlert.showAndWait();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> formStage.close()); // Close the form on cancel

        // Layout for Save/Cancel buttons
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Add all components to the form layout
        formLayout.getChildren().addAll(

                new Label("Username:"), usernameField, usernameErrorLabel,
                new Label("Role:"), roleField, roleErrorLabel,
                new Label("Working Hours:"), workingHoursField, workingHoursErrorLabel,
                new Label("Date of Birth:"), datePicker, dateErrorLabel,
                new Label("Password:"), passwordField, passwordErrorLabel,
                buttonBox
        );

        Scene formScene = new Scene(formLayout, 400, 400);
        formStage.setScene(formScene);
        formStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with main window
        formStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
