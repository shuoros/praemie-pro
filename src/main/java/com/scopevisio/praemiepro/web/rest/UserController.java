package com.scopevisio.praemiepro.web.rest;

import com.scopevisio.praemiepro.repository.UserRepository;
import com.scopevisio.praemiepro.security.IsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
