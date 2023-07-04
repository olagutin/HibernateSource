package org.hibernateSource.controllers;

import lombok.RequiredArgsConstructor;
import org.hibernateSource.payload.response.MessageResponse;
import org.hibernateSource.repository.RoleRepository;
import org.hibernateSource.repository.UserRepository;
import org.hibernateSource.models.ERole;
import org.hibernateSource.models.Role;
import org.hibernateSource.models.User;
import org.hibernateSource.services.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class RegistarationController {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping("registration")
    public ModelAndView registration() {
        return new ModelAndView("registration");
    }

    @PostMapping("registration")
    public ResponseEntity<?> addUser(User user, Map<String, Object> model) {

        user.setEmail("2@mail.com");
        Set<String> strRoles = Collections.singleton("USER");
        Set<Role> roles = new HashSet<>();

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }


        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
