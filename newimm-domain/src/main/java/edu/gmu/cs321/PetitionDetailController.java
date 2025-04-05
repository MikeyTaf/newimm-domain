package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.UUID;
import java.util.Date;

/**
 * Controller for the Petition Detail screen
 */
public class PetitionDetailController {

    @FXML private Label lblPetitionID;
    @FXML private Label lblSubmissionDate;
    @FXML private Label lblStatus;
    
    // Immigrant information
    @FXML private Label lblImmigrantName;
    @FXML private Label lblImmigrantDOB;
    @FXML private Label lblNationality;
    @FXML private Label lblPassportNumber;
    
    // Dependents
    @FXML private VBox dependentsContainer;
    
    // Review section
    @FXML private TextArea txtComments;
    @FXML private RadioButton radApprove;
    @FXML private RadioButton radReject;
    @FXML private Button btnSubmitDecision;
    @FXML private Button btnClose;
    
    private String petitionID;
    private ToggleGroup decisionGroup;
    
    /**
     * Initializes the controller
     */
    public void initialize() {
        // Set up toggle group for radio buttons
        decisionGroup = new ToggleGroup();
        radApprove.setToggleGroup(decisionGroup);
        radReject.setToggleGroup(decisionGroup);
        
        // Set up button handlers
        btnSubmitDecision.setOnAction(this::handleSubmitDecision);
        btnClose.setOnAction(this::handleClose);
    }
    
    /**
     * Loads petition data based on petition ID
     */
    public void loadPetition(String petitionID) {
        this.petitionID = petitionID;
        
        // In a real application, this would load data from the database
        // For now, we'll use mock data
        
        // Set petition details
        lblPetitionID.setText(petitionID);
        lblSubmissionDate.setText("08/15/2023");
        lblStatus.setText("SUBMITTED");
        
        // Set immigrant details
        lblImmigrantName.setText("John Smith");
        lblImmigrantDOB.setText("01/15/1980");
        lblNationality.setText("Canada");
        lblPassportNumber.setText("C1234567");
        
        // Add mock dependents
        addDependentToUI("Sarah Smith", "SPOUSE", "05/22/1982", "C7654321");
        addDependentToUI("Michael Smith", "CHILD", "10/30/2010", "C8765432");
    }
    
    /**
     * Adds a dependent to the UI
     */
    private void addDependentToUI(String name, String relationship, String dob, String passport) {
        // Create labels for dependent info
        Label lblName = new Label("Name: " + name);
        Label lblRelationship = new Label("Relationship: " + relationship);
        Label lblDOB = new Label("Date of Birth: " + dob);
        Label lblPassport = new Label("Passport: " + passport);
        
        // Create container for this dependent
        VBox dependentBox = new VBox(5, 
            new Label("Dependent Information:"),
            lblName, 
            lblRelationship, 
            lblDOB, 
            lblPassport,
            new Separator()
        );
        dependentBox.getStyleClass().add("dependent-box");
        
        // Add to the container
        dependentsContainer.getChildren().add(dependentBox);
    }
    
    /**
     * Handles the Submit Decision button click
     */
    private void handleSubmitDecision(ActionEvent event) {
        if (decisionGroup.getSelectedToggle() == null) {
            showAlert("Please select Approve or Reject");
            return;
        }
        
        if (txtComments.getText().trim().isEmpty()) {
            showAlert("Please provide comments for your decision");
            return;
        }
        
        boolean approved = decisionGroup.getSelectedToggle() == radApprove;
        
        // In a real application, this would save to the database
        // For demonstration, we'll just show a confirmation
        
        // Create review record
        String reviewID = UUID.randomUUID().toString();
        String reviewerID = "REV-001"; // In a real app, this would be the logged-in reviewer
        
        new Review(
            reviewID,
            petitionID,
            reviewerID,
            new Date(),
            txtComments.getText().trim(),
            approved
        );
        
        // Show confirmation
        String message = "Decision submitted: " + (approved ? "APPROVED" : "REJECTED") + 
                        "\nComments: " + txtComments.getText().trim();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Decision Submitted");
        alert.setHeaderText("Petition Review Complete");
        alert.setContentText(message);
        alert.showAndWait();
        
        // Close the window
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Handles the Close button click
     */
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Shows an alert with the given message
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Action Required");
        alert.setContentText(message);
        alert.showAndWait();
    }
}