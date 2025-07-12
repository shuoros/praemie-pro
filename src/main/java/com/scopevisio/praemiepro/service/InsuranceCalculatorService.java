package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.State;
import com.scopevisio.praemiepro.domain.SystemParameter;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.exception.WrongZipcodeException;
import com.scopevisio.praemiepro.repository.StateRepository;
import com.scopevisio.praemiepro.service.dto.InsuranceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InsuranceCalculatorService {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private SystemParameterService systemParameterService;

    public InsuranceDTO calculateInsurance(final VehicleType vehicleType, final Integer yearlyDrive, final String zipcode) {
        final float yearlyDriveFactor = calculateYearlyDriveFactor(yearlyDrive);
        final float zipcodeFactor = findZipcodeFactor(zipcode);
        final float yearlyInsuranceFactor = calculateInsuranceFactor(vehicleType.getFactor(), yearlyDriveFactor, zipcodeFactor);

        final BigDecimal yearlyInsurance = calculateYearlyInsurance(yearlyInsuranceFactor);
        final BigDecimal monthlyInsurance = calculateMonthlyInsurance(yearlyInsurance);

        return InsuranceDTO.builder()
                .monthlyPrice(monthlyInsurance)
                .yearlyPrice(yearlyInsurance)
                .build();
    }

    private BigDecimal calculateMonthlyInsurance(final BigDecimal yearlyInsurance) {
        return yearlyInsurance.divide(
                BigDecimal.valueOf(12),
                2,
                RoundingMode.HALF_UP
        );
    }

    private BigDecimal calculateYearlyInsurance(final float yearlyInsuranceFactor) {
        final SystemParameter systemParameter = systemParameterService.getSystemParameter();

        return systemParameter.getBaseInsurancePrice()
                .multiply(BigDecimal.valueOf(yearlyInsuranceFactor))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private float calculateInsuranceFactor(final float vehicleType, final float yearlyDriveFactor, final float zipcodeFactor) {
        return vehicleType * yearlyDriveFactor * zipcodeFactor;
    }

    private float findZipcodeFactor(final String zipcode) {
        final State state = stateRepository.findOneByRegions_zipcode(zipcode).orElseThrow(WrongZipcodeException::new);

        return state.getInsuranceFactor();
    }

    private float calculateYearlyDriveFactor(final Integer yearlyDrive) {
        if(yearlyDrive <= 5000) {
            return 0.5f;
        } else if (yearlyDrive <= 10000) {
            return 1f;
        } else if (yearlyDrive <= 20000) {
            return 1.5f;
        }
        return 2f;
    }
}
