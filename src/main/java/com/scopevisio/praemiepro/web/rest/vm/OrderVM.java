package com.scopevisio.praemiepro.web.rest.vm;

import com.scopevisio.praemiepro.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderVM extends CalculateVM {

    @NotNull
    private User user;
}
