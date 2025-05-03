
package edu.gmu.cs321;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

        btnSubmit.setOnAction(e -> handlePreviewAndSubmit());
        lblStatus.setText("");
    }

    private void handlePreviewAndSubmit() {
        lblStatus.setText("");
        try {

            Immigrant immigrant = createImmigrant();
            String[] immigrantErrors = validator.getImmigrantErrors(immigrant);
            if (immigrantErrors.length > 0) {
                showValidationErrors("Invalid Immigrant Information", immigrantErrors);
                return;
            }

            List<Dependent> dependents = new ArrayList<>();
            List<String> dependentErrors = new ArrayList<>();
            for (int i = 0; i < dependentControllers.size(); i++) {
                DependentRowController controller = dependentControllers.get(i);
                Dependent dependent = controller.getDependent();
                String[] errors = validator.getDependentErrors(dependent);
                if (errors.length > 0) {

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

            if (immigrantErrors.length == 0 && dependentErrors.isEmpty()) {
                showPreviewScreen(immigrant, dependents);
            }

        } catch (Exception e) {
            lblStatus.setText("Error preparing preview: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not prepare preview");
            alert.setContentText("An unexpected error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void showPreviewScreen(Immigrant immigrant, List<Dependent> dependents) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormPreview.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FormPreview.fxml. Check path/filename in resources/edu/gmu/cs321/");
            }
            Parent previewRoot = loader.load();

            FormPreviewController previewController = loader.getController();
            previewController.loadData(immigrant, dependents);

            Stage previewStage = new Stage();
            previewStage.setTitle("Petition Preview");
            previewStage.initModality(Modality.APPLICATION_MODAL);
            previewStage.initOwner(btnSubmit.getScene().getWindow());
            previewStage.setScene(new Scene(previewRoot));

            previewController.setOnSuccessfulSubmit(() -> {
                lblStatus.setText("Petition submitted successfully!");
                clearForm();
            });

            previewStage.showAndWait();

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

    private Immigrant createImmigrant() {

        String personID = "PER-" + System.currentTimeMillis() % 100000;
        String immigrantID = "IMM-" + System.currentTimeMillis() % 100000;

        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String nationality = txtNationality.getText().trim();
        String passportNumber = txtPassportNumber.getText().trim().toUpperCase();

        Date dob = null;
        if (datePickerDOB.getValue() != null) {
            dob = Date.from(datePickerDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        Immigrant immigrant = new Immigrant(
                personID,
                firstName,
                lastName,
                dob,
                immigrantID,
                nationality,
                passportNumber);
        return immigrant;
    }

    private void addDependentRow() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("DependentRow.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find DependentRow.fxml. Check path/filename in resources/edu/gmu/cs321/");
            }
            HBox dependentRow = loader.load();
            DependentRowController controller = loader.getController();

            controller.setOnRemove(() -> {
                dependentsContainer.getChildren().remove(dependentRow);
                dependentControllers.remove(controller);
            });

            dependentControllers.add(controller);
            dependentsContainer.getChildren().add(dependentRow);

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

    private void showValidationErrors(String title, String[] errors) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(title);

        String message = Arrays.stream(errors)
                .collect(Collectors.joining("\n"));

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);

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
        lblStatus.setText("Form cleared.");
    }

    private void setupDateControls() {

        List<String> months = Arrays.asList("January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December");
        comboMonth.getItems().setAll(months);

        List<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }
        comboDay.getItems().setAll(days);

        int currentYear = LocalDate.now().getYear();
        List<String> years = new ArrayList<>();
        for (int i = currentYear + 10; i >= currentYear - 100; i--) {
            years.add(String.valueOf(i));
        }
        comboYear.getItems().setAll(years);

        EventHandler<ActionEvent> dateChangeHandler = e -> updateDatePicker();
        comboMonth.setOnAction(dateChangeHandler);
        comboDay.setOnAction(dateChangeHandler);
        comboYear.setOnAction(dateChangeHandler);

        datePickerDOB.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {

                boolean monthMatch = comboMonth.getSelectionModel().getSelectedIndex() == newDate.getMonthValue() - 1;
                boolean dayMatch = String.valueOf(newDate.getDayOfMonth()).equals(comboDay.getValue());
                boolean yearMatch = String.valueOf(newDate.getYear()).equals(comboYear.getValue());

                if (!monthMatch || !dayMatch || !yearMatch) {
                    comboMonth.getSelectionModel().select(newDate.getMonthValue() - 1);
                    comboDay.setValue(String.valueOf(newDate.getDayOfMonth()));
                    comboYear.setValue(String.valueOf(newDate.getYear()));
                }
            } else {

                comboMonth.setValue(null);
                comboDay.setValue(null);
                comboYear.setValue(null);
            }
        });
    }

    private void updateDatePicker() {
        if (comboMonth.getValue() != null && comboDay.getValue() != null && comboYear.getValue() != null) {
            try {
                int monthIndex = comboMonth.getSelectionModel().getSelectedIndex();
                if (monthIndex < 0)
                    return;
                int month = monthIndex + 1;

                int day = Integer.parseInt(comboDay.getValue());
                int year = Integer.parseInt(comboYear.getValue());

                int maxDays = getMaxDaysInMonth(month, year);
                if (day > maxDays) {

                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Invalid Date");
                    alert.setHeaderText("Invalid Day for Selected Month/Year");
                    alert.setContentText("The selected month (" + comboMonth.getValue() + ") in year " + year
                            + " has only " + maxDays + " days.");
                    alert.showAndWait();

                    comboDay.setValue(null);
                    datePickerDOB.setValue(null);
                    return;
                }

                LocalDate newDate = LocalDate.of(year, month, day);

                if (!newDate.equals(datePickerDOB.getValue())) {
                    datePickerDOB.setValue(newDate);
                }

            } catch (NumberFormatException ex) {
                System.err.println("Error parsing day/year: " + ex.getMessage());
                datePickerDOB.setValue(null);
            } catch (Exception ex) {
                System.err.println("Error updating date: " + ex.getMessage());
                ex.printStackTrace();
                datePickerDOB.setValue(null);
            }
        } else {

            if (datePickerDOB.getValue() != null) {
                datePickerDOB.setValue(null);
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

                boolean isLeap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                return isLeap ? 29 : 28;
            default:
                return 31;
        }
    }
}