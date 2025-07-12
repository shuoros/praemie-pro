package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.config.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class AuthenticateService {

    private static final String AUTHORITIES_CLAIM = "auth";

    private static final String USER_EMAIL_CLAIM = "userEmail";

    @Value("${application.security.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    @Value("${application.security.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;

    @Autowired
    private JwtEncoder jwtEncoder;

    public String createToken(final Authentication authentication, final boolean rememberMe) {
        final String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        final Instant now = Instant.now();
        final Instant validity = rememberMe
                ? now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS)
                : now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);

        final JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim(AUTHORITIES_CLAIM, authorities);
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User user) {
            builder.claim(USER_EMAIL_CLAIM, user.getUsername());
        }

        final JwsHeader jwsHeader = JwsHeader.with(JwtConfiguration.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }
}
