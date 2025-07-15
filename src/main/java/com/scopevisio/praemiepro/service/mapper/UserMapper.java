package com.scopevisio.praemiepro.service.mapper;

import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDTO userToUserDTO(final User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
