package com.scopevisio.praemiepro.web.rest;

import com.scopevisio.praemiepro.service.InsuranceCalculatorService;
import com.scopevisio.praemiepro.service.dto.InsuranceDTO;
import com.scopevisio.praemiepro.web.rest.vm.CalculateVM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/insurance")
public class InsuranceCalculatorController {

    @Autowired
    private InsuranceCalculatorService insuranceCalculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<InsuranceDTO> calculateInsurance(@Valid @RequestBody CalculateVM calculateVM) {
        final InsuranceDTO insuranceDTO = insuranceCalculatorService.calculateInsurance(
                calculateVM.getVehicleType(),
                calculateVM.getYearlyDrive(),
                calculateVM.getZipcode()
        );

        return ResponseEntity.ok(insuranceDTO);
    }
}
