package net.digitallogic.UserLogin.persistence.model;

import net.digitallogic.UserLogin.security.Authority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AuthorityEntityTest {

    @Test
    void equalityTest() {
        AuthorityEntity a = new AuthorityEntity(Authority.ADMIN_USER.auth);
        AuthorityEntity b = new AuthorityEntity(Authority.ADMIN_USER.auth);
        assertEquals(a,b);
    }
}
