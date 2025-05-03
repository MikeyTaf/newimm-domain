// src/main/java/edu/gmu/cs321/FormPreviewController.java
package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FormPreviewController {

    // Immigrant Labels
    @FXML private Label lblImmFirstName;
    @FXML private Label lblImmLastName;
    @FXML private Label lblImmDOB;
    @FXML private Label lblImmNationality;
    @FXML private Label lblImmPassport;

    // Dependents Container
    @FXML private VBox dependentsPreviewContainer;

    // Buttons & Status
    @FXML private Button btnConfirmSubmit;
    @FXML private Button btnGoBack;
    @FXML private Label lblPreviewStatus;

    // Data Storage
    private Immigrant immigrantData;
    private List<Dependent> dependentData;
    private Runnable onSuccessfulSubmitCallback; // To notify the original form

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public void initialize() {
        // Initialization logic if needed
        lblPreviewStatus.setText("");
    }

    // Method called by NewImmigrantFormController to pass data
    public void loadData(Immigrant immigrant, List<Dependent> dependents) {
        this.immigrantData = immigrant;
        this.dependentData = dependents;

        // Populate Immigrant Labels
        lblImmFirstName.setText(immigrant.getFirstName());
        lblImmLastName.setText(immigrant.getLastName());
        lblImmDOB.setText(formatDate(immigrant.getDob()));
        lblImmNationality.setText(immigrant.getNationality());
        lblImmPassport.setText(immigrant.getPassportNumber());

        // Populate Dependents
        dependentsPreviewContainer.getChildren().clear(); // Clear previous entries if any
        if (dependents != null && !dependents.isEmpty()) {
            for (Dependent dep : dependents) {
                GridPane depPane = createDependentPreviewPane(dep);
                dependentsPreviewContainer.getChildren().add(depPane);
                dependentsPreviewContainer.getChildren().add(new Separator());
            }
        } else {
            dependentsPreviewContainer.getChildren().add(new Label("No dependents entered."));
        }
    }

    // Helper to create display for one dependent
    private GridPane createDependentPreviewPane(Dependent dep) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.getColumnConstraints().add(new ColumnConstraints(120)); // Label column width

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(new Label(dep.getFirstName()), 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(new Label(dep.getLastName()), 1, 1);
        grid.add(new Label("Date of Birth:"), 0, 2);
        grid.add(new Label(formatDate(dep.getDob())), 1, 2);
        grid.add(new Label("Relationship:"), 0, 3);
        grid.add(new Label(dep.getRelationshipType()), 1, 3);
        grid.add(new Label("Passport #:"), 0, 4);
        grid.add(new Label(dep.getPassportNumber()), 1, 4);

        return grid;
    }


    // Action for the "Confirm and Submit" button
    @FXML
    private void handleConfirmSubmitAction() {
        try {
            // Create the form model using the stored data
            String petitionID = "PET-" + System.currentTimeMillis() % 10000; // Simple ID generation
            Date submissionDate = new Date();

            NewImmFormModel form = new NewImmFormModel(
                    this.immigrantData,
                    this.dependentData,
                    petitionID,
                    submissionDate
            );

            // Submit the form (this saves to DataStore and updates workflow)
            boolean success = form.submitForm();

            if (success) {
                // Show success Alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Submission Successful");
                alert.setHeaderText("Petition Submitted");
                alert.setContentText("Petition ID: " + form.getPetitionID() + " has been submitted for review.");
                alert.showAndWait();

                 // Execute the callback to notify the original form
                 if (onSuccessfulSubmitCallback != null) {
                     onSuccessfulSubmitCallback.run();
                 }

                // Close the preview window
                closeWindow();

            } else {
                lblPreviewStatus.setText("Error submitting petition. Please check logs.");
                // Optionally show an error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Submission Failed");
                alert.setHeaderText("Could not submit petition");
                alert.setContentText("An error occurred during submission. Please try again or contact support.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            lblPreviewStatus.setText("Error: " + e.getMessage());
            e.printStackTrace();
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Submission Error");
             alert.setHeaderText("An unexpected error occurred");
             alert.setContentText("Error details: " + e.getMessage());
             alert.showAndWait();
        }
    }

    // Action for the "Go Back / Edit" button
    @FXML
    private void handleGoBackAction() {
        // Simply close the preview window
        closeWindow();
    }

     // Setter for the callback
     public void setOnSuccessfulSubmit(Runnable callback) {
         this.onSuccessfulSubmitCallback = callback;
     }


    // Helper to close the current window
    private void closeWindow() {
        Stage stage = (Stage) btnGoBack.getScene().getWindow();
        stage.close();
    }

    // Helper to format dates consistently
    private String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        return dateFormat.format(date);
    }
}