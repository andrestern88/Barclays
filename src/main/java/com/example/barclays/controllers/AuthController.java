package com.example.barclays.controllers;

import com.example.barclays.domain.dto.AccountDTO;
import com.example.barclays.domain.dto.UserDTO;
import com.example.barclays.domain.entities.Account;
import com.example.barclays.domain.entities.User;
import com.example.barclays.mappers.Mapper;
import com.example.barclays.repositories.UserRepository;
import com.example.barclays.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;

    @Autowired
    private Mapper<User, UserDTO> mapper;

    @PostMapping("/v1/auth/signin")
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
    @PostMapping("/v1/users")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already taken");
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
        UserDTO dto = mapper.mapFrom(newUser);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
