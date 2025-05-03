// src/main/java/edu/gmu/cs321/NewImmigrantFormController.java
package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent; // Import Parent
import javafx.scene.Scene;  // Import Scene
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Modality; // Import Modality
import javafx.stage.Stage;    // Import Stage

import java.io.IOException; // Import IOException
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors; // Import Collectors

public class NewImmigrantFormController implements Initializable {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private DatePicker datePickerDOB; // Hidden DatePicker
    @FXML
    private TextField txtNationality;
    @FXML
    private TextField txtPassportNumber;
    @FXML
    private Button btnAddDependent;
    @FXML
    private VBox dependentsContainer;
    @FXML
    private Button btnSubmit; // This button now triggers the preview
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
        // The btnSubmit now leads to the preview/submit logic
        btnSubmit.setOnAction(e -> handlePreviewAndSubmit()); // Changed handler name for clarity
        lblStatus.setText(""); // Clear status on init
    }

    // This method now handles PREVIEWING before final submission
    private void handlePreviewAndSubmit() {
        lblStatus.setText(""); // Clear previous status
        try {
            // 1. Create and Validate Immigrant
            Immigrant immigrant = createImmigrant();
            String[] immigrantErrors = validator.getImmigrantErrors(immigrant);
            if (immigrantErrors.length > 0) {
                showValidationErrors("Invalid Immigrant Information", immigrantErrors);
                return;
            }

            // 2. Create and Validate Dependents
            List<Dependent> dependents = new ArrayList<>();
            List<String> dependentErrors = new ArrayList<>();
            for (int i = 0; i < dependentControllers.size(); i++) {
                DependentRowController controller = dependentControllers.get(i);
                Dependent dependent = controller.getDependent();
                String[] errors = validator.getDependentErrors(dependent);
                if (errors.length > 0) {
                    // Format errors nicely for display
                    dependentErrors.add("Dependent #" + (i + 1) + ":");
                    dependentErrors.addAll(Arrays.stream(errors)
                                                 .map(err -> "  - " + err)
                                                 .collect(Collectors.toList()));
                } else {
                    dependents.add(dependent);
                }
            }
            if (!dependentErrors.isEmpty()) {
                showValidationErrors("Invalid Dependent Information", dependentErrors.toArray(new String[0]));
                return;
            }

            // 3. --- NEW: Load Preview Screen ---
            // Only show preview if all validations passed
            if (immigrantErrors.length == 0 && dependentErrors.isEmpty()) {
                showPreviewScreen(immigrant, dependents);
            }

        } catch (Exception e) {
            lblStatus.setText("Error preparing preview: " + e.getMessage());
            e.printStackTrace();
            // Show a user-friendly error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not prepare preview");
            alert.setContentText("An unexpected error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // --- NEW Method to show the preview screen ---
    private void showPreviewScreen(Immigrant immigrant, List<Dependent> dependents) {
        try {
            // Ensure the FXML file path is correct relative to the resources folder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormPreview.fxml"));
             if (loader.getLocation() == null) {
                throw new IOException("Cannot find FormPreview.fxml. Check path/filename in resources/edu/gmu/cs321/");
            }
            Parent previewRoot = loader.load();

            // Get the preview controller and pass data
            FormPreviewController previewController = loader.getController();
            previewController.loadData(immigrant, dependents);

             // Create a new stage (window) for the preview
            Stage previewStage = new Stage();
            previewStage.setTitle("Petition Preview");
            previewStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main form
            previewStage.initOwner(btnSubmit.getScene().getWindow()); // Link to main window
            previewStage.setScene(new Scene(previewRoot));

            // Set action for when preview is submitted successfully
            // This lambda expression will be executed by the preview controller upon success
             previewController.setOnSuccessfulSubmit(() -> {
                 lblStatus.setText("Petition submitted successfully!"); // Update status on original form
                 clearForm(); // Clear the original form
             });

            previewStage.showAndWait(); // Show preview window and wait for it to close

            // Optional: Check a status from previewController if needed after it closes
            // e.g., if (previewController.wasSubmitted()) { ... }

        } catch (IOException e) {
            lblStatus.setText("Error loading preview screen: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load preview screen");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        } catch (IllegalStateException e) {
             lblStatus.setText("Error: Controller/FXML issue with FormPreview.fxml.");
             e.printStackTrace();
              Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Error");
             alert.setHeaderText("UI Loading Error");
             alert.setContentText("Could not initialize the preview screen components.");
             alert.showAndWait();
        }
    }

    // Helper method to create an Immigrant based on the form fields
    private Immigrant createImmigrant() {
        // Generate unique IDs (consider a more robust approach for production)
        String personID = "PER-" + System.currentTimeMillis() % 100000; // Increased range slightly
        String immigrantID = "IMM-" + System.currentTimeMillis() % 100000;

        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String nationality = txtNationality.getText().trim();
        String passportNumber = txtPassportNumber.getText().trim().toUpperCase(); // Standardize passport input

        // Convert LocalDate from hidden DatePicker to Date for DOB
        Date dob = null;
        if (datePickerDOB.getValue() != null) {
            dob = Date.from(datePickerDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        // Create immigrant object using the constructor
        Immigrant immigrant = new Immigrant(
                personID,
                firstName,
                lastName,
                dob,
                immigrantID,
                nationality,
                passportNumber
        );
        return immigrant;
    }

     // Helper method to add a new dependent row UI element
     private void addDependentRow() {
        try {
            // Ensure the FXML file path is correct
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DependentRow.fxml"));
             if (loader.getLocation() == null) {
                throw new IOException("Cannot find DependentRow.fxml. Check path/filename in resources/edu/gmu/cs321/");
            }
            HBox dependentRow = loader.load(); // Load the HBox defined in DependentRow.fxml
            DependentRowController controller = loader.getController();

            // Add remove button functionality using a lambda expression
            controller.setOnRemove(() -> {
                dependentsContainer.getChildren().remove(dependentRow);
                dependentControllers.remove(controller);
            });

            // Store the controller and add the row to the VBox container
            dependentControllers.add(controller);
            dependentsContainer.getChildren().add(dependentRow);

            // IMPORTANT: Setup the date controls for the newly added dependent row
            controller.setupDateControls();

        } catch (IOException e) {
            System.err.println("Error adding dependent row: " + e.getMessage());
            e.printStackTrace();
             lblStatus.setText("Error adding dependent row: " + e.getMessage());
              Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("UI Error");
             alert.setHeaderText("Could not add dependent fields");
             alert.setContentText("Details: " + e.getMessage());
             alert.showAndWait();
        } catch (IllegalStateException e) {
             System.err.println("Error initializing dependent row controller: " + e.getMessage());
             e.printStackTrace();
             lblStatus.setText("Error initializing dependent fields.");
              Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("UI Error");
             alert.setHeaderText("Could not initialize dependent fields");
             alert.setContentText("Details: " + e.getMessage());
             alert.showAndWait();
        }
    }

    // Helper method to display validation errors in an Alert dialog
    private void showValidationErrors(String title, String[] errors) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(title);

        // Use streams for concise message building
        String message = Arrays.stream(errors)
                             .collect(Collectors.joining("\n"));

        // Use a TextArea for potentially long error messages
        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);

        alert.showAndWait();
    }

    // Helper method to clear all fields on the form
    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        datePickerDOB.setValue(null); // Clear hidden picker
        // Clear visible date dropdowns
        comboMonth.setValue(null);
        comboDay.setValue(null);
        comboYear.setValue(null);
        txtNationality.clear();
        txtPassportNumber.clear();
        // Clear dependents from UI and controller list
        dependentsContainer.getChildren().clear();
        dependentControllers.clear();
        lblStatus.setText("Form cleared."); // Provide feedback
    }

     // --- Date Handling Helper Methods ---

    private void setupDateControls() {
        // Populate month dropdown
        List<String> months = Arrays.asList("January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December");
        comboMonth.getItems().setAll(months); // Use setAll to clear previous items if any

        // Populate day dropdown (1-31)
        List<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        comboDay.getItems().setAll(days);

        // Populate year dropdown (current year - 100 to current year + 10, maybe for future dates?)
        // Adjusted range slightly based on common use cases
        int currentYear = LocalDate.now().getYear();
        List<String> years = new ArrayList<>();
        for (int i = currentYear + 10; i >= currentYear - 100; i--) {
            years.add(String.valueOf(i));
        }
        comboYear.getItems().setAll(years);

        // Add listeners to update the hidden DatePicker when selections change
        EventHandler<ActionEvent> dateChangeHandler = e -> updateDatePicker();
        comboMonth.setOnAction(dateChangeHandler);
        comboDay.setOnAction(dateChangeHandler);
        comboYear.setOnAction(dateChangeHandler);

         // Add listener to hidden DatePicker to update dropdowns if its value changes externally
         datePickerDOB.valueProperty().addListener((obs, oldDate, newDate) -> {
             if (newDate != null) {
                 // Check if dropdowns already match to avoid infinite loops
                 boolean monthMatch = comboMonth.getSelectionModel().getSelectedIndex() == newDate.getMonthValue() - 1;
                 boolean dayMatch = String.valueOf(newDate.getDayOfMonth()).equals(comboDay.getValue());
                 boolean yearMatch = String.valueOf(newDate.getYear()).equals(comboYear.getValue());

                 if (!monthMatch || !dayMatch || !yearMatch) {
                     comboMonth.getSelectionModel().select(newDate.getMonthValue() - 1);
                     comboDay.setValue(String.valueOf(newDate.getDayOfMonth()));
                     comboYear.setValue(String.valueOf(newDate.getYear()));
                 }
             } else {
                 // If DatePicker is cleared, clear dropdowns
                 comboMonth.setValue(null);
                 comboDay.setValue(null);
                 comboYear.setValue(null);
             }
         });
    }

    // Updates the hidden DatePicker based on dropdown selections
    private void updateDatePicker() {
        if (comboMonth.getValue() != null && comboDay.getValue() != null && comboYear.getValue() != null) {
            try {
                int monthIndex = comboMonth.getSelectionModel().getSelectedIndex();
                if (monthIndex < 0) return; // No month selected
                int month = monthIndex + 1; // Month is 1-based

                int day = Integer.parseInt(comboDay.getValue());
                int year = Integer.parseInt(comboYear.getValue());

                // Validate day based on month and year
                int maxDays = getMaxDaysInMonth(month, year);
                if (day > maxDays) {
                    // If invalid day for month, show error and reset day dropdown
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Invalid Date");
                    alert.setHeaderText("Invalid Day for Selected Month/Year");
                    alert.setContentText("The selected month (" + comboMonth.getValue() + ") in year " + year + " has only " + maxDays + " days.");
                    alert.showAndWait();

                    comboDay.setValue(null); // Reset day
                    datePickerDOB.setValue(null); // Clear hidden picker too
                    return;
                }

                // Update the hidden DatePicker - this might trigger its listener
                 LocalDate newDate = LocalDate.of(year, month, day);
                 // Only update if the value is actually different to prevent potential loops
                 if (!newDate.equals(datePickerDOB.getValue())) {
                    datePickerDOB.setValue(newDate);
                 }

            } catch (NumberFormatException ex) {
                System.err.println("Error parsing day/year: " + ex.getMessage());
                datePickerDOB.setValue(null); // Clear if parsing fails
            } catch (Exception ex) {
                System.err.println("Error updating date: " + ex.getMessage());
                ex.printStackTrace();
                datePickerDOB.setValue(null); // Clear on other errors
            }
        } else {
             // If any dropdown is null, clear the hidden DatePicker
             if (datePickerDOB.getValue() != null) {
                 datePickerDOB.setValue(null);
             }
        }
    }

    // Returns the maximum number of days in a given month and year
    private int getMaxDaysInMonth(int month, int year) {
        switch (month) {
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                return 30;
            case 2: // February
                // Check for leap year: divisible by 4, unless divisible by 100 but not by 400
                boolean isLeap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                return isLeap ? 29 : 28;
            default: // Jan, Mar, May, Jul, Aug, Oct, Dec
                return 31;
        }
    }
}