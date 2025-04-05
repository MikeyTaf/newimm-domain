package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Controller for the New Immigrant Form screen
 */
public class NewImmigrantFormController {

    // Main immigrant information fields
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private DatePicker datePickerDOB;
    @FXML private TextField txtNationality;
    @FXML private TextField txtPassportNumber;
    
    // Dependent section
    @FXML private VBox dependentsContainer;
    @FXML private Button btnAddDependent;
    
    // Form submission
    @FXML private Button btnSubmit;
    @FXML private Label lblStatus;
    
    // Validation and data
    private Validation validator;
    private List<DependentFormGroup> dependentGroups;
    
    /**
     * Initializes the controller
     */
    public void initialize() {
        validator = new Validation();
        dependentGroups = new ArrayList<>();
        
        // Initialize date picker format
        setupDatePicker();
        
        // Set up event handlers
        btnAddDependent.setOnAction(this::handleAddDependent);
        btnSubmit.setOnAction(this::handleSubmitForm);
    }
    
    /**
     * Sets up date picker with proper formatting
     */
    private void setupDatePicker() {
        // Convert between LocalDate and Date
        datePickerDOB.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return date.toString();
                }
                return "";
            }
            
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string);
                }
                return null;
            }
        });
        
        // Set default value to null
        datePickerDOB.setValue(null);
    }
    
    /**
     * Handles adding a new dependent to the form
     */
    private void handleAddDependent(ActionEvent event) {
        // Create a new dependent form group
        DependentFormGroup group = new DependentFormGroup();
        dependentGroups.add(group);
        
        // Add the dependent form to the UI
        dependentsContainer.getChildren().add(group.getContainer());
        
        // Set up the remove button
        group.getRemoveButton().setOnAction(e -> {
            dependentGroups.remove(group);
            dependentsContainer.getChildren().remove(group.getContainer());
        });
    }
    
    /**
     * Handles the form submission
     */
    private void handleSubmitForm(ActionEvent event) {
        // Clear previous status
        lblStatus.setText("");
        
        try {
            // Create immigrant from form data
            Immigrant immigrant = createImmigrantFromForm();
            
            // Validate immigrant
            String[] immigrantErrors = validator.getImmigrantErrors(immigrant);
            if (immigrantErrors.length > 0) {
                displayErrors("Immigrant information errors:", immigrantErrors);
                return;
            }
            
            // Process dependents
            List<Dependent> dependents = new ArrayList<>();
            for (DependentFormGroup group : dependentGroups) {
                Dependent dependent = createDependentFromGroup(group, immigrant.getImmigrantID());
                
                // Validate dependent
                String[] dependentErrors = validator.getDependentErrors(dependent);
                if (dependentErrors.length > 0) {
                    displayErrors("Dependent information errors:", dependentErrors);
                    return;
                }
                
                dependents.add(dependent);
            }
            
            // Create form model
            String petitionID = UUID.randomUUID().toString();
            NewImmFormModel formModel = new NewImmFormModel(
                immigrant,
                dependents,
                petitionID,
                new Date()
            );
            
            // Save the form
            boolean success = formModel.submitForm();
            
            if (success) {
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Form submitted successfully! Petition ID: " + petitionID);
                clearForm();
            } else {
                lblStatus.setTextFill(Color.RED);
                lblStatus.setText("Error submitting form. Please try again.");
            }
            
        } catch (Exception ex) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Creates an Immigrant object from the form data
     */
    private Immigrant createImmigrantFromForm() {
        String personID = UUID.randomUUID().toString();
        String immigrantID = UUID.randomUUID().toString();
        
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        
        // Convert LocalDate to Date
        Date dob = null;
        if (datePickerDOB.getValue() != null) {
            dob = Date.from(datePickerDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        
        String nationality = txtNationality.getText().trim();
        String passportNumber = txtPassportNumber.getText().trim();
        
        return new Immigrant(
            personID,
            firstName,
            lastName,
            dob,
            immigrantID,
            nationality,
            passportNumber
        );
    }
    
    /**
     * Creates a Dependent object from a form group
     */
    private Dependent createDependentFromGroup(DependentFormGroup group, String immigrantID) {
        String personID = UUID.randomUUID().toString();
        String dependentID = UUID.randomUUID().toString();
        
        String firstName = group.getFirstNameField().getText().trim();
        String lastName = group.getLastNameField().getText().trim();
        
        // Convert LocalDate to Date
        Date dob = null;
        if (group.getDobPicker().getValue() != null) {
            dob = Date.from(group.getDobPicker().getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        
        String relationshipType = group.getRelationshipCombo().getValue();
        String passportNumber = group.getPassportField().getText().trim();
        
        return new Dependent(
            personID,
            firstName,
            lastName,
            dob,
            dependentID,
            relationshipType,
            passportNumber,
            immigrantID
        );
    }
    
    /**
     * Displays error messages in the status label
     */
    private void displayErrors(String header, String[] errors) {
        StringBuilder sb = new StringBuilder(header);
        sb.append("\n");
        for (String error : errors) {
            sb.append("- ").append(error).append("\n");
        }
        lblStatus.setTextFill(Color.RED);
        lblStatus.setText(sb.toString());
    }
    
    /**
     * Clears the form after successful submission
     */
    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        datePickerDOB.setValue(null);
        txtNationality.clear();
        txtPassportNumber.clear();
        
        // Clear dependents
        dependentsContainer.getChildren().clear();
        dependentGroups.clear();
    }
    
    /**
     * Inner class to manage a group of dependent form fields
     */
    private class DependentFormGroup {
        private VBox container;
        private TextField firstNameField;
        private TextField lastNameField;
        private DatePicker dobPicker;
        private ComboBox<String> relationshipCombo;
        private TextField passportField;
        private Button removeButton;
        
        public DependentFormGroup() {
            // Create container
            container = new VBox(5); // 5px spacing
            container.getStyleClass().add("dependent-container");
            
            // Create fields
            firstNameField = new TextField();
            firstNameField.setPromptText("First Name");
            
            lastNameField = new TextField();
            lastNameField.setPromptText("Last Name");
            
            dobPicker = new DatePicker();
            dobPicker.setPromptText("Date of Birth");
            
            // Set up relationship combo box
            ObservableList<String> relationships = FXCollections.observableArrayList(
                "SPOUSE", "CHILD", "PARENT", "SIBLING"
            );
            relationshipCombo = new ComboBox<>(relationships);
            relationshipCombo.setPromptText("Relationship");
            
            passportField = new TextField();
            passportField.setPromptText("Passport Number");
            
            removeButton = new Button("Remove Dependent");
            removeButton.getStyleClass().add("remove-button");
            
            // Add fields to container
            container.getChildren().addAll(
                new Label("Dependent Information:"),
                firstNameField,
                lastNameField,
                dobPicker,
                relationshipCombo,
                passportField,
                removeButton,
                new Separator() // Visual separator between dependents
            );
        }
        
        public VBox getContainer() {
            return container;
        }
        
        public TextField getFirstNameField() {
            return firstNameField;
        }
        
        public TextField getLastNameField() {
            return lastNameField;
        }
        
        public DatePicker getDobPicker() {
            return dobPicker;
        }
        
        public ComboBox<String> getRelationshipCombo() {
            return relationshipCombo;
        }
        
        public TextField getPassportField() {
            return passportField;
        }
        
        public Button getRemoveButton() {
            return removeButton;
        }
    }
}