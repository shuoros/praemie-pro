package com.scopevisio.praemiepro.service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class JwtDTO implements Serializable {

    private String token;
}
