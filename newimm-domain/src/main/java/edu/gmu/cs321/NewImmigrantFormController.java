package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class NewImmigrantFormController implements Initializable {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private DatePicker datePickerDOB;
    @FXML
    private TextField txtNationality;
    @FXML
    private TextField txtPassportNumber;
    @FXML
    private Button btnAddDependent;
    @FXML
    private VBox dependentsContainer;
    @FXML
    private Button btnSubmit;
    @FXML
    private Label lblStatus;

    // New date components
    @FXML
    private ComboBox<String> comboMonth;
    @FXML
    private ComboBox<String> comboDay;
    @FXML
    private ComboBox<String> comboYear;

    private List<DependentRowController> dependentControllers = new ArrayList<>();
    private Validation validator = new Validation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDateControls();

        btnAddDependent.setOnAction(e -> addDependentRow());
        btnSubmit.setOnAction(e -> submitForm());
    }

    private void setupDateControls() {
        // Populate month dropdown
        List<String> months = Arrays.asList("January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December");
        comboMonth.getItems().addAll(months);

        // Populate day dropdown (1-31)
        for (int i = 1; i <= 31; i++) {
            comboDay.getItems().add(String.valueOf(i));
        }

        // Populate year dropdown (current year - 100 to current year)
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 100; i--) {
            comboYear.getItems().add(String.valueOf(i));
        }

        // Add listeners to update the hidden DatePicker when selections change
        EventHandler<ActionEvent> dateChangeHandler = e -> updateDatePicker();
        comboMonth.setOnAction(dateChangeHandler);
        comboDay.setOnAction(dateChangeHandler);
        comboYear.setOnAction(dateChangeHandler);
    }

    private void updateDatePicker() {
        // Only update if all fields are selected
        if (comboMonth.getValue() != null && comboDay.getValue() != null && comboYear.getValue() != null) {
            try {
                int month = comboMonth.getItems().indexOf(comboMonth.getValue()) + 1; // Month is 1-based
                int day = Integer.parseInt(comboDay.getValue());
                int year = Integer.parseInt(comboYear.getValue());

                // Validate day based on month and year
                int maxDays = getMaxDaysInMonth(month, year);
                if (day > maxDays) {
                    // If invalid day for month, show error and reset day
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Invalid Date");
                    alert.setHeaderText("Invalid Day for Selected Month");
                    alert.setContentText("The selected month has only " + maxDays + " days.");
                    alert.showAndWait();

                    comboDay.setValue(null);
                    return;
                }

                // Update the hidden DatePicker
                datePickerDOB.setValue(LocalDate.of(year, month, day));
            } catch (Exception ex) {
                // Handle potential parsing errors
                System.err.println("Error updating date: " + ex.getMessage());
            }
        }
    }

    private int getMaxDaysInMonth(int month, int year) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                // Check for leap year
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            default:
                return 31;
        }
    }

    private void addDependentRow() {
        try {
            // Create a new dependent row
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DependentRow.fxml"));
            HBox dependentRow = loader.load();
            DependentRowController controller = loader.getController();

            // Add remove button functionality
            controller.setOnRemove(() -> {
                dependentsContainer.getChildren().remove(dependentRow);
                dependentControllers.remove(controller);
            });

            // Store the controller and add the row to the container
            dependentControllers.add(controller);
            dependentsContainer.getChildren().add(dependentRow);

            // Setup date components for the dependent
            controller.setupDateControls();

        } catch (Exception e) {
            System.err.println("Error adding dependent row: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void submitForm() {
        try {
            // Create the immigrant
            Immigrant immigrant = createImmigrant();

            // Validate immigrant
            String[] immigrantErrors = validator.getImmigrantErrors(immigrant);
            if (immigrantErrors.length > 0) {
                showValidationErrors("Invalid Immigrant Information", immigrantErrors);
                return;
            }

            // Create and validate dependents
            List<Dependent> dependents = new ArrayList<>();
            List<String> dependentErrors = new ArrayList<>();

            for (int i = 0; i < dependentControllers.size(); i++) {
                DependentRowController controller = dependentControllers.get(i);
                Dependent dependent = controller.getDependent();

                String[] errors = validator.getDependentErrors(dependent);
                if (errors.length > 0) {
                    dependentErrors.add("Dependent #" + (i + 1) + ":");
                    for (String error : errors) {
                        dependentErrors.add("  - " + error);
                    }
                } else {
                    dependents.add(dependent);
                }
            }

            if (!dependentErrors.isEmpty()) {
                showValidationErrors("Invalid Dependent Information",
                        dependentErrors.toArray(new String[0]));
                return;
            }

            // Create the form model with the correct parameter order
            String petitionID = "PET-" + System.currentTimeMillis() % 10000; // Simple ID generation
            Date submissionDate = new Date();

            NewImmFormModel form = new NewImmFormModel(
                    immigrant, // Immigrant
                    dependents, // List of dependents
                    petitionID, // Petition ID
                    submissionDate // Submission date
            );

            // Submit the form
            boolean success = form.submitForm();

            if (success) {
                lblStatus.setText("Petition submitted successfully! Petition ID: " + form.getPetitionID());
                clearForm();
            } else {
                lblStatus.setText("Error submitting petition. Please try again.");
            }

        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to create an Immigrant based on your class definition
    private Immigrant createImmigrant() {
        // Generate a unique ID for the immigrant
        String personID = "PER-" + System.currentTimeMillis() % 10000;
        String immigrantID = "IMM-" + System.currentTimeMillis() % 10000;

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String nationality = txtNationality.getText();
        String passportNumber = txtPassportNumber.getText();

        // Convert LocalDate to Date for DOB
        Date dob = null;
        if (datePickerDOB.getValue() != null) {
            dob = Date.from(datePickerDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        // Create immigrant object using the constructor with all required parameters
        Immigrant immigrant = new Immigrant(
                personID, // Person ID
                firstName, // First name
                lastName, // Last name
                dob, // Date of birth
                immigrantID, // Immigrant ID
                nationality, // Nationality
                passportNumber // Passport number
        );

        return immigrant;
    }

    private void showValidationErrors(String title, String[] errors) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(title);

        StringBuilder message = new StringBuilder();
        for (String error : errors) {
            message.append(error).append("\n");
        }

        alert.setContentText(message.toString());
        alert.showAndWait();
    }

    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        datePickerDOB.setValue(null);
        comboMonth.setValue(null);
        comboDay.setValue(null);
        comboYear.setValue(null);
        txtNationality.clear();
        txtPassportNumber.clear();
        dependentsContainer.getChildren().clear();
        dependentControllers.clear();
    }
}