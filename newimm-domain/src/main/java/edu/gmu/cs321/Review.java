package edu.gmu.cs321;

import java.util.Date;

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
        return false;
    }

    public boolean updateReview() {
        return false;
    }

    public Review getReview(String reviewID) {
        return null;
    }
}
