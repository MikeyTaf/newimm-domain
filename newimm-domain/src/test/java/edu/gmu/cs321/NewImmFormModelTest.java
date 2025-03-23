package edu.gmu.cs321;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
 * Test class for NewImmFormModel
 * Tests NewFormModel class for creation, validation, dependent addition
 */
class NewImmFormModelTester {
    private NewImmFormModel form;
    private Immigrant immigrant;
    private List<Dependent> dependents;

    /*
     * Method to initialize the form, immigrant, and dependents before each test
     */
    @BeforeEach
    void setUp() {
        immigrant = new Immigrant("I12345", "John", "Kratos", new Date(), "IM56789", "Spartan", "P987654321");
        dependents = new ArrayList<>();
        dependents.add(new Dependent("D12345", "Doom", "Guy", new Date(), "D67890", "Child", "P876543210"));
        
        form = new NewImmFormModel(immigrant, dependents, "F001", new Date());
    }

    /*
     * Test to verify that NewImmFormModel us created correctly
     */
    @Test
    void testCreateFormWithValidData() {
        assertNotNull(form);
        assertEquals("F001", form.getPetitionID());
        assertEquals(immigrant, form.getImmigrant());
        assertEquals(1, form.getDependents().size());
    }

    /*
     * Test for when form has a null immigrant
     */
    @Test
    void testCreateFormWithNullImmigrant() {
        NewImmFormModel invalidForm = new NewImmFormModel(null, dependents, "F002", new Date());
        assertNull(invalidForm.getImmigrant());
    }

    /*
     * Test to see if dependent can be added to the form correctly
     */
    @Test
    void testAddDependentValid() {
        Dependent newDependent = new Dependent("D54321", "Charlie", "Johnson", new Date(), "D54321", "Spouse", "P765432109");
        form.addDependent(newDependent);

        assertEquals(2, form.getDependents().size());
        assertTrue(form.getDependents().contains(newDependent));
    }

    /*
     * Test to see that adding a null dependent doesn't change the list
     */
    @Test
    void testAddDependentNull() {
        form.addDependent(null);
        assertEquals(1, form.getDependents().size()); 
    }

    /*
     * Test to see if submission date can be updated correctly
     */
    @Test
    void testUpdateSubmissionDate() {
        Date newDate = new Date();
        form.setSubmissionDate(newDate);
        assertEquals(newDate, form.getSubmissionDate());
    }

    @Test
    void testSubmitForm() {
        assertFalse(form.submitForm()); 
    }
}
