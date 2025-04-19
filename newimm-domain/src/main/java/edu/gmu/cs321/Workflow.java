package edu.gmu.cs321;

// Import the API using its fully qualified name when needed to avoid conflicts

/**
 * Wrapper class for the Workflow API
 */
public class Workflow {
    private String petitionID;
    private String status;
    private static com.cs321.Workflow workflowAPI;

    // Constructor
    public Workflow(String petitionID, String status) {
        this.petitionID = petitionID;
        this.status = status;
        
        // Initialize the API if not already done
        if (workflowAPI == null) {
            try {
                workflowAPI = new com.cs321.Workflow();
            } catch (Exception e) {
                System.err.println("Error initializing Workflow API: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Add the workflow item to the API
        try {
            if (workflowAPI != null) {
                // Convert petitionID to integer for the API
                int formID = Integer.parseInt(petitionID.replaceAll("[^0-9]", ""));
                String nextStep = convertStatusToNextStep(status);
                
                int result = workflowAPI.AddWFItem(formID, nextStep);
                if (result < 0) {
                    System.err.println("Error adding workflow item: " + result);
                }
            }
        } catch (Exception e) {
            System.err.println("Error adding workflow item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters and Setters (unchanged)
    public String getWorkflowID() {
        return petitionID; // Using petitionID as workflowID for simplicity
    }

    public void setWorkflowID(String workflowID) {
        this.petitionID = workflowID;
    }

    public String getPetitionID() {
        return petitionID;
    }

    public void setPetitionID(String petitionID) {
        this.petitionID = petitionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Update the status of this workflow
    public void updateStatus(String newStatus) {
        System.out.println("Workflow: Updating status for petition " + petitionID +
                " from " + this.status + " to " + newStatus);
        this.status = newStatus;
        
        // Update in the API
        try {
            if (workflowAPI != null) {
                // Convert petitionID to integer for the API
                int formID = Integer.parseInt(petitionID.replaceAll("[^0-9]", ""));
                String nextStep = convertStatusToNextStep(newStatus);
                
                int result = workflowAPI.AddWFItem(formID, nextStep);
                if (result < 0) {
                    System.err.println("Error updating workflow item: " + result);
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating workflow item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get a readable description of the current status
    public String getStatusDescription() {
        switch (status) {
            case "SUBMITTED":
                return "Submitted - Awaiting Review";
            case "APPROVED":
                return "Approved";
            case "REJECTED":
                return "Rejected";
            default:
                return "Unknown Status";
        }
    }
    
    // Map internal status to API NextStep (instance method)
    private String convertStatusToNextStep(String status) {
        switch (status) {
            case "SUBMITTED":
                return "Review";
            case "APPROVED":
            case "REJECTED":
                return "Approve";
            default:
                return "Review"; // Default to Review for unknown statuses
        }
    }
    
    // Static method to get the next petition ID for a given status
    public static String getNextPetitionID(String status) {
        if (workflowAPI == null) {
            try {
                workflowAPI = new com.cs321.Workflow();
            } catch (Exception e) {
                System.err.println("Error initializing Workflow API: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        
        try {
            String nextStep = convertStatusToNextStepStatic(status);
            int formID = workflowAPI.GetNextWFItem(nextStep);
            
            if (formID > 0) {
                // Format the form ID as a petition ID
                return "PET-" + formID;
            }
        } catch (Exception e) {
            System.err.println("Error getting next workflow item: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Static helper method for mapping status to NextStep (static version)
    private static String convertStatusToNextStepStatic(String status) {
        switch (status) {
            case "SUBMITTED":
                return "Review";
            case "APPROVED":
            case "REJECTED":
                return "Approve";
            default:
                return "Review"; // Default to Review for unknown statuses
        }
    }
    
    // Close the API connection when the application exits
    public static void closeConnection() {
        if (workflowAPI != null) {
            try {
                workflowAPI.closeConnection();
                workflowAPI = null;
            } catch (Exception e) {
                System.err.println("Error closing workflow connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}