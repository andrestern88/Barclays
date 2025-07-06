package com.example.barclays.controllers;

import com.example.barclays.domain.dto.UserDTO;
import com.example.barclays.domain.entities.User;
import com.example.barclays.mappers.Mapper;
import com.example.barclays.security.JwtUtil;
import com.example.barclays.services.AccountService;
import com.example.barclays.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;

    private AccountService accountService;

    private Mapper<User, UserDTO> mapper;

    @Autowired
    JwtUtil jwtUtils;

    public UserController(UserService userService, AccountService accountService, Mapper<User, UserDTO> mapper) {
        this.userService = userService;
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @PostMapping(path = "v1/users")
    public UserDTO createUser(@RequestBody UserDTO userDTO){
        User user = mapper.mapTo(userDTO);
        user.setCreatedTimestamp(new Date());
        user.setUpdatedTimestamp(new Date());
        User savedUser = userService.save(user);
        return mapper.mapFrom(savedUser);
    }

    @GetMapping(path = "v1/users")
    public List<UserDTO> getUsers() {
        final List<User> allUsers = userService.findAll();
        return allUsers.stream().map(mapper::mapFrom).collect(Collectors.toList());
    }

    @GetMapping(path = "v1/users/{userId}")
    public ResponseEntity<?> fetchUserByID(@PathVariable("userId") String userId, @RequestHeader(value = "Authorization") String jwtToken ) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User byUsername = userService.findByUsername(usernameFromToken);
        if(!byUsername.getId().equals(userId)) {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        Optional<User> foundUser = userService.findById(userId);
        if (foundUser.isPresent()) {
            UserDTO dto = mapper.mapFrom(foundUser.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found");
        }
    }

    @PatchMapping(path = "v1/users/{userId}")
    public ResponseEntity<?> updateUserByID(@PathVariable("userId") String userId,
                                              @RequestBody UserDTO userDto,
                                            @RequestHeader(value = "Authorization") String jwtToken) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User byUsername = userService.findByUsername(usernameFromToken);
        if(!byUsername.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        if(!userService.isExists(userId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = mapper.mapTo(userDto);
        User savedUser = userService.update(userId, user);
        return new ResponseEntity<>(mapper.mapFrom(savedUser), HttpStatus.OK);
    }

    @DeleteMapping(path = "v1/users/{userId}")
    public ResponseEntity deleteUserByID(@PathVariable("userId") String userId,
                                         @RequestHeader(value = "Authorization") String jwtToken) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User byUsername = userService.findByUsername(usernameFromToken);
        if(!byUsername.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        if(!userService.isExists(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found");
        }
        userService.delete(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
