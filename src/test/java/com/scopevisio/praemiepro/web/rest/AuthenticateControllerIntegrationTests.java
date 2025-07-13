package com.scopevisio.praemiepro.web.rest;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scopevisio.praemiepro.AbstractTest;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.web.rest.vm.LoginVM;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticateControllerIntegrationTests extends AbstractTest {

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
        final User activatedUser = new User();
        activatedUser.setEmail(USER_EMAIL);
        activatedUser.setActivated(true);
        activatedUser.setPassword(passwordEncoder.encode(PASSWORD));
        userRepository.saveAndFlush(activatedUser);

        final User nonActivatedUser = new User();
        nonActivatedUser.setEmail(USER_EMAIL_2);
        nonActivatedUser.setActivated(false);
        nonActivatedUser.setPassword(passwordEncoder.encode(PASSWORD));
        userRepository.saveAndFlush(nonActivatedUser);
    }

    @AfterAll
    @Transactional
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void testAuthenticate() throws Exception {
        // Arrange
        final LoginVM login = new LoginVM();
        login.setEmail(USER_EMAIL);
        login.setPassword(PASSWORD);

        // Act & Assert
        mockMvc
                .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(header().string("Authorization", not(nullValue())))
                .andExpect(header().string("Authorization", not(is(emptyString()))));
    }

    @Test
    @Transactional
    void testAuthenticateWithRememberMe() throws Exception {
        // Arrange
        final LoginVM login = new LoginVM();
        login.setEmail(USER_EMAIL);
        login.setPassword(PASSWORD);
        login.setRememberMe(true);

        // Act & Assert
        mockMvc
                .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(header().string("Authorization", not(nullValue())))
                .andExpect(header().string("Authorization", not(is(emptyString()))));
    }

    @Test
    void testAuthenticateWithWrongUsernameAndPassword() throws Exception {
        // Arrange
        final LoginVM login = new LoginVM();
        login.setEmail("wrong@example.com");
        login.setPassword("wrong password");

        // Act & Assert
        mockMvc
                .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(header().doesNotExist("Authorization"));
    }

    @Test
    void testAuthenticateWithRightUsernameButWrongPassword() throws Exception {
        // Arrange
        final LoginVM login = new LoginVM();
        login.setEmail(USER_EMAIL);
        login.setPassword("wrong password");

        // Act & Assert
        mockMvc
                .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(header().doesNotExist("Authorization"));
    }

    @Test
    void testAuthenticateWithNonActivatedUser() throws Exception {
        // Arrange
        final LoginVM login = new LoginVM();
        login.setEmail(USER_EMAIL_2);
        login.setPassword(PASSWORD);

        // Act & Assert
        mockMvc
                .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(header().doesNotExist("Authorization"));
    }
}
