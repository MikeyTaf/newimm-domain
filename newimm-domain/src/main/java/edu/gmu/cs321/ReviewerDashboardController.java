package edu.gmu.cs321; // Changed to match your project structure

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ReviewerDashboardController {

    @FXML
    private TableView<PetitionTableItem> petitionsTable;
    @FXML
    private TableColumn<PetitionTableItem, String> petitionIdColumn;
    @FXML
    private TableColumn<PetitionTableItem, String> immigrantNameColumn;
    @FXML
    private TableColumn<PetitionTableItem, String> submissionDateColumn;
    @FXML
    private TableColumn<PetitionTableItem, String> statusColumn;

    @FXML
    private Button viewButton;
    @FXML
    private Button refreshButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label warningLabel;

    private ObservableList<PetitionTableItem> petitions = FXCollections.observableArrayList();

    // Helper class for table display
    public static class PetitionTableItem {
        private String petitionId;
        private String immigrantName;
        private String submissionDate;
        private String status;

        public PetitionTableItem(String petitionId, String immigrantName, String submissionDate, String status) {
            this.petitionId = petitionId;
            this.immigrantName = immigrantName;
            this.submissionDate = submissionDate;
            this.status = status;
        }

        // Getters - removed duplicate methods
        public String getPetitionId() {
            return petitionId;
        }

        public String getImmigrantName() {
            return immigrantName;
        }

        public String getSubmissionDate() {
            return submissionDate;
        }

        public String getStatus() {
            return status;
        }
    }

    @FXML
    public void initialize() {
        // Set up the table columns
        petitionIdColumn.setCellValueFactory(new PropertyValueFactory<>("petitionId"));
        immigrantNameColumn.setCellValueFactory(new PropertyValueFactory<>("immigrantName"));
        submissionDateColumn.setCellValueFactory(new PropertyValueFactory<>("submissionDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        petitionsTable.setItems(petitions);

        // Disable view button until selection is made
        viewButton.setDisable(true);
        warningLabel.setVisible(false);

        // Add listener for table selection
        petitionsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    viewButton.setDisable(newSelection == null);
                });

        // Load petitions
        loadPetitions();
    }

    private void loadPetitions() {
        petitions.clear();
        warningLabel.setVisible(false);
    
        Map<String,Integer> passportCount = new HashMap<>();
        List<String> duplicatePassportNumbers = new ArrayList<>();
    
        List<Review> pendingReviews = Review.getPendingReviews();
        System.out.println("Found " + pendingReviews.size() + " pending reviews");
    
        for (Review review : pendingReviews) {
            String petitionId = review.getPetitionID();
            NewImmFormModel petition = NewImmFormModel.getPetition(petitionId);
            if (petition != null) {
                String passport = petition.getImmigrant().getPassportNumber();
                passportCount.put(passport, passportCount.getOrDefault(passport, 0) + 1);
    
                String immigrantName = petition.getImmigrant().getFirstName() + " " +
                                       petition.getImmigrant().getLastName();
                String submissionDate = petition.getSubmissionDate().toString();
                petitions.add(new PetitionTableItem(
                    petitionId, immigrantName, submissionDate, "PENDING"));
                System.out.println("Added petition: " + petitionId + " - " + immigrantName);
            } else {
                System.out.println("Could not find petition with ID: " + petitionId);
            }
        }
    
        duplicatePassportNumbers = passportCount.entrySet().stream()
            .filter(e -> e.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    
        if (!duplicatePassportNumbers.isEmpty()) {
            warningLabel.setText("⚠️ Duplicate passport number(s): " +
                                 String.join(", ", duplicatePassportNumbers));
            warningLabel.setVisible(true);
        }
    
        statusLabel.setText("Loaded " + petitions.size() + " pending petitions");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        System.out.println("Refreshing petitions list");
        loadPetitions();
    }

    @FXML
    private void handleView(ActionEvent event) {
        PetitionTableItem selected = petitionsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String petitionId = selected.getPetitionId();
                System.out.println("Opening petition details for: " + petitionId);

                // Load the petition detail view
                FXMLLoader loader = null;
                try {
                    // Method 1
                    loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/PetitionDetail.fxml"));
                    if (loader.getLocation() == null)
                        throw new Exception("Method 1 failed");
                } catch (Exception ex1) {
                    try {
                        // Method 2
                        loader = new FXMLLoader(getClass().getResource("PetitionDetail.fxml"));
                        if (loader.getLocation() == null)
                            throw new Exception("Method 2 failed");
                    } catch (Exception ex2) {
                        // Method 3
                        loader = new FXMLLoader(ReviewerDashboardController.class.getClassLoader()
                                .getResource("edu/gmu/cs321/PetitionDetail.fxml"));
                        if (loader.getLocation() == null) {
                            throw new Exception("Could not find PetitionDetail.fxml");
                        }
                    }
                }

                Parent root = loader.load();

                // Get the controller and pass the petition ID
                PetitionDetailController controller = loader.getController();
                System.out.println("Sending petition ID to detail controller: " + petitionId);
                controller.loadPetition(petitionId); // Make sure this is passing the selected petition ID

                // Show the detail view
                Stage stage = new Stage();
                stage.setTitle("Petition Details");
                stage.setScene(new Scene(root, 500, 600));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Error loading petition details: " + e.getMessage());
            }
        }
    }
}