package com.scopevisio.praemiepro.web.rest;

import com.scopevisio.praemiepro.AbstractTest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTests extends AbstractTest {

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
    @WithMockUser(username = ADMIN_EMAIL, authorities = {"SCOPE_ROLE_ADMIN", "SCOPE_ROLE_USER"})
    void testDeleteUserWithAdminAuthority() throws Exception {
        // Arrange
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act & Assert
        mockMvc
                .perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());
        assertTrue(userRepository.findById(user.getId()).isEmpty());
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
        assertTrue(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void testDeleteUserWithNoAuthority() throws Exception {
        // Arrange
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act & Assert
        mockMvc
                .perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isUnauthorized());
        assertTrue(userRepository.findById(user.getId()).isPresent());
    }
}
