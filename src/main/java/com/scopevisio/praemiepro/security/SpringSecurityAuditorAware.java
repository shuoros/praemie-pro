package com.scopevisio.praemiepro.security;

import com.scopevisio.praemiepro.config.Constants;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(
                Optional.ofNullable(SecurityUtils.extractPrincipal()).orElse(Constants.SYSTEM)
        );
    }
}
