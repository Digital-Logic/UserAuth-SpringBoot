package net.digitallogic.UserLogin.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.digitallogic.UserLogin.persistence.model.RoleEntity;
import net.digitallogic.UserLogin.persistence.model.UserEntity;
import net.digitallogic.UserLogin.persistence.repository.RoleRepository;
import net.digitallogic.UserLogin.persistence.repository.UserRepository;
import net.digitallogic.UserLogin.security.SecurityConstants;
import net.digitallogic.UserLogin.shared.Utils;
import net.digitallogic.UserLogin.web.controller.request.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${token.secret}")
    private String tokenSecret;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Utils utils;

    @Autowired
    private EntityManager entityManager;


    @Test
    @Transactional
    void CreateUserTest() throws Exception {
        CreateUserRequest requestObj = new CreateUserRequest();
        requestObj.setFirstName("Joe");
        requestObj.setLastName("Dirt");
        requestObj.setEmail("joe@dirt.com");
        requestObj.setPassword("asdfqwer");

        mockMvc.perform(post("/api/users")
                .content(new ObjectMapper().writeValueAsString(requestObj))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        // verify entity added to database
        UserEntity user = userRepository.findByEmail(requestObj.getEmail());
        assertNotNull(user);
        assertFalse(user.isEmailVerified());
    }

    @Test
    @Transactional
    void getUserDetailsUnauthenticatedTest() throws Exception {
        UserEntity user = createUser("Joe", "Thomas", "jThomas@kdminer.com");

        mockMvc.perform(
                get("/api/users/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void GetUserDetailsTest() throws Exception {
        UserEntity user = createUser("Sarah", "Conner", "sConner@gmail.com");

        mockMvc.perform(
                get("/api/users/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(createCookie(user))
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }

    @Test
    @Transactional
    void GetUserDetailsForAnotherAccountTest() throws Exception {
        UserEntity userA = createUser("Bob", "Barker", "BobBarker@yahoo.com");
        UserEntity userB = createUser("Tim", "Cook", "tim@cook.net");

        mockMvc.perform(
                get("/api/users/" + userA.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(createCookie(userB))
            )
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void GetUsersListTest() throws Exception {
        // Create admin user
        UserEntity adminUser = createUser("AdminUser", "Admin", "admin@something.com");
        RoleEntity adminRole = roleRepository.findByName("ROLE_ADMIN");
        assertNotNull(adminRole);
        adminUser.addRole(adminRole);

        userRepository.save(adminUser);

        mockMvc.perform(
                get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(createCookie(adminUser))
        ).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void GetUsersListUnauthorizedTest() throws Exception {
        UserEntity user = createUser("Bob", "Barker", "BobBarker@yahoo.com");

        mockMvc.perform(
                get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(createCookie(user))
        ).andExpect(status().isForbidden());
    }

    private UserEntity createUser(String firstName, String lastName, String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            user = new UserEntity(firstName, lastName, email);
            user.setEmailVerified(true);
            user.setEncryptedPassword(bCryptPasswordEncoder.encode("superSecretPassword"));
            user.setUserId(utils.generateId());
            user = userRepository.save(user);
            // flush user to the database
            // JPQL queries will not work without first flushing new users to the database,
            // This also require that each method that uses this helper function has a transaction open
            entityManager.flush();
        }
        assertNotNull(user);

        return user;
    }

    private Cookie createCookie(UserEntity user) {
        String token = Jwts.builder()
                .setSubject(user.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8)) // Set to 8 hours
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();

        Cookie cookie = new Cookie(SecurityConstants.ACCESS_TOKEN, token);
        cookie.setMaxAge(60*60*8); // expires in 8 hours
        return cookie;
    }

}