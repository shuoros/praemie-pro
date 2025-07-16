package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.config.Constants;
import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.Order;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.exception.UserNotFoundException;
import com.scopevisio.praemiepro.exception.WrongZipcodeException;
import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.AbstractTest;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import com.scopevisio.praemiepro.util.NumberUtils;
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
        final Authority adminAuthority = new Authority();
        adminAuthority.setName("ROLE_ADMIN");
        final Authority authority = new Authority();
        authority.setName("ROLE_USER");
        final Authority authority2 = new Authority();
        authority2.setName("ROLE_USER");

        final User admin = new User();
        admin.setEmail(ADMIN_EMAIL);
        admin.setActivated(true);
        admin.setPassword(passwordEncoder.encode(PASSWORD));
        admin.setAuthorities(Set.of(adminAuthority));
        userRepository.saveAndFlush(admin);

        final User user1 = new User();
        user1.setEmail(USER_EMAIL);
        user1.setActivated(true);
        user1.setPassword(passwordEncoder.encode(PASSWORD));
        user1.setAuthorities(Set.of(authority));
        user1.setCreatedBy(Constants.SYSTEM);
        userRepository.saveAndFlush(user1);

        final User user2 = new User();
        user2.setEmail(USER_EMAIL_2);
        user2.setActivated(true);
        user2.setPassword(passwordEncoder.encode(PASSWORD));
        user2.setAuthorities(Set.of(authority2));
        user2.setCreatedBy(Constants.SYSTEM);
        userRepository.saveAndFlush(user2);
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
        final OrderDTO orderDTO = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user
        );

        // Assert
        assertNotNull(orderDTO);
        assertNotNull(orderDTO.getId());
        assertNotNull(orderDTO.getUser());
        assertEquals(USER_EMAIL, orderDTO.getUser().getEmail());
        assertEquals(vehicleType.translate(), orderDTO.getVehicleType());
        assertEquals(NumberUtils.formatToGermanNumber(yearlyDrive), orderDTO.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, orderDTO.getZipcode());
        assertEquals(NumberUtils.formatToGermanNumber(AbstractTest.getBigDecimal(1650.00)), orderDTO.getYearlyPrice());
        assertNotNull(orderDTO.getDate());
    }

    @Test
    void testCreateOrderWithAnotherValidData() {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        // Act
        final OrderDTO orderDTO = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user
        );

        // Assert
        assertNotNull(orderDTO);
        assertNotNull(orderDTO.getId());
        assertNotNull(orderDTO.getUser());
        assertEquals(USER_EMAIL, orderDTO.getUser().getEmail());
        assertEquals(vehicleType.translate(), orderDTO.getVehicleType());
        assertEquals(NumberUtils.formatToGermanNumber(yearlyDrive), orderDTO.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, orderDTO.getZipcode());
        assertEquals(NumberUtils.formatToGermanNumber(AbstractTest.getBigDecimal(1100.00)), orderDTO.getYearlyPrice());
        assertNotNull(orderDTO.getDate());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void testRegisterOrderWithValidData() {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final Integer yearlyDrive = 10000;

        // Act
        final OrderDTO orderDTO = orderService.registerOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(orderDTO);
        assertNotNull(orderDTO.getId());
        assertNotNull(orderDTO.getUser());
        assertEquals(USER_EMAIL, orderDTO.getUser().getEmail());
        assertEquals(vehicleType.translate(), orderDTO.getVehicleType());
        assertEquals(NumberUtils.formatToGermanNumber(yearlyDrive), orderDTO.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, orderDTO.getZipcode());
        assertEquals(NumberUtils.formatToGermanNumber(AbstractTest.getBigDecimal(1650.00)), orderDTO.getYearlyPrice());
        assertNotNull(orderDTO.getDate());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void testRegisterOrderWithAnotherValidData() {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;

        // Act
        final OrderDTO orderDTO = orderService.registerOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(orderDTO);
        assertNotNull(orderDTO.getId());
        assertNotNull(orderDTO.getUser());
        assertEquals(USER_EMAIL, orderDTO.getUser().getEmail());
        assertEquals(vehicleType.translate(), orderDTO.getVehicleType());
        assertEquals(NumberUtils.formatToGermanNumber(yearlyDrive), orderDTO.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, orderDTO.getZipcode());
        assertEquals(NumberUtils.formatToGermanNumber(AbstractTest.getBigDecimal(1100.00)), orderDTO.getYearlyPrice());
        assertNotNull(orderDTO.getDate());
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

    @Test
    void testFindOrdersOfUser() {
        // Arrange
        orderRepository.deleteAll();;

        final VehicleType vehicleType = VehicleType.SPORT;
        final Integer yearlyDrive = 10000;

        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();
        final UserDTO userDTO = UserDTO.builder().id(user.getId()).build();
        final OrderDTO orderDTO = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user
        );

        // Act
        final List<OrderDTO> foundedOrders = orderService.findOrdersOfUser(userDTO);

        // Assert
        assertNotNull(foundedOrders);
        assertEquals(1, foundedOrders.size());
        assertTrue(foundedOrders.contains(orderDTO));
    }

    @Test
    void testFindOrdersOfUserWithNoUser() {
        // Act
        final List<OrderDTO> foundedOrders = orderService.findOrdersOfUser(null);

        // Assert
        assertNotNull(foundedOrders);
        assertEquals(0, foundedOrders.size());
    }

    @Test
    void testFindOrdersBasedOnAuthoritiesWithAdminAuthority() {
        // Arrange
        orderRepository.deleteAll();;

        final VehicleType vehicleType = VehicleType.SPORT;
        final Integer yearlyDrive = 10000;

        final User user1 = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();
        final OrderDTO orderDTO1 = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user1
        );

        final User user2 = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL_2).orElseThrow();
        final OrderDTO orderDTO2 = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user2
        );

        final List<String> authorities = List.of("ROLE_ADMIN");

        // Act
        final List<OrderDTO> foundedOrders = orderService.findOrdersBasedOnAuthorities(authorities, null);

        // Assert
        assertNotNull(foundedOrders);
        assertEquals(2, foundedOrders.size());
        assertTrue(foundedOrders.contains(orderDTO1));
        assertTrue(foundedOrders.contains(orderDTO2));
    }

    @Test
    void testFindOrdersBasedOnAuthoritiesWithUserAuthority() {
        // Arrange
        orderRepository.deleteAll();;

        final VehicleType vehicleType = VehicleType.SPORT;
        final Integer yearlyDrive = 10000;

        final User user1 = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();
        final OrderDTO orderDTO1 = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user1
        );

        final User user2 = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL_2).orElseThrow();
        final OrderDTO orderDTO2 = orderService.createOrder(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE,
                user2
        );

        final List<String> authorities = List.of("ROLE_USER");

        // Act
        final List<OrderDTO> foundedOrdersUser1 = orderService.findOrdersBasedOnAuthorities(authorities, user1);
        final List<OrderDTO> foundedOrdersUser2 = orderService.findOrdersBasedOnAuthorities(authorities, user2);

        // Assert
        assertNotNull(foundedOrdersUser1);
        assertNotNull(foundedOrdersUser2);
        assertEquals(1, foundedOrdersUser1.size());
        assertEquals(1, foundedOrdersUser2.size());
        assertTrue(foundedOrdersUser1.contains(orderDTO1));
        assertTrue(foundedOrdersUser2.contains(orderDTO2));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = "SCOPE_ROLE_USER")
    void testUpdateOrderWithTheOwnerUser() {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final VehicleType updatedVehicleType = VehicleType.SEDAN;
        final int yearlyDrive = 10000;
        final int updatedYearlyDrive = 50000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final Order order = new Order();
        order.setVehicleType(vehicleType);
        order.setYearlyDrive(yearlyDrive);
        order.setZipcode(VALID_ZIPCODE);
        order.setUser(user);
        orderRepository.save(order);

        // Act
        final Optional<OrderDTO> optionalOrderDTO = orderService.updateOrder(
                order.getId(),
                updatedVehicleType,
                updatedYearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertTrue(optionalOrderDTO.isPresent());
        final OrderDTO orderDTO = optionalOrderDTO.get();
        assertNotNull(orderDTO.getId());
        assertNotNull(orderDTO.getUser());
        assertEquals(USER_EMAIL, orderDTO.getUser().getEmail());
        assertEquals(updatedVehicleType.translate(), orderDTO.getVehicleType());
        assertEquals(NumberUtils.formatToGermanNumber(updatedYearlyDrive), orderDTO.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, orderDTO.getZipcode());
        assertEquals(NumberUtils.formatToGermanNumber(AbstractTest.getBigDecimal(2750.00)), orderDTO.getYearlyPrice());
        assertNotNull(orderDTO.getDate());
    }

    @Test
    @WithMockUser(username = ADMIN_EMAIL, authorities = {"SCOPE_ROLE_ADMIN", "SCOPE_ROLE_USER"})
    void testUpdateOrderWithAdminAuthority() {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final VehicleType updatedVehicleType = VehicleType.SEDAN;
        final int yearlyDrive = 10000;
        final int updatedYearlyDrive = 50000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final Order order = new Order();
        order.setVehicleType(vehicleType);
        order.setYearlyDrive(yearlyDrive);
        order.setZipcode(VALID_ZIPCODE);
        order.setUser(user);
        orderRepository.save(order);

        // Act
        final Optional<OrderDTO> optionalOrderDTO = orderService.updateOrder(
                order.getId(),
                updatedVehicleType,
                updatedYearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertTrue(optionalOrderDTO.isPresent());
        final OrderDTO orderDTO = optionalOrderDTO.get();
        assertNotNull(orderDTO.getId());
        assertNotNull(orderDTO.getUser());
        assertEquals(USER_EMAIL, orderDTO.getUser().getEmail());
        assertEquals(updatedVehicleType.translate(), orderDTO.getVehicleType());
        assertEquals(NumberUtils.formatToGermanNumber(updatedYearlyDrive), orderDTO.getYearlyDrive());
        assertEquals(VALID_ZIPCODE, orderDTO.getZipcode());
        assertEquals(NumberUtils.formatToGermanNumber(AbstractTest.getBigDecimal(2750.00)), orderDTO.getYearlyPrice());
        assertNotNull(orderDTO.getDate());
    }

    @Test
    @WithMockUser(username = USER_EMAIL_2, authorities = "SCOPE_ROLE_USER")
    void testUpdateOrderWithAnotherUserThanOwner() {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final VehicleType updatedVehicleType = VehicleType.SEDAN;
        final int yearlyDrive = 10000;
        final int updatedYearlyDrive = 50000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final Order order = new Order();
        order.setVehicleType(vehicleType);
        order.setYearlyDrive(yearlyDrive);
        order.setZipcode(VALID_ZIPCODE);
        order.setUser(user);
        orderRepository.save(order);

        // Act
        final Optional<OrderDTO> optionalOrderDTO = orderService.updateOrder(
                order.getId(),
                updatedVehicleType,
                updatedYearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertTrue(optionalOrderDTO.isEmpty());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    void testUpdateOrderWithInvalidZipcode() {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final VehicleType updatedVehicleType = VehicleType.SEDAN;
        final int yearlyDrive = 10000;
        final int updatedYearlyDrive = 50000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final Order order = new Order();
        order.setVehicleType(vehicleType);
        order.setYearlyDrive(yearlyDrive);
        order.setZipcode(VALID_ZIPCODE);
        order.setUser(user);
        orderRepository.save(order);

        // Act & Assert
        assertThrows(
                WrongZipcodeException.class,
                () -> orderService.updateOrder(
                        order.getId(),
                        updatedVehicleType,
                        updatedYearlyDrive,
                        INVALID_ZIPCODE
                )
        );
    }
}
