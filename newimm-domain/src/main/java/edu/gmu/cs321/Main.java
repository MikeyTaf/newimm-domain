// src/main/java/edu/gmu/cs321/Main.java
package edu.gmu.cs321;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Import URL

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            String loginFxmlFile = "LoginScreen.fxml";
            URL fxmlUrl = getClass().getResource(loginFxmlFile);

            if (fxmlUrl == null) {
                 System.err.println("FATAL ERROR: Cannot find FXML file: " + loginFxmlFile);
                 System.err.println("Check that the file exists in src/main/resources/edu/gmu/cs321/ and the build process includes resources.");
                 throw new IOException("Cannot find FXML file: " + loginFxmlFile);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Immigration Workflow - Login");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Add a shutdown hook to close the workflow connection
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Application shutting down, closing connections...");
                DataStore ds = DataStore.getInstance();
                if (ds != null) {
                    ds.cleanup();
                } else {
                    System.err.println("Warning: DataStore instance was null during shutdown cleanup.");
                }
            }));

        } catch (Exception e) {
            System.err.println("Error during application startup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        System.out.println("JavaFX Application stopping...");
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}