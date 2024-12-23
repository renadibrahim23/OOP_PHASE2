package com.example.phase2;

import Entity.Customer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Service.*;
import DAO.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class SignupController {
    private final CustomerService customerService = new CustomerService();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private Label warningMessage;

    @FXML
    private Button createAccountButton;

    @FXML
    public void initialize() {
        // Populate genderComboBox with valid gender values
        genderComboBox.setItems(FXCollections.observableArrayList(Customer.Gender.getValidValues().split(" ")));
    }

    @FXML
    public void signUp(ActionEvent event) {
        // Fetch input values
        String username = usernameField.getText();
        String password = passwordField.getText();
        String address = addressField.getText();
        String genderInput = genderComboBox.getValue();
        LocalDate localDate = birthDatePicker.getValue();
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                address == null || address.isBlank() ||
                genderInput == null || localDate == null) {
            warningMessage.setText("Please fill in all the fields.");
            return;
        }

        // Convert LocalDate to SQL Date
        Date dateOfBirth = java.sql.Date.valueOf(localDate);

        // Validate gender and parse to Enum
        Customer.Gender gender;
        try {
            gender = Customer.Gender.valueOf(genderInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            warningMessage.setText("Invalid gender. Choose from: " + Customer.Gender.getValidValues());
            return;
        }

        // Centralized validation via CustomerService
        if (!customerService.signUp(username, password, dateOfBirth, address, genderInput)) {
            Platform.runLater(() -> {
                warningMessage.setText(customerService.getSignUpMessage());
            });
            return;
        }

        // Show success message and navigate to the login page
        warningMessage.setText("Signup successful! Redirecting to login.");
        PauseTransition pause = new PauseTransition(Duration.seconds(1)); // 2-second delay
        pause.setOnFinished(e -> {
            try {
                navigateToLogin(event);
            } catch (IOException ex) {
                ex.printStackTrace();
                warningMessage.setText("Failed to load the login page. Please try again later.");
            }
        });
        pause.play();
    }

    private void navigateToLogin(ActionEvent event) throws IOException {
        // Navigate to the login page
        Stage stage = (Stage) createAccountButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("loginnn.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}