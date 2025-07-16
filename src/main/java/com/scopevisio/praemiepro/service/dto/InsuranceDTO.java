package com.scopevisio.praemiepro.service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDTO implements Serializable {

    private BigDecimal yearlyPrice;

    private BigDecimal monthlyPrice;
}
