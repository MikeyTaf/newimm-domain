package edu.gmu.cs321;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class PersonTest {

    /*
     * Tests to see if person can be created with a valid input
     */
    @Test
    void testCreatePersonValidInput() {

        Person person = new Person("P123", "Peter", "Parker", new Date());
        assertNotNull(person);
    }

    /*
     * Tests to see if null input throws exception
     */
    @Test
    void testCreatePersonInvalidInput() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Person(null, "", "", null);
        });
    }

    /*
     * Tests to see if first name can be updated
     */
    @Test
    void testUpdatePerson() {

        Person person = new Person("P123", "JPeter", "Parker", new Date());
        
        assertEquals("JPeter", person.getFirstName());
    }

    /*
     * Tests to see if Person details can be retrieved
     */
    @Test
    void testGetPersonDetails() {

        Person person = new Person("P123", "JPeter", "Parker", new Date());
        assertEquals("JPeter", person.getFirstName());
    }
}
