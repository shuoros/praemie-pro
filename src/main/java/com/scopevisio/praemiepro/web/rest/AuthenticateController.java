package com.scopevisio.praemiepro.web.rest;

import com.scopevisio.praemiepro.service.AuthenticateService;
import com.scopevisio.praemiepro.service.dto.JwtDTO;
import com.scopevisio.praemiepro.web.vm.LoginVM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticateController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private AuthenticateService authenticateService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtDTO> authenticate(@Valid @RequestBody LoginVM loginVM) {
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getEmail(),
                loginVM.getPassword()
        );

        final Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwt = authenticateService.createToken(authentication, loginVM.getRememberMe());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);

        return new ResponseEntity<>(JwtDTO.builder().token(jwt).build(), httpHeaders, HttpStatus.OK);
    }
}
