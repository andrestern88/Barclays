package com.example.barclays.controllers;

import com.example.barclays.domain.dto.AccountDTO;
import com.example.barclays.domain.entities.Account;
import com.example.barclays.domain.entities.User;
import com.example.barclays.mappers.Mapper;
import com.example.barclays.security.JwtUtil;
import com.example.barclays.services.AccountService;
import com.example.barclays.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    private AccountService accountService;

    private UserService userService;

    private Mapper<Account, AccountDTO> mapper;

    @Autowired
    JwtUtil jwtUtils;

    public AccountController(AccountService accountService, UserService userService, Mapper<Account, AccountDTO> mapper) {
        this.accountService = accountService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping(path = "v1/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO,
                                    @RequestHeader(value = "Authorization") String jwtToken){
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User user = userService.findByUsername(usernameFromToken);
        Account account = mapper.mapTo(accountDTO);
        account.setAccountOwner(user);
        Account savedAccount = accountService.save(account);
        return new ResponseEntity<>(mapper.mapFrom(savedAccount), HttpStatus.CREATED);
    }

    @GetMapping(path = "v1/accounts")
    public List<AccountDTO> getAccounts(@RequestHeader(value = "Authorization") String jwtToken) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User user = userService.findByUsername(usernameFromToken);
        final List<Account> allAccounts = accountService.findAllByUserId(user.getId());
        return allAccounts.stream().map(mapper::mapFrom).collect(Collectors.toList());
    }

    @GetMapping(path = "v1/accounts/{accountId}")
    public ResponseEntity<?> fetchAccountById(@PathVariable("accountId") Long accountId, @RequestHeader(value = "Authorization") String jwtToken ) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User user = userService.findByUsername(usernameFromToken);
        final Optional<Account> account = accountService.findById(accountId);
        if(!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account was not found");
        } else if(!account.get().getAccountOwner().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        AccountDTO dto = mapper.mapFrom(account.get());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping(path = "v1/accounts/{accountId}")
    public ResponseEntity<?> updateAccountByID(@PathVariable("accountId") Long accountId,
                                            @RequestBody AccountDTO accountDto,
                                            @RequestHeader(value = "Authorization") String jwtToken) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User byUsername = userService.findByUsername(usernameFromToken);
        final Optional<Account> account = accountService.findById(accountId);
        if(!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account was not found");
        }
        if(!account.get().getAccountOwner().getId().equals(byUsername.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        Account updatedAccount = mapper.mapTo(accountDto);
        Account savedaccount = accountService.update(accountId, updatedAccount);
        return new ResponseEntity<>(mapper.mapFrom(savedaccount), HttpStatus.OK);
    }

    @DeleteMapping(path = "v1/accounts/{accountId}")
    public ResponseEntity deleteAccountByID(@PathVariable("accountId") Long accountId,
                                         @RequestHeader(value = "Authorization") String jwtToken) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User byUsername = userService.findByUsername(usernameFromToken);
        final Optional<Account> account = accountService.findById(accountId);
        if(!account.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account was not found");
        }
        if(!account.get().getAccountOwner().getId().equals(byUsername.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }

        accountService.delete(accountId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
