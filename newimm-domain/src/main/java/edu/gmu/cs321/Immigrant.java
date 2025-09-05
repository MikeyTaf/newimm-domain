package edu.gmu.cs321;

import java.util.Date;

public class Immigrant extends Person {
    private String immigrantID;
    private String nationality;
    private String passportNumber;

    // Constructor
    public Immigrant(String personID, String firstName, String lastName, Date dob, String immigrantID, String nationality, String passportNumber) {
        
        super(personID, firstName, lastName, dob);  
        
        if(immigrantID == null || immigrantID.trim().isEmpty()) {
            throw new IllegalArgumentException("Immigrant ID cannot be null");
        }

        if(nationality == null || nationality.trim().isEmpty()) {
            throw new IllegalArgumentException("Nationaloty cannot be null");
        }

        if(passportNumber == null || passportNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Passprt number cannot be null");
        }

        this.immigrantID = immigrantID;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
    }

    // Getters and setters
    public String getImmigrantID() {
        return immigrantID;
    }

    public void setImmigrantID(String immigrantID) {
        this.immigrantID = immigrantID;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public boolean createImmigrant() {
        return false;
    }

    public boolean updateImmigrant() {
        return false;
    }

    public Immigrant getImmigrant(String immigrantID) {

        return null;
    }
}
