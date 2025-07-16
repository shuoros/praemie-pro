package com.scopevisio.praemiepro.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scopevisio.praemiepro.AbstractTest;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.web.vm.CalculateVM;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class InsuranceCalculatorControllerIntegrationTests extends AbstractTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCalculateInsuranceWithValidData() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.yearlyPrice").value(1650.00))
                .andExpect(jsonPath("$.monthlyPrice").value(137.50));
    }

    @Test
    void testCalculateInsuranceWithAnotherValidData() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.OLD_TIMER);
        calculateVM.setYearlyDrive(4500);
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.yearlyPrice").value(1100.00))
                .andExpect(jsonPath("$.monthlyPrice").value(91.67));
    }

    @Test
    void testCalculateInsuranceWithJsonKeysAsSnakeCase() throws Exception {
        // Arrange
        final JSONObject payload = new JSONObject();
        payload.put("vehicle_type", "SPORT");
        payload.put("yearly_drive", 10000);
        payload.put("zipcode", VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(payload.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.yearlyPrice").value(1650.00))
                .andExpect(jsonPath("$.monthlyPrice").value(137.50));
    }

    @Test
    void testCalculateInsuranceWithWrongZipcode() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.SPORT);
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode(INVALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is(406));
    }

    @Test
    void testCalculateInsuranceWithNullVehicleType() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setYearlyDrive(10000);
        calculateVM.setZipcode(INVALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCalculateInsuranceWithNullYearlyDrive() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.OLD_TIMER);
        calculateVM.setZipcode(INVALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCalculateInsuranceWithNullZipcode() throws Exception {
        // Arrange
        final CalculateVM calculateVM = new CalculateVM();
        calculateVM.setVehicleType(VehicleType.OLD_TIMER);
        calculateVM.setYearlyDrive(10000);

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
        calculateVM.setZipcode(VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(calculateVM)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCalculateInsuranceWithWrongVehicleType() throws Exception {
        // Arrange
        final JSONObject payload = new JSONObject();
        payload.put("vehicleType", "AUTO");
        payload.put("yearlyDrive", 10000);
        payload.put("zipcode", VALID_ZIPCODE);

        // Act & Assert
        mockMvc
                .perform(post("/public/insurance/calculate").contentType(MediaType.APPLICATION_JSON).content(payload.toString()))
                .andExpect(status().is4xxClientError());
    }
}
