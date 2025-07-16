package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.Authority;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.exception.EmailAlreadyExistsException;
import com.scopevisio.praemiepro.repository.AuthorityRepository;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.security.AuthorityConstants;
import com.scopevisio.praemiepro.security.SecurityUtils;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import com.scopevisio.praemiepro.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(SecurityUtils.extractPrincipal())
                .flatMap(userRepository::findOneWithAuthoritiesByEmailIgnoreCase);
    }

    public Optional<UserDTO> findUserDTOById(final Long id) {
        return userRepository.findById(id).map(userMapper::userToUserDTO);
    }

    public List<UserDTO> findCustomerUsers() {
        return userMapper.usersToUserDTOs(
                userRepository.findAllCustomers()
        );
    }

    public UserDTO registerUser(final String firstName,
                                final String lastName,
                                final String email,
                                final String password) {
        userRepository.findOneWithAuthoritiesByEmailIgnoreCase(email).ifPresent(
                user -> {
                    throw new EmailAlreadyExistsException(user.getEmail());
                }
        );

        final User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setActivated(true);

        final String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);

        final Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthorityConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        return userMapper.userToUserDTO(
                userRepository.save(newUser)
        );
    }
}
