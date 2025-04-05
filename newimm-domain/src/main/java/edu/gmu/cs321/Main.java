package edu.gmu.cs321;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main application class for the Immigration Petition System
 */
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
            
            // Set up button actions
            btnNewForm.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/NewImmigrantForm.fxml"));
                    Parent root = loader.load();
                    Stage formStage = new Stage();
                    formStage.setTitle("New Immigrant Form");
                    formStage.setScene(new Scene(root, 800, 600));
                    formStage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            
            btnReviewerDashboard.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/ReviewerDashboard.fxml"));
                    Parent root = loader.load();
                    Stage dashboardStage = new Stage();
                    dashboardStage.setTitle("Reviewer Dashboard");
                    dashboardStage.setScene(new Scene(root, 800, 600));
                    dashboardStage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            
            // Configure and show the main stage
            primaryStage.setTitle("Immigration Petition System");
            primaryStage.setScene(menuScene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to launch the JavaFX application
     */
    public static void main(String[] args) {
        launch(args);
    }
}