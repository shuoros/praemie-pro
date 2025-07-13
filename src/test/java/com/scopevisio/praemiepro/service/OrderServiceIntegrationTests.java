package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.Order;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.exception.UserNotFoundException;
import com.scopevisio.praemiepro.exception.WrongZipcodeException;
import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.AbstractTest;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceIntegrationTests extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderService orderService;

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
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateOrderWithValidData() {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final Integer yearlyDrive = 10000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act
        final Order order = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user
        );

        // Assert
        assertNotNull(order);
        assertNotNull(order.getId());
        assertNotNull(order.getUser());
        assertEquals(USER_EMAIL, order.getUser().getEmail());
        assertEquals(vehicleType, order.getVehicleType());
        assertEquals(yearlyDrive, order.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, order.getZipcode());
        assertEquals(AbstractTest.getBigDecimal(1650.00), order.getYearlyPrice());
    }

    @Test
    void testCreateOrderWithAnotherValidData() {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act
        final Order order = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user
        );

        // Assert
        assertNotNull(order);
        assertNotNull(order.getId());
        assertNotNull(order.getUser());
        assertEquals(USER_EMAIL, order.getUser().getEmail());
        assertEquals(vehicleType, order.getVehicleType());
        assertEquals(yearlyDrive, order.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, order.getZipcode());
        assertEquals(AbstractTest.getBigDecimal(1100.00), order.getYearlyPrice());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void testRegisterOrderWithValidData() {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final Integer yearlyDrive = 10000;

        // Act
        final Order order = orderService.registerOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(order);
        assertNotNull(order.getId());
        assertNotNull(order.getUser());
        assertEquals(USER_EMAIL, order.getUser().getEmail());
        assertEquals(vehicleType, order.getVehicleType());
        assertEquals(yearlyDrive, order.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, order.getZipcode());
        assertEquals(AbstractTest.getBigDecimal(1650.00), order.getYearlyPrice());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void testRegisterOrderWithAnotherValidData() {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;

        // Act
        final Order order = orderService.registerOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(order);
        assertNotNull(order.getId());
        assertNotNull(order.getUser());
        assertEquals(USER_EMAIL, order.getUser().getEmail());
        assertEquals(vehicleType, order.getVehicleType());
        assertEquals(yearlyDrive, order.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, order.getZipcode());
        assertEquals(AbstractTest.getBigDecimal(1100.00), order.getYearlyPrice());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void testRegisterOrderWithInvalidZipcode() {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;

        // Act & Assert
        assertThrows(
                WrongZipcodeException.class,
                () -> orderService.registerOrder(
                        vehicleType,
                        yearlyDrive,
                        INVALID_ZIPCODE
                )
        );
    }

    @Test
    void testRegisterOrderWithNoUserFound() {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> orderService.registerOrder(
                        vehicleType,
                        yearlyDrive,
                        VALID_ZIPCODE
                )
        );
    }
}
