package edu.gmu.cs321;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Create a simple navigation menu
            Button btnNewForm = new Button("New Immigrant Form");
            Button btnReviewerDashboard = new Button("Reviewer Dashboard");
            
            VBox menu = new VBox(10, 
                btnNewForm, 
                btnReviewerDashboard
            );
            menu.setStyle("-fx-padding: 20; -fx-alignment: center;");
            
            // Set up the scene for the menu
            Scene menuScene = new Scene(menu, 300, 200);
            
            // Set up button actions with corrected resource paths
            btnNewForm.setOnAction(e -> {
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
                            loader = new FXMLLoader(Main.class.getClassLoader().getResource("edu/gmu/cs321/NewImmigrantForm.fxml"));
                            if (loader.getLocation() == null) throw new Exception("Method 3 failed");
                        }
                    }
                    
                    // Load the form
                    Parent root = loader.load();
                    Stage formStage = new Stage();
                    formStage.setTitle("New Immigrant Form");
                    formStage.setScene(new Scene(root, 800, 600));
                    formStage.show();
                } catch (Exception ex) {
                    System.err.println("Error loading NewImmigrantForm.fxml: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            btnReviewerDashboard.setOnAction(e -> {
                try {
                    // Try multiple resource loading methods
                    FXMLLoader loader = null;
                    try {
                        // Method 1
                        loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/ReviewerDashboard.fxml"));
                        if (loader.getLocation() == null) throw new Exception("Method 1 failed");
                    } catch (Exception ex1) {
                        try {
                            // Method 2
                            loader = new FXMLLoader(getClass().getResource("ReviewerDashboard.fxml"));
                            if (loader.getLocation() == null) throw new Exception("Method 2 failed");
                        } catch (Exception ex2) {
                            // Method 3
                            loader = new FXMLLoader(Main.class.getClassLoader().getResource("edu/gmu/cs321/ReviewerDashboard.fxml"));
                            if (loader.getLocation() == null) throw new Exception("Method 3 failed");
                        }
                    }
                    
                    // Load the dashboard
                    Parent root = loader.load();
                    Stage dashboardStage = new Stage();
                    dashboardStage.setTitle("Reviewer Dashboard");
                    dashboardStage.setScene(new Scene(root, 800, 600));
                    dashboardStage.show();
                } catch (Exception ex) {
                    System.err.println("Error loading ReviewerDashboard.fxml: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            // Configure and show the main stage
            primaryStage.setTitle("Immigration Petition System");
            primaryStage.setScene(menuScene);
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error in start method: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}