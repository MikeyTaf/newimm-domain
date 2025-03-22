package edu.gmu.cs321;

import java.util.Date;

public class Dependent extends Person {
    private String dependentID;
    private String relationshipType;
    private String passportNumber;

    public Dependent(String personID, String firstName, String lastName, Date dob, String dependentID, String relationshipType, String passportNumber) {
        super(personID, firstName, lastName, dob);
        this.dependentID = dependentID;
        this.relationshipType = relationshipType;
        this.passportNumber = passportNumber;
    }

    public String getDependentID() {
        return dependentID;
    }

    public void setDependentID(String dependentID) {
        this.dependentID = dependentID;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public boolean createDependent() {
        return false;
    }

    public boolean updateDependent() {
        return false;
    }

    public Dependent getDependent(String dependentID) {
        return null;
    }
}
