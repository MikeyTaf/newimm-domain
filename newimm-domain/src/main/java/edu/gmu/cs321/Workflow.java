package edu.gmu.cs321;

public class Workflow {
    private String workflowID;
    private String petitionID; // The petition this workflow is tracking
    private String status;     // Current status (SUBMITTED, APPROVED, REJECTED)

    // Constructor
    public Workflow(String petitionID, String status) {
        this.workflowID = petitionID; // Using petitionID as workflowID for simplicity
        this.petitionID = petitionID;
        this.status = status;
    }

    // Getters and Setters
    public String getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(String workflowID) {
        this.workflowID = workflowID;
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
    }
    
    // Get a readable description of the current status
    public String getStatusDescription() {
        switch(status) {
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
}