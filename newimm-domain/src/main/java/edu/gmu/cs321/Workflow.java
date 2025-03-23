package edu.gmu.cs321;

public class Workflow {
    private String workflowID;
    private String status;

    //Constrcutor
    public Workflow(String workflowID, String status) {
        this.workflowID = workflowID;
        this.status = status;
    }

    //Getter and Setter
    public String getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(String workflowID) {
        this.workflowID = workflowID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        
    }
}
