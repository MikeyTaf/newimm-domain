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
        return false;
    }
}
