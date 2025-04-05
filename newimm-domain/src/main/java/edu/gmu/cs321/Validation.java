package edu.gmu.cs321;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Provides validation methods for immigrant petition forms
 */
public class Validation {
    // Passport number format: Letter followed by 7 digits
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("[A-Z]\\d{7}");
    
    // Simple name validation: at least 2 characters, only letters and hyphens
    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z\\-]{2,}");
    
    /**
     * Validates all required fields for an immigrant
     * 
     * @param immigrant The immigrant to validate
     * @return true if all validations pass, false otherwise
     */
    public boolean validateImmigrant(Immigrant immigrant) {
        if (immigrant == null) {
            return false;
        }
        
        // Check for required fields
        if (isEmpty(immigrant.getFirstName()) || isEmpty(immigrant.getLastName()) || 
            isEmpty(immigrant.getNationality()) || isEmpty(immigrant.getPassportNumber())) {
            return false;
        }
        
        // Validate name format
        if (!NAME_PATTERN.matcher(immigrant.getFirstName()).matches() || 
            !NAME_PATTERN.matcher(immigrant.getLastName()).matches()) {
            return false;
        }
        
        // Validate passport format
        if (!PASSPORT_PATTERN.matcher(immigrant.getPassportNumber()).matches()) {
            return false;
        }
        
        // Validate date of birth (must be in the past)
        if (immigrant.getDob() == null || immigrant.getDob().after(new Date())) {
            return false;
        }
        
        return true;
    }

    /**
     * Validates all required fields for a dependent
     * 
     * @param dependent The dependent to validate
     * @return true if all validations pass, false otherwise
     */
    public boolean validateDependent(Dependent dependent) {
        if (dependent == null) {
            return false;
        }
        
        // Check for required fields
        if (isEmpty(dependent.getFirstName()) || isEmpty(dependent.getLastName()) || 
            isEmpty(dependent.getRelationshipType()) || isEmpty(dependent.getPassportNumber())) {
            return false;
        }
        
        // Validate name format
        if (!NAME_PATTERN.matcher(dependent.getFirstName()).matches() || 
            !NAME_PATTERN.matcher(dependent.getLastName()).matches()) {
            return false;
        }
        
        // Validate passport format
        if (!PASSPORT_PATTERN.matcher(dependent.getPassportNumber()).matches()) {
            return false;
        }
        
        // Validate date of birth (must be in the past)
        if (dependent.getDob() == null || dependent.getDob().after(new Date())) {
            return false;
        }
        
        // Validate relationship type
        if (!isValidRelationship(dependent.getRelationshipType())) {
            return false;
        }
        
        return true;
    }

    /**
     * Validates the entire petition form
     * 
     * @param form The form model to validate
     * @return true if all validations pass, false otherwise
     */
    public boolean validateForm(NewImmFormModel form) {
        if (form == null || form.getImmigrant() == null) {
            return false;
        }
        
        // Validate immigrant
        if (!validateImmigrant(form.getImmigrant())) {
            return false;
        }
        
        // Validate dependents if any
        if (form.getDependents() != null) {
            for (Dependent dependent : form.getDependents()) {
                if (!validateDependent(dependent)) {
                    return false;
                }
            }
        }
        
        // Validate petition ID
        if (isEmpty(form.getPetitionID())) {
            return false;
        }
        
        // Validate submission date
        if (form.getSubmissionDate() == null) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets specific validation error messages for an immigrant
     * 
     * @param immigrant The immigrant to validate
     * @return An array of error messages, empty if no errors
     */
    public String[] getImmigrantErrors(Immigrant immigrant) {
        java.util.List<String> errors = new java.util.ArrayList<>();
        
        if (immigrant == null) {
            errors.add("Immigrant information is missing");
            return errors.toArray(new String[0]);
        }
        
        // Check first name
        if (isEmpty(immigrant.getFirstName())) {
            errors.add("First name is required");
        } else if (!NAME_PATTERN.matcher(immigrant.getFirstName()).matches()) {
            errors.add("First name can only contain letters and hyphens");
        }
        
        // Check last name
        if (isEmpty(immigrant.getLastName())) {
            errors.add("Last name is required");
        } else if (!NAME_PATTERN.matcher(immigrant.getLastName()).matches()) {
            errors.add("Last name can only contain letters and hyphens");
        }
        
        // Check nationality
        if (isEmpty(immigrant.getNationality())) {
            errors.add("Nationality is required");
        }
        
        // Check passport
        if (isEmpty(immigrant.getPassportNumber())) {
            errors.add("Passport number is required");
        } else if (!PASSPORT_PATTERN.matcher(immigrant.getPassportNumber()).matches()) {
            errors.add("Passport number must be a letter followed by 7 digits");
        }
        
        // Check date of birth
        if (immigrant.getDob() == null) {
            errors.add("Date of birth is required");
        } else if (immigrant.getDob().after(new Date())) {
            errors.add("Date of birth cannot be in the future");
        }
        
        return errors.toArray(new String[0]);
    }
    
    /**
     * Gets specific validation error messages for a dependent
     * 
     * @param dependent The dependent to validate
     * @return An array of error messages, empty if no errors
     */
    public String[] getDependentErrors(Dependent dependent) {
        java.util.List<String> errors = new java.util.ArrayList<>();
        
        if (dependent == null) {
            errors.add("Dependent information is missing");
            return errors.toArray(new String[0]);
        }
        
        // Check first name
        if (isEmpty(dependent.getFirstName())) {
            errors.add("First name is required");
        } else if (!NAME_PATTERN.matcher(dependent.getFirstName()).matches()) {
            errors.add("First name can only contain letters and hyphens");
        }
        
        // Check last name
        if (isEmpty(dependent.getLastName())) {
            errors.add("Last name is required");
        } else if (!NAME_PATTERN.matcher(dependent.getLastName()).matches()) {
            errors.add("Last name can only contain letters and hyphens");
        }
        
        // Check relationship
        if (isEmpty(dependent.getRelationshipType())) {
            errors.add("Relationship type is required");
        } else if (!isValidRelationship(dependent.getRelationshipType())) {
            errors.add("Invalid relationship type");
        }
        
        // Check passport
        if (isEmpty(dependent.getPassportNumber())) {
            errors.add("Passport number is required");
        } else if (!PASSPORT_PATTERN.matcher(dependent.getPassportNumber()).matches()) {
            errors.add("Passport number must be a letter followed by 7 digits");
        }
        
        // Check date of birth
        if (dependent.getDob() == null) {
            errors.add("Date of birth is required");
        } else if (dependent.getDob().after(new Date())) {
            errors.add("Date of birth cannot be in the future");
        }
        
        return errors.toArray(new String[0]);
    }
    
    // Helper methods
    
    /**
     * Checks if a string is empty or null
     */
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validates if the relationship type is valid
     */
    private boolean isValidRelationship(String relationship) {
        if (isEmpty(relationship)) {
            return false;
        }
        
        // List of valid relationship types
        String[] validRelationships = {"SPOUSE", "CHILD", "PARENT", "SIBLING"};
        for (String valid : validRelationships) {
            if (valid.equalsIgnoreCase(relationship)) {
                return true;
            }
        }
        return false;
    }
}