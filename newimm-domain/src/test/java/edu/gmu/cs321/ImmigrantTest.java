package edu.gmu.cs321;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class ImmigrantTest {

    /*
     * Checks if Immigrant can be created with a valid input
     */
    @Test
    void testCreateImmigrantValidInput() {
        // Update Code in Iteration 4
        Immigrant immigrant = new Immigrant("P123", "Ben", "Tennyson", new Date(), "I456", "American", "A12345");
        assertNotNull(immigrant);
    }

    /*
     * Checks if creating immigrant with invalid input has an error
     */
    @Test
    void testCreateImmigrantInvalidInput() {
        // Update Code in Iteration 4
        assertThrows(IllegalArgumentException.class, () -> {
            new Immigrant(null, "", "", null, null, "", "");
        });
    }

    /*
     * Tests to see if immigrant nationality can be updated
     */
    @Test
    void testUpdateImmigrant() {
       // Update Code in Iteration 4
        Immigrant immigrant = new Immigrant("P123", "Ben", "Tennyson", new Date(), "I456", "Canadian", "A12345");
        immigrant.setNationality("Martian");
        assertEquals("Martian", immigrant.getNationality());
    }

    /*
     * Test to see if immigrant details can be retrieved
     */
    @Test
    void testGetImmigrantDetails() {
       // Update Code in Iteration 4
        Immigrant immigrant = new Immigrant("P123", "Alice", "Smith", new Date(), "I456", "Canadian", "A12345");
        assertEquals("Alice", immigrant.getFirstName());
    }
}
