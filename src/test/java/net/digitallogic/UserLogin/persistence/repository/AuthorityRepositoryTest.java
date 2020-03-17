package net.digitallogic.UserLogin.persistence.repository;

import net.digitallogic.UserLogin.persistence.model.AuthorityEntity;
import net.digitallogic.UserLogin.persistence.model.AuthorityEntityTest;
import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import net.digitallogic.UserLogin.security.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AuthorityRepositoryTest {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    EntityManager entityManager;

    Collection<AuthorityEntityTest> authorityEntities = new HashSet<>();
    RoleEntity roleEntity;

    @BeforeEach
    void beforeEach() {
        // build all authorities in database
        Stream.of(Authority.values())
                .forEach(authority -> {
                    authorityRepository.save(new AuthorityEntity(authority.auth));
                });
    }

    @Test
    void findByAuthorityTest() {
        Stream.of(Authority.values())
                .forEach(authority -> {
                   AuthorityEntity curAuthority = authorityRepository.findByAuthority(authority.auth);
                   assertEquals(curAuthority.getAuthority(), authority.auth);
                });
    }
}
