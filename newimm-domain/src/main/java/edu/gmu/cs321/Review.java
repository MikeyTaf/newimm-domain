package edu.gmu.cs321;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Review {
    private String reviewID;
    private String petitionID;
    private String reviewerID;
    private Date reviewDate;
    private String reviewComments;
    private boolean approved;

    public Review(String reviewID, String petitionID, String reviewerID, Date reviewDate, String reviewComments, boolean approved) {
        this.reviewID = reviewID;
        this.petitionID = petitionID;
        this.reviewerID = reviewerID;
        this.reviewDate = reviewDate;
        this.reviewComments = reviewComments;
        this.approved = approved;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getPetitionID() {
        return petitionID;
    }

    public void setPetitionID(String petitionID) {
        this.petitionID = petitionID;
    }

    public String getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean createReview() {
        try {
            // Save to DataStore instead of database
            DataStore.getInstance().saveReview(this);
            
            // Update workflow status
            Workflow workflow = new Workflow(petitionID, approved ? "APPROVED" : "REJECTED");
            workflow.updateStatus(approved ? "APPROVED" : "REJECTED");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error creating review: " + e.getMessage());
            return false;
        }
    }

    public boolean updateReview() {
        // In a real app, this would update the database
        // For now, just update the in-memory store
        return createReview();
    }

    public Review getReview(String reviewID) {
        return DataStore.getInstance().getReview(reviewID);
    }
    
    public static List<Review> getPendingReviews() {
        List<Review> pendingReviews = new ArrayList<>();
        List<String> pendingIDs = DataStore.getInstance().getPendingPetitionIDs();
        
        System.out.println("Getting pending reviews. Found " + pendingIDs.size() + " pending IDs");
        
        for (String petitionID : pendingIDs) {
            System.out.println("Creating pending review for petition ID: " + petitionID);
            
            // Create placeholder Review objects for pending petitions
            Review pendingReview = new Review(
                UUID.randomUUID().toString(), // Generate a temp ID
                petitionID,
                null, // No reviewer assigned yet
                null, // No review date yet
                null, // No comments yet
                false // Not approved yet
            );
            pendingReviews.add(pendingReview);
        }
        
        return pendingReviews;
    }
}