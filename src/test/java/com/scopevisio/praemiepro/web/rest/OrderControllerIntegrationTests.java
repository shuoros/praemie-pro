package com.scopevisio.praemiepro.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scopevisio.praemiepro.AbstractTest;
import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.web.rest.vm.CalculateVM;
import com.scopevisio.praemiepro.web.rest.vm.OrderVM;
import org.json.JSONObject;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerIntegrationTests extends AbstractTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;

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
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = ADMIN_EMAIL, authorities = {"SCOPE_ROLE_ADMIN", "SCOPE_ROLE_USER"})
    void testCreateOrderWithValidData() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final int yearlyDrive = 10000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final OrderVM orderVM = new OrderVM();
        orderVM.setVehicleType(vehicleType);
        orderVM.setYearlyDrive(yearlyDrive);
        orderVM.setZipcode(VALID_ZIPCODE);
        orderVM.setUser(user);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(orderVM)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.vehicleType").value(vehicleType.toString()))
                .andExpect(jsonPath("$.yearlyDrive").value(yearlyDrive))
                .andExpect(jsonPath("$.yearlyPrice").value(1650.00))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").value(USER_EMAIL));
    }

    @Test
    @WithMockUser(username = ADMIN_EMAIL, authorities = {"SCOPE_ROLE_ADMIN", "SCOPE_ROLE_USER"})
    void testCreateOrderWithAnotherValidData() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final int yearlyDrive = 4500;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final OrderVM orderVM = new OrderVM();
        orderVM.setVehicleType(vehicleType);
        orderVM.setYearlyDrive(yearlyDrive);
        orderVM.setZipcode(VALID_ZIPCODE);
        orderVM.setUser(user);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(orderVM)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.vehicleType").value(vehicleType.toString()))
                .andExpect(jsonPath("$.yearlyDrive").value(yearlyDrive))
                .andExpect(jsonPath("$.yearlyPrice").value(1100.00))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").value(USER_EMAIL));
    }

    @Test
    void testCreateOrderWithUnAuthenticatedClient() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final int yearlyDrive = 10000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final OrderVM orderVM = new OrderVM();
        orderVM.setVehicleType(vehicleType);
        orderVM.setYearlyDrive(yearlyDrive);
        orderVM.setZipcode(VALID_ZIPCODE);
        orderVM.setUser(user);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(orderVM)))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testCreateOrderWithUnauthorizedClient() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final int yearlyDrive = 10000;
        final User user = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(USER_EMAIL).orElseThrow();

        final OrderVM orderVM = new OrderVM();
        orderVM.setVehicleType(vehicleType);
        orderVM.setYearlyDrive(yearlyDrive);
        orderVM.setZipcode(VALID_ZIPCODE);
        orderVM.setUser(user);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(orderVM)))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = ADMIN_EMAIL, authorities = {"SCOPE_ROLE_ADMIN", "SCOPE_ROLE_USER"})
    void testRegisterOrderWithValidDataAndAdminAuthority() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final int yearlyDrive = 10000;

        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(vehicleType);
        calculateVM.setYearlyDrive(yearlyDrive);
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.vehicleType").value(vehicleType.toString()))
                .andExpect(jsonPath("$.yearlyDrive").value(yearlyDrive))
                .andExpect(jsonPath("$.yearlyPrice").value(1650.00))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").value(ADMIN_EMAIL));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithValidData() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final int yearlyDrive = 10000;
        
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(vehicleType);
        calculateVM.setYearlyDrive(yearlyDrive);
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.vehicleType").value(vehicleType.toString()))
                .andExpect(jsonPath("$.yearlyDrive").value(yearlyDrive))
                .andExpect(jsonPath("$.yearlyPrice").value(1650.00))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").value(USER_EMAIL));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithAnotherValidData() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final int yearlyDrive = 4500;
        
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(vehicleType);
        calculateVM.setYearlyDrive(yearlyDrive);
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.vehicleType").value(vehicleType.toString()))
                .andExpect(jsonPath("$.yearlyDrive").value(yearlyDrive))
                .andExpect(jsonPath("$.yearlyPrice").value(1100.00))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").value(USER_EMAIL));
    }

    @Test
    void testRegisterOrderWithUnAuthenticatedClient() throws Exception {
        // Arrange
        final VehicleType vehicleType = VehicleType.SPORT;
        final int yearlyDrive = 10000;

        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(vehicleType);
        calculateVM.setYearlyDrive(yearlyDrive);
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithJsonKeysAsSnakeCase() throws Exception {
        // Arrange
        final String vehicleType = "SPORT";
        final int yearlyDrive = 10000;
        
        final JSONObject payload = new JSONObject();
        payload.put("vehicle_type", vehicleType);
        payload.put("yearly_drive", yearlyDrive);
        payload.put("zipcode", VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(payload.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.vehicleType").value(vehicleType))
                .andExpect(jsonPath("$.yearlyDrive").value(yearlyDrive))
                .andExpect(jsonPath("$.yearlyPrice").value(1650.00))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").value(USER_EMAIL));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithWrongZipcode() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode(INVALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is(406));
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithNullVehicleType() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode(INVALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithNullYearlyDrive() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.OLD_TIMER);
        calculateVM.setZipcode(INVALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithNullZipcode() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.OLD_TIMER);
        calculateVM.setYearlyDrive(10000);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithWrongZipcodeLength() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode("5037");

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithMinusYearlyDrive() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(-10000);
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = USER_EMAIL, authorities = {"SCOPE_ROLE_USER"})
    void testRegisterOrderWithWrongVehicleType() throws Exception {
        // Arrange
        final JSONObject payload = new JSONObject();
        payload.put("vehicleType", "AUTO");
        payload.put("yearlyDrive", 10000);
        payload.put("zipcode", VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/api/orders/register").contentType(MediaType.APPLICATION_JSON).content(payload.toString()))
                .andExpect(status().is4xxClientError());
    }
}
