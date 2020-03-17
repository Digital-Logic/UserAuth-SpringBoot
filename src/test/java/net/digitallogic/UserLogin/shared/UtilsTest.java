package net.digitallogic.UserLogin.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    void testGenerateId() {
        Utils utils = new Utils();
        String id = utils.generateId();

        assertNotNull(id);
        assertTrue(id.length() == 30);
        assertFalse(id.equals(utils.generateId()));
    }
}
