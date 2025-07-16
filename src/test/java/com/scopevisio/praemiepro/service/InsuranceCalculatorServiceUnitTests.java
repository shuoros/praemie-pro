package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.State;
import com.scopevisio.praemiepro.domain.SystemParameter;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.exception.WrongZipcodeException;
import com.scopevisio.praemiepro.repository.StateRepository;
import com.scopevisio.praemiepro.service.dto.InsuranceDTO;
import com.scopevisio.praemiepro.AbstractTest;
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
public class InsuranceCalculatorServiceUnitTests extends AbstractTest {

    @Mock
    private StateRepository stateRepository;

    @Mock
    private SystemParameterService systemParameterService;

    @InjectMocks
    private InsuranceCalculatorService insuranceCalculatorService;

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
        final InsuranceDTO insuranceDTO = insuranceCalculatorService.calculateInsurance(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(insuranceDTO);
        assertEquals(AbstractTest.getBigDecimal(1650.00), insuranceDTO.getYearlyPrice());
        assertEquals(AbstractTest.getBigDecimal(137.50), insuranceDTO.getMonthlyPrice());
    }

    @Test
    void testCalculateInsurance2() {
        // Arrange
        setupWithValidZipcode();
        final VehicleType vehicleType = VehicleType.OLD_TIMER;
        final Integer yearlyDrive = 4500;

        // Act
        final InsuranceDTO insuranceDTO = insuranceCalculatorService.calculateInsurance(
                vehicleType,
                yearlyDrive,
                VALID_ZIPCODE
        );

        // Assert
        assertNotNull(insuranceDTO);
        assertEquals(AbstractTest.getBigDecimal(1100.00), insuranceDTO.getYearlyPrice());
        assertEquals(AbstractTest.getBigDecimal(91.67), insuranceDTO.getMonthlyPrice());
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
                () -> insuranceCalculatorService.calculateInsurance(
                        vehicleType,
                        yearlyDrive,
                        INVALID_ZIPCODE
                )
        );
    }
}
