package edu.gmu.cs321;

import com.cs321.Workflow;

/**
 * Simple test class to verify database connection and Workflow API functionality
 */
public class DatabaseConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("Starting database connection test...");
        
        Workflow workflow = null;
        
        try {
            // Step 1: Initialize the Workflow API (this will test the database connection)
            System.out.println("Attempting to connect to the database...");
            workflow = new com.cs321.Workflow();
            System.out.println("SUCCESS: Connected to the database successfully!");
            
            // Step 2: Try adding a test workflow item
            System.out.println("\nTesting workflow item creation...");
            int testFormId = 999;
            int result = workflow.AddWFItem(testFormId, "Review");
            
            if (result == 0) {
                System.out.println("SUCCESS: Added workflow item with ID " + testFormId);
            } else if (result == -1) {
                System.out.println("ERROR: Invalid NextStep value");
            } else if (result == -2) {
                System.out.println("ERROR: FormID already exists or is invalid");
            } else {
                System.out.println("ERROR: Unknown error code: " + result);
            }
            
            // Step 3: Try retrieving the workflow item
            System.out.println("\nTesting workflow item retrieval...");
            int retrievedId = workflow.GetNextWFItem("Review");
            
            if (retrievedId > 0) {
                System.out.println("SUCCESS: Retrieved workflow item with ID " + retrievedId);
            } else if (retrievedId == -1) {
                System.out.println("ERROR: Invalid NextStep value");
            } else if (retrievedId == -3) {
                System.out.println("ERROR: No items available with the specified status");
            } else {
                System.out.println("ERROR: Unknown error code: " + retrievedId);
            }
            
            System.out.println("\nDatabase connection test completed successfully!");
            
        } catch (Exception e) {
            System.out.println("\nERROR: Database connection test failed!");
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close the connection when done
            if (workflow != null) {
                try {
                    workflow.closeConnection();
                    System.out.println("Database connection closed.");
                } catch (Exception e) {
                    System.out.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }
}