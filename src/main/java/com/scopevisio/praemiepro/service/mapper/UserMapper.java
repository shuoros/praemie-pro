package com.scopevisio.praemiepro.service.mapper;

import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {

    public List<UserDTO> usersToUserDTOs(final List<User> users) {
        return users.stream().map(this::userToUserDTO).toList();
    }

    public UserDTO userToUserDTO(final User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
