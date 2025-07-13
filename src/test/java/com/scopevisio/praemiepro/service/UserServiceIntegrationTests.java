package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.Authority;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIntegrationTests {

    private static final String EMAIL = "user@example.com";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @BeforeAll
    @Transactional
    void beforeAll() {
        final Authority authority = new Authority();
        authority.setName("ROLE_USER");

        final User user = new User();
        user.setEmail(EMAIL);
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode("test"));
        user.setAuthorities(Set.of(authority));
        userRepository.saveAndFlush(user);
    }

    @AfterAll
    @Transactional
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = EMAIL)
    void testGetCurrentUser() {
        // Act
        final Optional<User> optionalUser = userService.getCurrentUser();

        // Assert
        assertTrue(optionalUser.isPresent());
        assertEquals(EMAIL, optionalUser.get().getEmail());
    }

    @Test
    void testGetCurrentUserWhenNoUserAuthenticated() {
        // Act
        final Optional<User> optionalUser = userService.getCurrentUser();

        // Assert
        assertTrue(optionalUser.isEmpty());
    }
}
