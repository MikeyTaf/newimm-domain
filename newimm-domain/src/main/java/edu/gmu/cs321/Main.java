package edu.gmu.cs321;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Immigration Petition System
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/gmu/cs321/NewImmigrantForm.fxml"));
            Parent root = loader.load();
            
            // Set up the scene
            Scene scene = new Scene(root, 800, 600);
            
            // Configure and show the stage
            primaryStage.setTitle("Immigration Petition System");
            primaryStage.setScene(scene);
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