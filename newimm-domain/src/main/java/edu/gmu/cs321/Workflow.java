package edu.gmu.cs321;



/**
 * Wrapper class for the Workflow API
 */
public class Workflow {
    private String petitionID;
    private String status;
    private static com.cs321.Workflow workflowAPI;

    
    public Workflow(String petitionID, String status) {
        this.petitionID = petitionID;
        this.status = status;
        
        
        if (workflowAPI == null) {
            try {
                workflowAPI = new com.cs321.Workflow();
            } catch (Exception e) {
                System.err.println("Error initializing Workflow API: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        
        try {
            if (workflowAPI != null) {
                
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

    
    public String getWorkflowID() {
        return petitionID; 
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

    
    public void updateStatus(String newStatus) {
        System.out.println("Workflow: Updating status for petition " + petitionID +
                " from " + this.status + " to " + newStatus);
        this.status = newStatus;
        
        
        try {
            if (workflowAPI != null) {
                
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
    
    
    private String convertStatusToNextStep(String status) {
        switch (status) {
            case "SUBMITTED":
                return "Review";
            case "APPROVED":
            case "REJECTED":
                return "Approve";
            default:
                return "Review"; 
        }
    }
    
    
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
                
                return "PET-" + formID;
            }
        } catch (Exception e) {
            System.err.println("Error getting next workflow item: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    private static String convertStatusToNextStepStatic(String status) {
        switch (status) {
            case "SUBMITTED":
                return "Review";
            case "APPROVED":
            case "REJECTED":
                return "Approve";
            default:
                return "Review"; 
        }
    }
    
    
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