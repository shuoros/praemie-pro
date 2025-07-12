package com.scopevisio.praemiepro.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.web.rest.vm.CalculateVM;
import com.scopevisio.praemiepro.web.rest.vm.LoginVM;
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

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InsuranceCalculatorControllerIntegrationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCalculateInsurance() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode("50374");

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.yearlyPrice").value(1650.00))
                .andExpect(jsonPath("$.monthlyPrice").value(137.50));
    }

    @Test
    void testCalculateInsurance2() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.OLD_TIMER);
        calculateVM.setYearlyDrive(4500);
        calculateVM.setZipcode("50374");

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.yearlyPrice").value(1100.00))
                .andExpect(jsonPath("$.monthlyPrice").value(91.67));
    }

    @Test
    void testCalculateInsuranceWithWrongZipcode() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode("00000");

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is(406));
    }

    @Test
    void testCalculateInsuranceWithNullValues() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode("00000");

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCalculateInsuranceWithWrongZipcodeLength() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode("5037");

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCalculateInsuranceWithMinusYearlyDrive() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(-10000);
        calculateVM.setZipcode("50374");

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }
}
