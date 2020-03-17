package net.digitallogic.UserLogin.persistence.repository;


import net.digitallogic.UserLogin.persistence.model.AuthorityEntity;
import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.shared.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private EntityManager entityManager;

    Utils utils = new Utils();
    Random random = new Random();
    Map<String, UserEntity> userEntities;
    Map<String, RoleEntity> roles;

    @BeforeEach
    @Transactional
    void beforeEach() {
        System.out.println("Setup for testing...");
        // Create Authorities
        Map<String, AuthorityEntity> authorities =
                Arrays.asList("ADMIN_AUTHORITY", "READ_AUTHORITY", "DELETE_AUTHORITY").stream()
                    .map(authority -> authorityRepository.save(new AuthorityEntity(authority)))
                    .collect(Collectors.toMap(AuthorityEntity::getAuthority, Function.identity()));

        // Create Roles
        roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN").stream()
                .map(roleName -> roleRepository.save(new RoleEntity(roleName)))
                .collect(Collectors.toMap(RoleEntity::getName, Function.identity()));


        roles.get("ROLE_USER").addAuthority(authorities.get("READ_AUTHORITY"));

        roles.get("ROLE_ADMIN")
                .addAuthority(authorities.get("ADMIN_AUTHORITY"))
                .addAuthority(authorities.get("DELETE_AUTHORITY"));

        // Create Users
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity("Joe", "Thomas", "jThomas@testing.com"));
        users.add(new UserEntity("Bob", "Barker", "bBarker@testing.com"));
        users.add(new UserEntity("Sarah", "Conner", "sConner@testing.com"));
        users.add(new UserEntity("Joe", "Dirt", "jDirt@gmail.com"));
        users.add(new UserEntity("Admin", "Administrator", "admin@testing.com"));
        userEntities = users.stream()
                .map(user -> {
                    user.setEmailVerified(false);
                    user.setEncryptedPassword("encryptedPassword");
                    user.setUserId(utils.generateId());
                    user.addRole(roles.get("ROLE_USER"));

                    return userRepository.save(user);
                })
                .collect(Collectors.toMap(UserEntity::getEmail, Function.identity()));

        userRepository.save(
            userEntities.get("admin@testing.com")
                    .addRole(roles.get("ROLE_ADMIN"))
        );

        // flush to database
        entityManager.flush();
        // update local entities
        userEntities.values().forEach(user -> entityManager.refresh(user));

        System.out.println("Setup completed.");
    }

    @Test
    void verifyRolesInDatabaseTest() {
        assertEquals(roles.size(), roleRepository.count());
        assertEquals(2, roleRepository.count());
    }

    @Test
    void findUserByIdWithRolesTest() {
        UserEntity user = userRepository.findByUserIdWithRoles(userEntities.get("admin@testing.com").getUserId());
        assertNotNull(user);
        assertEquals(userEntities.get("admin@testing.com").getRoles().size(), user.getRoles().size());
    }

    @Test
    void findUserByIdWithAuthoritiesTest() {
        UserEntity user = userRepository.findByUserIdWithAuthorities(userEntities.get("admin@testing.com").getUserId());
        assertNotNull(user);
        user.getRoles().forEach(role -> {
            assertNotNull(role);
            role.getAuthorities().forEach(authorityEntity -> {
               assertNotNull(authorityEntity);
            });
        });
    }

    @Test
    void findUserByEmailWithAuthoritiesTest() {
        UserEntity user = userRepository.findByEmailWithAuthorities("admin@testing.com");
        assertNotNull(user);
    }

    @Test
    @Transactional
    void getUserTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserEntity> userPage = userRepository.findAll(pageRequest);
        assertEquals(userPage.getContent().size(), userEntities.size());
        userPage.getContent().stream().forEach(user -> {
            assertNotNull(userEntities.get(user.getEmail()));
            assertEquals(userEntities.get(user.getEmail()).getRoles().size(), user.getRoles().size());
        });
    }

    @Test
    @Transactional
    void findUserByIdTest() {
        userEntities.values()
            .forEach(user -> {
                System.out.println("Get User: " + user.getEmail());
                UserEntity result = userRepository.findByUserId(user.getUserId());
                assertNotNull(result);

                System.out.println("Getting user roles");
                result.getRoles().forEach(role -> {
                    assertTrue(user.getRoles().contains(role));
                });
            });
    }

    @Test
    @Transactional
    void findByEmailTest() {
        userEntities.values().forEach(userEntity -> {
            assertNotNull(
                    userRepository.findByEmail(userEntity.getEmail())
            );
        });
    }

    @Test
    @Transactional
    void updateUserVerificationStatusTest() {

        userEntities.values().forEach(user -> {
            userRepository.setUserVerificationStatusToVerified(user.getUserId());
        });
        userEntities.values().forEach(user -> {

            // sync catch with database
            entityManager.refresh(user);

            assertTrue(
                userRepository.findByUserId(user.getUserId())
                        .isEmailVerified()
            );
        });
    }
}
