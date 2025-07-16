package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.security.SecurityUtils;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import com.scopevisio.praemiepro.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
}
