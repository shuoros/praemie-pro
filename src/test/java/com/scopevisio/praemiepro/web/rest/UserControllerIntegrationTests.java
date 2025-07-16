package com.scopevisio.praemiepro.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scopevisio.praemiepro.AbstractTest;
import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.web.vm.UserVM;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTests extends AbstractTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    @Transactional
    void beforeAll() {
        final Authority adminAuthority = new Authority();
        adminAuthority.setName("ROLE_ADMIN");
        final Authority userAuthority = new Authority();
        userAuthority.setName("ROLE_USER");

        final User admin = new User();
        admin.setEmail(ADMIN_EMAIL);
        admin.setActivated(true);
        admin.setPassword(passwordEncoder.encode(PASSWORD));
        admin.setAuthorities(Set.of(adminAuthority));
        userRepository.saveAndFlush(admin);
        
        final User user = new User();
        user.setEmail(USER_EMAIL);
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setAuthorities(Set.of(userAuthority));
        userRepository.saveAndFlush(user);
    }

    @AfterAll
    @Transactional
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUserWithValidData() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setEmail(USER_EMAIL_2);
        userVM.setPassword(PASSWORD);
        userVM.setFirstName(FIRST_NAME);
        userVM.setLastName(LAST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(FIRST_NAME + " " + LAST_NAME))
                .andExpect(jsonPath("$.email").value(USER_EMAIL_2));
    }

    @Test
    void testRegisterUserWithExistingEmail() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setEmail(USER_EMAIL);
        userVM.setPassword(PASSWORD);
        userVM.setFirstName(FIRST_NAME);
        userVM.setLastName(LAST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().isConflict());
    }

    @Test
    void testRegisterUserWithWrongEmail() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setEmail("wrong@");
        userVM.setPassword(PASSWORD);
        userVM.setFirstName(FIRST_NAME);
        userVM.setLastName(LAST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterUserWithNullEmail() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setPassword(PASSWORD);
        userVM.setFirstName(FIRST_NAME);
        userVM.setLastName(LAST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterUserWithWrongPasswordLength() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setEmail(USER_EMAIL);
        userVM.setPassword("p");
        userVM.setFirstName(FIRST_NAME);
        userVM.setLastName(LAST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterUserWithNullPassword() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setEmail(USER_EMAIL);
        userVM.setFirstName(FIRST_NAME);
        userVM.setLastName(LAST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterUserWithNullFirstName() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setEmail(USER_EMAIL);
        userVM.setPassword(PASSWORD);
        userVM.setLastName(LAST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterUserWithNullLastName() throws Exception {
        // Arrange
        final UserVM userVM = new UserVM();
        userVM.setEmail(USER_EMAIL);
        userVM.setPassword(PASSWORD);
        userVM.setFirstName(FIRST_NAME);

        // Act & Assert
        mockMvc
                .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(userVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = ADMIN_EMAIL, authorities = {"SCOPE_ROLE_ADMIN", "SCOPE_ROLE_USER"})
    void testDeleteUserWithAdminAuthority() throws Exception {
        // Arrange
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act & Assert
        mockMvc
                .perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = "SCOPE_ROLE_USER")
    void testDeleteUserWithUserAuthority() throws Exception {
        // Arrange
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act & Assert
        mockMvc
                .perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isForbidden());
        assertTrue(userRepository.existsById(user.getId()));
    }

    @Test
    void testDeleteUserWithNoAuthority() throws Exception {
        // Arrange
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act & Assert
        mockMvc
                .perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isUnauthorized());
        assertTrue(userRepository.existsById(user.getId()));
    }
}
