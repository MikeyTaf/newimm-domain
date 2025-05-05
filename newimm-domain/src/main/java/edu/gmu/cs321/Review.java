package edu.gmu.cs321;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Represents a review of an immigration petition.
 */
public class Review {
    private String reviewID;
    private String petitionID;
    private String reviewerID;
    private Date reviewDate;
    private String reviewComments;
    private boolean approved;

    /**
     * Constructs a Review object and automatically saves it to the data store.
     */
    public Review(String reviewID, String petitionID, String reviewerID, Date reviewDate, String reviewComments, boolean approved) {
        this.reviewID = reviewID;
        this.petitionID = petitionID;
        this.reviewerID = reviewerID;
        this.reviewDate = reviewDate;
        this.reviewComments = reviewComments;
        this.approved = approved;

        // Automatically save the review when it is created
        createReview();
    }

    // Getters and setters for all fields

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

    /**
     * Saves the review to the data store.
     * @return true if the save was successful, false otherwise
     */
    public boolean createReview() {
        try {
            DataStore.getInstance().saveReview(this);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating review: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the review in the data store.
     * In this implementation, it just re-saves it.
     * @return true if the update was successful
     */
    public boolean updateReview() {
        return createReview(); // Simulates update by re-saving
    }

    /**
     * Retrieves a review by its ID from the data store.
     * @param reviewID The ID of the review to fetch
     * @return the Review object, or null if not found
     */
    public Review getReview(String reviewID) {
        return DataStore.getInstance().getReview(reviewID);
    }

    /**
     * Generates a list of placeholder Review objects for pending petitions.
     * @return List of pending Review objects
     */
    public static List<Review> getPendingReviews() {
        List<Review> pendingReviews = new ArrayList<>();
        List<String> pendingIDs = DataStore.getInstance().getPendingPetitionIDs();

        System.out.println("Getting pending reviews. Found " + pendingIDs.size() + " pending IDs");

        for (String petitionID : pendingIDs) {
            System.out.println("Creating pending review for petition ID: " + petitionID);

            // Create a placeholder Review object with no reviewer or comments yet
            Review pendingReview = new Review(
                UUID.randomUUID().toString(), // Generate a temporary ID
                petitionID,
                null,    // Reviewer not yet assigned
                null,    // No review date
                null,    // No comments
                false    // Not approved yet
            );
            pendingReviews.add(pendingReview);
        }

        return pendingReviews;
    }
}
