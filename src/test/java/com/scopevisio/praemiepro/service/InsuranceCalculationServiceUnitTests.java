package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.State;
import com.scopevisio.praemiepro.domain.SystemParameter;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.exception.WrongZipcodeException;
import com.scopevisio.praemiepro.repository.StateRepository;
import com.scopevisio.praemiepro.service.dto.InsuranceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InsuranceCalculationServiceUnitTests {

    private static final String VALID_ZIPCODE = "50374";
    private static final String INVALID_ZIPCODE = "00000";

    @Mock
    private StateRepository stateRepository;

    @Mock
    private SystemParameterService systemParameterService;

    @InjectMocks
    private InsuranceCalculationService insuranceCalculationService;

    private void setupWithValidZipcode() {
        final State state = new State();
        state.setInsuranceFactor(1.1f);
        when(stateRepository.findOneByRegions_zipcode(VALID_ZIPCODE)).thenReturn(Optional.of(state));

        final SystemParameter systemParameter = new SystemParameter();
        systemParameter.setBaseInsurancePrice(BigDecimal.valueOf(1000));
        when(systemParameterService.getSystemParameter()).thenReturn(systemParameter);
    }

    @Test
    void testCalculateInsurance1() {
        // Arrange
        setupWithValidZipcode();
        final VehicleType vehicleType = VehicleType.SPORT;
        final Integer yearlyDrive = 10000;

        // Act
        final InsuranceDTO insuranceDTO = insuranceCalculationService.calculateInsurance(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(insuranceDTO);
        assertEquals(BigDecimal.valueOf(1650.00).setScale(2), insuranceDTO.getYearlyPrice());
        assertEquals(BigDecimal.valueOf(137.50).setScale(2), insuranceDTO.getMonthlyPrice());
    }

    @Test
    void testCalculateInsurance2() {
        // Arrange
        setupWithValidZipcode();
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;

        // Act
        final InsuranceDTO insuranceDTO = insuranceCalculationService.calculateInsurance(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(insuranceDTO);
        assertEquals(BigDecimal.valueOf(1100.00).setScale(2), insuranceDTO.getYearlyPrice());
        assertEquals(BigDecimal.valueOf(91.67).setScale(2), insuranceDTO.getMonthlyPrice());
    }

    @Test
    void testCalculateInsuranceWithInvalidZipcode() {
        // Arrange
        when(stateRepository.findOneByRegions_zipcode(INVALID_ZIPCODE)).thenReturn(Optional.empty());

        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;

        // Act & Assert
        assertThrows(
                WrongZipcodeException.class,
                () -> insuranceCalculationService.calculateInsurance(
                        vehicleType,
                        yearlyDrive,
                        INVALID_ZIPCODE
                )
        );
    }
}
