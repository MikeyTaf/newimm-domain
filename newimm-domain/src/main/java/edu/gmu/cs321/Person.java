
package edu.gmu.cs321;

import java.util.Date;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private Date dob;

    // Constructor
    // Inside Person.java
    public Person(String personID, String firstName, String lastName, Date dob) {
        if (personID == null || personID.trim().isEmpty()) {
            throw new IllegalArgumentException("Person ID cannot be null or empty.");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty.");
        }
        if (dob == null) {
            throw new IllegalArgumentException("Date of birth cannot be null.");
        }
        // Only assign if validation passes
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


    //Skeleton methods
    public boolean createPerson() {
        return false;
    }


    public boolean updatePerson() {
        return false;
    }


    public Person getPerson(String personID) {
        return null;
    }
}
