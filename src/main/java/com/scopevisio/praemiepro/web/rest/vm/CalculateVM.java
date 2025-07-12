package com.scopevisio.praemiepro.web.rest.vm;

import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CalculateVM {

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    @Min(0)
    private Integer yearlyDrive;

    @NotNull
    @Size(min = 5, max = 5)
    private String zipcode;
}
