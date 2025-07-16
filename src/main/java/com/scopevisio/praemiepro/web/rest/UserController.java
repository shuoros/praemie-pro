package com.scopevisio.praemiepro.web.rest;

import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.security.IsAdmin;
import com.scopevisio.praemiepro.security.IsUser;
import com.scopevisio.praemiepro.service.UserService;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.service.dto.UserDTO;
import com.scopevisio.praemiepro.web.vm.CalculateVM;
import com.scopevisio.praemiepro.web.vm.UserVM;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserVM userVM) {
        final UserDTO userDTO = userService.registerUser(
                userVM.getFirstName(),
                userVM.getLastName(),
                userVM.getEmail(),
                userVM.getPassword()
        );

        return ResponseEntity
                .created(URI.create("/api/users/" + userDTO.getId()))
                .body(userDTO);
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
