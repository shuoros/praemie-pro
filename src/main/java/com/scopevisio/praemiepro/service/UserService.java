package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import com.scopevisio.praemiepro.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public Optional<User> getCurrentUser() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(
                extractPrincipal(securityContext.getAuthentication())
        ).flatMap(userRepository::findOneWithAuthoritiesByEmailIgnoreCase);
    }

    public Optional<UserDTO> findUserDTOById(final Long id) {
        return userRepository.findById(id).map(userMapper::userToUserDTO);
    }

    public List<UserDTO> findCustomerUsers() {
        return userMapper.usersToUserDTOs(
                userRepository.findAllCustomers()
        );
    }

    private String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
}
