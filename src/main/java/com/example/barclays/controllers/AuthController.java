package com.example.barclays.controllers;

import com.example.barclays.domain.dto.UserDTO;
import com.example.barclays.domain.entities.User;
import com.example.barclays.repositories.UserRepository;
import com.example.barclays.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }
    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Error: Username is already taken!";
        }

        User newUser = new User(
                user.getId(),
                user.getUsername(),
                encoder.encode(user.getPassword()),
                user.getFirstnames(),
                user.getLastnames(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getEmail(),
                new Date(),
                new Date()
        );

        userRepository.save(newUser);
        return "User registered successfully!";
    }
}
