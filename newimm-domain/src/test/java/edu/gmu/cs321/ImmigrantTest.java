package edu.gmu.cs321;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ImmigrantTest {

    @Test
    public void testcheckAnum() {

        Immigration testImm = new Immigration();
        boolean testRest = testImm.checkANum("1");
        assertTrue(testRest);
    }
    
}
