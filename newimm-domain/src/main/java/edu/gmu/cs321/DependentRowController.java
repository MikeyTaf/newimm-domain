package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class DependentRowController implements Initializable {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private DatePicker datePickerDOB;
    @FXML
    private ComboBox<String> comboRelationship;
    @FXML
    private TextField txtPassportNumber;
    @FXML
    private Button btnRemove;

    // New date components
    @FXML
    private ComboBox<String> comboMonth;
    @FXML
    private ComboBox<String> comboDay;
    @FXML
    private ComboBox<String> comboYear;

    private Runnable onRemove;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize relationship dropdown
        comboRelationship.getItems().addAll("SPOUSE", "CHILD", "PARENT", "SIBLING");

        btnRemove.setOnAction(e -> {
            if (onRemove != null) {
                onRemove.run();
            }
        });
    }

    public void setupDateControls() {
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
                    Alert alert = new Alert(Alert.AlertType.WARNING);
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

    public void setOnRemove(Runnable onRemove) {
        this.onRemove = onRemove;
    }

    public Dependent getDependent() {
        // Generate a unique ID for the dependent
        String dependentID = "DEP-" + System.currentTimeMillis() % 10000;
        String personID = "PER-" + System.currentTimeMillis() % 10000;

        // Get values from form fields
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String relationshipType = comboRelationship.getValue();
        String passportNumber = txtPassportNumber.getText();

        // Convert LocalDate to Date for DOB
        Date dob = null;
        if (datePickerDOB.getValue() != null) {
            dob = Date.from(datePickerDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        // Create dependent object using the constructor with parameters
        Dependent dependent = new Dependent(
                personID, // Person ID
                firstName, // First name
                lastName, // Last name
                dob, // Date of birth
                dependentID, // Dependent ID
                relationshipType, // Relationship type
                passportNumber // Passport number
        );

        return dependent;
    }
}