package edu.gmu.cs321;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewImmFormModel {
    private Immigrant immigrant;
    private List<Dependent> dependents;
    private String petitionID;
    private Date submissionDate;

    public NewImmFormModel(Immigrant immigrant, List<Dependent> dependents, String petitionID, Date submissionDate) {
        this.immigrant = immigrant;
        this.dependents = dependents;
        this.petitionID = petitionID;
        this.submissionDate = submissionDate;
    }

    public Immigrant getImmigrant() {
        return immigrant;
    }

    public void setImmigrant(Immigrant immigrant) {
        this.immigrant = immigrant;
    }

    public List<Dependent> getDependents() {
        return dependents;
    }

    public void setDependents(List<Dependent> dependents) {
        this.dependents = dependents;
    }

    public String getPetitionID() {
        return petitionID;
    }

    public void setPetitionID(String petitionID) {
        this.petitionID = petitionID;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public void addDependent(Dependent dependent) {
        if (dependents == null) {
            dependents = new ArrayList<>();
        }
        dependents.add(dependent);
    }

    

    public boolean submitForm() {
        try {
            System.out.println("Submitting petition with ID: " + petitionID);
            
            // Print immigrant info for debugging
            System.out.println("Immigrant: " + immigrant.getFirstName() + " " + immigrant.getLastName());
            System.out.println("Dependents: " + dependents.size());
            
            // Save to DataStore
            DataStore.getInstance().savePetition(this);
            
            // Verify it was saved
            System.out.println("Checking if petition was saved...");
            NewImmFormModel savedPetition = DataStore.getInstance().getPetition(petitionID);
            if (savedPetition != null) {
                System.out.println("Petition saved successfully!");
            } else {
                System.out.println("ERROR: Petition was not saved!");
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error submitting form: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Also modify the static getPetition method:
    public static NewImmFormModel getPetition(String petitionID) {
        return DataStore.getInstance().getPetition(petitionID);
    }
}
