package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = authenticate(username, password);

        if (role != null) {
            statusLabel.setText("Login Successful!");
            loadMainApplication(role);
        } else {
            statusLabel.setText("Invalid username or password.");
            passwordField.clear();
        }
    }

    /**
     * Authenticates the user.
     * @param username The entered username
     * @param password The entered password
     * @return The user's role ("DATA_ENTRY", "REVIEWER", "APPROVER") or null if authentication fails.
     */
    private String authenticate(String username, String password) {
        // Example hardcoded credentials - replace with real logic
        if ("entry".equalsIgnoreCase(username) && "pass1".equals(password)) {
            return "DATA_ENTRY";
        } else if ("reviewer".equalsIgnoreCase(username) && "pass2".equals(password)) {
            return "REVIEWER";
        } else if ("approver".equalsIgnoreCase(username) && "pass3".equals(password)) {
             return "APPROVER";
        }
        return null; 
    }

    private void loadMainApplication(String role) {
        String fxmlFile = null;
        String windowTitle = null;

        try {
            // Determine which FXML to load based on the role
            if ("DATA_ENTRY".equals(role)) {
                fxmlFile = "NewImmigrantForm.fxml";
                windowTitle = "New Immigrant Petition Entry";
            } else if ("REVIEWER".equals(role)) {
                fxmlFile = "ReviewerDashboard.fxml";
                windowTitle = "Reviewer Dashboard";
            } else {
                statusLabel.setText("Error: Unknown user role.");
                return;
            }

            // --- Loading the FXML ---
            URL fxmlUrl = getClass().getResource(fxmlFile);
            if (fxmlUrl == null) {
                 throw new IOException("Cannot find FXML file: " + fxmlFile + ". Check path and filename.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            
            /*Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(windowTitle);
            stage.centerOnScreen();
            stage.show(); */

            Stage newStage = new Stage();
            newStage.setTitle(windowTitle);
            newStage.setScene(scene);

            Window loginWindow = loginButton.getScene().getWindow();
            newStage.initOwner(loginWindow);


            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading application screen: " + e.getMessage());
        }
    }
}