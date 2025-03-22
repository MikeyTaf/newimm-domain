package edu.gmu.cs321;

import java.util.Date;

public class Approval {
    private String approvalID;
    private String petitionID;
    private String approverID;
    private String decision;
    private String decisionReason;
    private Date decisionDate;

    public Approval(String approvalID, String petitionID, String approverID, String decision, String decisionReason, Date decisionDate) {
        this.approvalID = approvalID;
        this.petitionID = petitionID;
        this.approverID = approverID;
        this.decision = decision;
        this.decisionReason = decisionReason;
        this.decisionDate = decisionDate;
    }

    public String getApprovalID() {
        return approvalID;
    }

    public void setApprovalID(String approvalID) {
        this.approvalID = approvalID;
    }

    public String getPetitionID() {
        return petitionID;
    }

    public void setPetitionID(String petitionID) {
        this.petitionID = petitionID;
    }

    public String getApproverID() {
        return approverID;
    }

    public void setApproverID(String approverID) {
        this.approverID = approverID;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getDecisionReason() {
        return decisionReason;
    }

    public void setDecisionReason(String decisionReason) {
        this.decisionReason = decisionReason;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public boolean createApproval() {
        return false;
    }

    public boolean updateApproval() {
        return false;
    }

    public Approval getApproval(String approvalID) {
        return null;
    }
}
