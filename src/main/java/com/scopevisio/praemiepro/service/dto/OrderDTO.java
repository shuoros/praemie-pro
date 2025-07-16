package com.scopevisio.praemiepro.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {

    private Long id;

    private String vehicleType;

    private String yearlyDrive;

    private String yearlyPrice;

    private String zipcode;

    private String date;

    private UserDTO user;
}
