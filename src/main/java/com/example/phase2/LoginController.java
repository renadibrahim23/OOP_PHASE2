package com.example.phase2;

import Service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.*;
import Entity.*;


import java.io.IOException;
import java.util.Date;

public class LoginController {
    private final AdminService adminService = new Service.AdminService();
    private final CustomerService customerService = new Service.CustomerService();

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
            // Navigate to the admin dashboard
            try {
                Stage stage = (Stage) loginasAdmin.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }

    // Customer Login
    public void loginAsCustomer(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            warningMessage.setText("Please fill in all fields.");
            return;
        }


        boolean success = customerService.logIn(username, password);
        warningMessage.setText(customerService.getWarningMessage());
        if (success) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customer.fxml"));
            Parent root = loader.load();

            // Show the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
    public void goToSignupPage(ActionEvent event) {
        try {
            // Adjust the path to your SignupPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Signup.fxml"));
            Parent root = loader.load();
            Stage stage=(Stage)signupButton.getScene().getWindow();
            Scene signupScene = new Scene(root);
            stage.setScene(signupScene);
            stage.setTitle("Signup Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}