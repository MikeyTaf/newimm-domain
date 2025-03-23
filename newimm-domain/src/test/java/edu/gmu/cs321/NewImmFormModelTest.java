package edu.gmu.cs321;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewImmFormModelTester {
    private NewImmFormModel form;
    private Immigrant immigrant;
    private List<Dependent> dependents;

    @BeforeEach
    void setUp() {
        immigrant = new Immigrant("I12345", "Alice", "Johnson", new Date(), "IM56789", "Canadian", "P987654321");
        dependents = new ArrayList<>();
        dependents.add(new Dependent("D12345", "Bob", "Johnson", new Date(), "D67890", "Child", "P876543210"));
        
        form = new NewImmFormModel(immigrant, dependents, "F001", new Date());
    }

    @Test
    void testCreateFormWithValidData() {
        assertNotNull(form);
        assertEquals("F001", form.getPetitionID());
        assertEquals(immigrant, form.getImmigrant());
        assertEquals(1, form.getDependents().size());
    }

    @Test
    void testCreateFormWithNullImmigrant() {
        NewImmFormModel invalidForm = new NewImmFormModel(null, dependents, "F002", new Date());
        assertNull(invalidForm.getImmigrant());
    }

    @Test
    void testAddDependentValid() {
        Dependent newDependent = new Dependent("D54321", "Charlie", "Johnson", new Date(), "D54321", "Spouse", "P765432109");
        form.addDependent(newDependent);

        assertEquals(2, form.getDependents().size());
        assertTrue(form.getDependents().contains(newDependent));
    }

    @Test
    void testAddDependentNull() {
        form.addDependent(null);
        assertEquals(1, form.getDependents().size()); 
    }

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
