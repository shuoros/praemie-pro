package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.AbstractTest;
import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.exception.EmailAlreadyExistsException;
import com.scopevisio.praemiepro.exception.WrongZipcodeException;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.service.dto.UserDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        final Authority authority = new Authority();
        authority.setName("ROLE_USER");

        final User user = new User();
        user.setEmail(USER_EMAIL);
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

    @Test
    void testFindUserDTOByIdWithValidId() {
        //Arrange
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act
        final Optional<UserDTO> optionalUserDTO = userService.findUserDTOById(user.getId());

        // Assert
        assertTrue(optionalUserDTO.isPresent());

        final UserDTO userDTO = optionalUserDTO.get();
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFirstName() + " " + user.getLastName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void testFindUserDTOByIdWithInvalidId() {
        // Act
        final Optional<UserDTO> optionalUserDTO = userService.findUserDTOById(-1L);

        // Assert
        assertTrue(optionalUserDTO.isEmpty());
    }

    @Test
    void testFindCustomerUsers() {
        //Arrange
        final Authority authority = new Authority();
        authority.setName("ROLE_ADMIN");

        final User admin = new User();
        admin.setEmail(ADMIN_EMAIL);
        admin.setActivated(true);
        admin.setPassword(passwordEncoder.encode("test"));
        admin.setAuthorities(Set.of(authority));
        userRepository.saveAndFlush(admin);

        // Act
        final List<UserDTO> customerUsers = userService.findCustomerUsers();

        // Assert
        assertNotNull(customerUsers);
        assertEquals(1, customerUsers.size());
        assertFalse(
                customerUsers.stream().anyMatch(userDTO -> ADMIN_EMAIL.equals(userDTO.getEmail()))
        );
        assertTrue(
                customerUsers.stream().anyMatch(userDTO -> USER_EMAIL.equals(userDTO.getEmail()))
        );
    }

    @Test
    void testRegisterUserWithValidData() {
        // Act
        userService.registerUser(
                FIRST_NAME,
                LAST_NAME,
                USER_EMAIL_2,
                PASSWORD
        );

        // Assert
        final Optional<User> optionalUser = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL_2);
        assertTrue(optionalUser.isPresent());

        final User registeredUser = optionalUser.get();
        assertEquals(FIRST_NAME, registeredUser.getFirstName());
        assertEquals(LAST_NAME, registeredUser.getLastName());
        assertTrue(registeredUser.isActivated());
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        // Act & Assert
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.registerUser(
                        FIRST_NAME,
                        LAST_NAME,
                        USER_EMAIL,
                        PASSWORD
                )
        );
    }
}
