package com.example.phase2;

import Entity.Customer;
import GUI.AdminDashBoard;
import GUI.CartWindow;
import GUI.OrderWindow;
import Service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    private final AdminService adminService = new AdminService();
    private final CustomerService customerService = new CustomerService();


    @FXML
    private Button loginasAdmin;
    @FXML
    private Button loginasCustomer;
    @FXML
    private Button signupButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label warningMessage;

    @FXML
    private Button checkoutButton;

    // Admin Login

    public void loginAsAdmin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            warningMessage.setText("Please fill in all fields.");
            return;
        }

        boolean success = adminService.logIn(username, password);
        warningMessage.setText(adminService.getLastMessage());
        if (success) {
            try {
                // Get the current stage
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Initialize and switch to the Admin Dashboard
                AdminDashBoard adminDashboard = new AdminDashBoard();
                adminDashboard.start(stage); // Start Admin Dashboard, switches the stage scene
            } catch (Exception e) {
                e.printStackTrace(); // Handle any exceptions during the transition
            }
        }
    }

    // Customer Login
    public void loginAsCustomer(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            warningMessage.setText("Please fill in all fields.");
            return;
        }

        boolean success = customerService.logIn(username, password);
        warningMessage.setText(customerService.getWarningMessage());
        if (success) {
            try {
                // Retrieve the currently logged-in customer from CustomerService
                Customer customer = customerService.getCustomerByUsername(username);
                if (customer == null) {
                    warningMessage.setText("Error: No customer found.");
                    return;
                }

                // Switch to Cart Window
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Pass the customer to CartWindow
                CartWindow cartWindow = new CartWindow(customer);
                Scene scene = cartWindow.createCartScene();

                stage.setScene(scene);
                stage.setTitle("Cart Window");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }



    // Navigate to Signup Page
    public void goToSignupPage(ActionEvent event) {
        try {
            // Logic to load signup page (if appropriate FXML or layout is set up)
            warningMessage.setText("Signup Page Navigation is not yet implemented.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}