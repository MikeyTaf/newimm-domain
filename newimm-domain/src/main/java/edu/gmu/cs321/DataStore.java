package edu.gmu.cs321;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    // Singleton instance
    private static DataStore instance = new DataStore();
    
    // Data storage
    private Map<String, NewImmFormModel> petitions = new HashMap<>();
    private Map<String, Review> reviews = new HashMap<>();
    private Map<String, Workflow> workflows = new HashMap<>();
    
    private DataStore() {
        // Private constructor for singleton
    }
    
    public static DataStore getInstance() {
        return instance;
    }
    
    // Petition methods
    public void savePetition(NewImmFormModel petition) {
        System.out.println("DataStore: Saving petition with ID " + petition.getPetitionID());
        petitions.put(petition.getPetitionID(), petition);
        
        // Create a workflow entry
        Workflow workflow = new Workflow(petition.getPetitionID(), "SUBMITTED");
        workflows.put(petition.getPetitionID(), workflow);
        System.out.println("DataStore: Created workflow with status " + workflow.getStatus());
    }
    
    public NewImmFormModel getPetition(String petitionID) {
        return petitions.get(petitionID);
    }
    
    public List<NewImmFormModel> getAllPetitions() {
        return new ArrayList<>(petitions.values());
    }
    
    // Review methods
    public void saveReview(Review review) {
        System.out.println("DataStore: Saving review for petition " + review.getPetitionID());
        reviews.put(review.getReviewID(), review);
        
        // Update workflow status
        Workflow workflow = workflows.get(review.getPetitionID());
        if (workflow != null) {
            workflow.setStatus(review.isApproved() ? "APPROVED" : "REJECTED");
            // Call updateStatus to ensure the status is updated in the Workflow API
            workflow.updateStatus(review.isApproved() ? "APPROVED" : "REJECTED");
            System.out.println("DataStore: Updated workflow status to " + workflow.getStatus());
        } else {
            System.out.println("DataStore: Warning - No workflow found for petition " + review.getPetitionID());
        }
    }
    
    public Review getReview(String reviewID) {
        return reviews.get(reviewID);
    }
    
    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews.values());
    }
    
    // Workflow methods
    public String getPetitionStatus(String petitionID) {
        Workflow workflow = workflows.get(petitionID);
        return workflow != null ? workflow.getStatus() : "UNKNOWN";
    }
    
    public List<String> getPendingPetitionIDs() {
        List<String> pendingIDs = new ArrayList<>();
        System.out.println("DataStore: Checking for pending petitions");
        
        // First check our local workflows
        for (Map.Entry<String, Workflow> entry : workflows.entrySet()) {
            System.out.println("DataStore: Workflow " + entry.getKey() + " has status " + entry.getValue().getStatus());
            if ("SUBMITTED".equals(entry.getValue().getStatus())) {
                pendingIDs.add(entry.getKey());
                System.out.println("DataStore: Added pending petition ID " + entry.getKey());
            }
        }
        
        // Then check the Workflow API for any additional pending items
        String nextPetitionID = Workflow.getNextPetitionID("SUBMITTED");
        while (nextPetitionID != null && !pendingIDs.contains(nextPetitionID)) {
            // If this is a new petition ID we don't have locally, add it
            pendingIDs.add(nextPetitionID);
            System.out.println("DataStore: Added pending petition ID from API: " + nextPetitionID);
            
            // Try to get another one
            nextPetitionID = Workflow.getNextPetitionID("SUBMITTED");
        }
        
        return pendingIDs;
    }
    
    // Debug method to print contents
    public void printContents() {
        System.out.println("=== DataStore Contents ===");
        System.out.println("Total petitions: " + petitions.size());
        for (NewImmFormModel petition : petitions.values()) {
            System.out.println("Petition ID: " + petition.getPetitionID());
            System.out.println("  Immigrant: " + petition.getImmigrant().getFirstName() + 
                              " " + petition.getImmigrant().getLastName());
            System.out.println("  Submission Date: " + petition.getSubmissionDate());
            System.out.println("  Dependents: " + petition.getDependents().size());
        }
        
        System.out.println("\nTotal workflows: " + workflows.size());
        for (Workflow workflow : workflows.values()) {
            System.out.println("Workflow for petition: " + workflow.getPetitionID());
            System.out.println("  Status: " + workflow.getStatus());
        }
        
        System.out.println("\nTotal reviews: " + reviews.size());
        for (Review review : reviews.values()) {
            System.out.println("Review ID: " + review.getReviewID());
            System.out.println("  Petition ID: " + review.getPetitionID());
            System.out.println("  Approved: " + review.isApproved());
        }
        System.out.println("========================");
    }
    
    // Clean up resources when the application exits
    public void cleanup() {
        Workflow.closeConnection();
    }
}