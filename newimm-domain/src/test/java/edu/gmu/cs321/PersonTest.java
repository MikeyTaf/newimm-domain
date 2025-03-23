package edu.gmu.cs321;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class PersonTest {

    @Test
    void testCreatePerson_ValidInput() {

        Person person = new Person("P123", "John", "Doe", new Date());
        assertNotNull(person);
    }

    @Test
    void testCreatePerson_InvalidInput() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Person(null, "", "", null);
        });
    }

    @Test
    void testUpdatePerson() {

        Person person = new Person("P123", "John", "Doe", new Date());
        person.setFirstName("Jane");
        assertEquals("Jane", person.getFirstName());
    }

    @Test
    void testGetPersonDetails() {

        Person person = new Person("P123", "John", "Doe", new Date());
        assertEquals("John", person.getFirstName());
    }
}
