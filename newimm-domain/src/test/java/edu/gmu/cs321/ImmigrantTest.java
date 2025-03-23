package edu.gmu.cs321;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class ImmigrantTest {

    @Test
    void testCreateImmigrant_ValidInput() {
        // Update Code in Iteration 4
        Immigrant immigrant = new Immigrant("P123", "Alice", "Smith", new Date(), "I456", "Canadian", "A12345");
        assertNotNull(immigrant);
    }

    @Test
    void testCreateImmigrant_InvalidInput() {
        // Update Code in Iteration 4
        assertThrows(IllegalArgumentException.class, () -> {
            new Immigrant(null, "", "", null, null, "", "");
        });
    }

    @Test
    void testUpdateImmigrant() {
       // Update Code in Iteration 4
        Immigrant immigrant = new Immigrant("P123", "Alice", "Smith", new Date(), "I456", "Canadian", "A12345");
        immigrant.setNationality("American");
        assertEquals("American", immigrant.getNationality());
    }

    @Test
    void testGetImmigrantDetails() {
       // Update Code in Iteration 4
        Immigrant immigrant = new Immigrant("P123", "Alice", "Smith", new Date(), "I456", "Canadian", "A12345");
        assertEquals("Alice", immigrant.getFirstName());
    }
}
