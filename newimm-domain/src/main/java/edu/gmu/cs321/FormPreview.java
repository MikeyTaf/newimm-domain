package edu.gmu.cs321;

import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FormPreview extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create a simple UI for selecting which form to view
            Label title = new Label("Select a form to preview");
            title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
            
            Button btnNewForm = new Button("New Immigrant Form");
            Button btnPetitionDetail = new Button("Petition Detail");
            
            // Set up button actions
            btnNewForm.setOnAction(e -> openNewImmigrantForm());
            btnPetitionDetail.setOnAction(e -> openPetitionDetail());
            
            // Create layout
            VBox root = new VBox(20);
            root.getChildren().addAll(title, btnNewForm, btnPetitionDetail);
            root.setStyle("-fx-padding: 20; -fx-alignment: center;");
            
            // Set up scene
            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("Form Preview");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the New Immigrant Form
     */
    private void openNewImmigrantForm() {
        try {
            // Try multiple resource loading methods
            FXMLLoader loader = null;
            try {
                // Method 1
                loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/NewImmigrantForm.fxml"));
                if (loader.getLocation() == null) throw new Exception("Method 1 failed");
            } catch (Exception ex1) {
                try {
                    // Method 2
                    loader = new FXMLLoader(getClass().getResource("NewImmigrantForm.fxml"));
                    if (loader.getLocation() == null) throw new Exception("Method 2 failed");
                } catch (Exception ex2) {
                    // Method 3
                    loader = new FXMLLoader(FormPreview.class.getClassLoader().getResource("edu/gmu/cs321/NewImmigrantForm.fxml"));
                }
            }
            
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("New Immigrant Form");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error opening New Immigrant Form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Opens the Petition Detail view with real data if available, or mock data if not
     */
    private void openPetitionDetail() {
        try {
            // Try to get a real petition ID from DataStore
            List<NewImmFormModel> allPetitions = DataStore.getInstance().getAllPetitions();
            String petitionID = "MOCK-12345"; // Default to mock ID
            
            if (!allPetitions.isEmpty()) {
                petitionID = allPetitions.get(0).getPetitionID();
                System.out.println("Using real petition ID for preview: " + petitionID);
            } else {
                System.out.println("No petitions found in DataStore, using mock ID");
            }
            
            // Try multiple resource loading methods
            FXMLLoader loader = null;
            Parent root = null;
            
            try {
                // Method 1
                loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/PetitionDetail.fxml"));
                if (loader.getLocation() == null) throw new Exception("Method 1 failed");
                root = loader.load();
            } catch (Exception ex1) {
                try {
                    // Method 2
                    loader = new FXMLLoader(getClass().getResource("PetitionDetail.fxml"));
                    if (loader.getLocation() == null) throw new Exception("Method 2 failed");
                    root = loader.load();
                } catch (Exception ex2) {
                    try {
                        // Method 3
                        loader = new FXMLLoader(FormPreview.class.getClassLoader().getResource("edu/gmu/cs321/PetitionDetail.fxml"));
                        if (loader == null || loader.getLocation() == null) {
                            throw new Exception("Could not find PetitionDetail.fxml");
                        }
                        root = loader.load();
                    } catch (Exception ex3) {
                        throw new Exception("Failed to load PetitionDetail.fxml: " + ex3.getMessage());
                    }
                }
            }
            
            if (loader == null || root == null) {
                throw new Exception("Failed to load PetitionDetail.fxml");
            }
            
            // Initialize with data
            PetitionDetailController controller = loader.getController();
            controller.loadPetition(petitionID);
            
            Stage stage = new Stage();
            stage.setTitle("Petition Detail");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error opening Petition Detail: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}