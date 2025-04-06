package edu.gmu.cs321;

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
     * Opens the Petition Detail view with mock data
     */
    private void openPetitionDetail() {
        try {
            // Try multiple resource loading methods
            FXMLLoader loader = null;
            try {
                // Method 1
                loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/PetitionDetail.fxml"));
                if (loader.getLocation() == null) throw new Exception("Method 1 failed");
            } catch (Exception ex1) {
                try {
                    // Method 2
                    loader = new FXMLLoader(getClass().getResource("PetitionDetail.fxml"));
                    if (loader.getLocation() == null) throw new Exception("Method 2 failed");
                } catch (Exception ex2) {
                    // Method 3
                    loader = new FXMLLoader(FormPreview.class.getClassLoader().getResource("edu/gmu/cs321/PetitionDetail.fxml"));
                }
            }
            
            Parent root = loader.load();
            
            // Initialize with mock data
            PetitionDetailController controller = loader.getController();
            controller.loadPetition("MOCK-12345");
            
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