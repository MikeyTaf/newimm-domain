
package edu.gmu.cs321;

import java.util.Date;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private Date dob;

    // Constructor
    public Person(String personID, String firstName, String lastName, Date dob) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    // Getters and setters of values from the form
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
