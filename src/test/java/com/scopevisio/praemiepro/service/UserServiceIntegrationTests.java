package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.AbstractTest;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIntegrationTests extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @BeforeAll
    @Transactional
    void beforeAll() {
        final User user = new User();
        user.setEmail(USER_EMAIL);
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode("test"));
        userRepository.saveAndFlush(user);
    }

    @AfterAll
    @Transactional
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void testGetCurrentUser() {
        // Act
        final Optional<User> optionalUser = userService.getCurrentUser();

        // Assert
        assertTrue(optionalUser.isPresent());
        assertEquals(USER_EMAIL, optionalUser.get().getEmail());
    }

    @Test
    void testGetCurrentUserWhenNoUserAuthenticated() {
        // Act
        final Optional<User> optionalUser = userService.getCurrentUser();

        // Assert
        assertTrue(optionalUser.isEmpty());
    }
}
