package com.scopevisio.praemiepro.web.vm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginVM {

    @Email
    @NotNull
    @Size(min = 1, max = 254)
    private String email;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    private Boolean rememberMe = false;
}
