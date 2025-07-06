package com.example.barclays.controllers;

import com.example.barclays.domain.dto.TransactionDTO;
import com.example.barclays.domain.entities.Account;
import com.example.barclays.domain.entities.Transaction;
import com.example.barclays.domain.entities.User;
import com.example.barclays.exceptions.NotEnoughFundsException;
import com.example.barclays.exceptions.TransactionAmountLimit;
import com.example.barclays.mappers.Mapper;
import com.example.barclays.security.JwtUtil;
import com.example.barclays.services.AccountService;
import com.example.barclays.services.TransactionService;
import com.example.barclays.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TransactionController {

    private AccountService accountService;

    private UserService userService;

    private TransactionService transactionService;

    private Mapper<Transaction, TransactionDTO> mapper;
    @Autowired
    JwtUtil jwtUtils;

    public TransactionController(AccountService accountService, UserService userService, TransactionService transactionService, Mapper<Transaction, TransactionDTO> mapper) {
        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.mapper = mapper;
    }

    @PostMapping(path = "v1/accounts/{accountId}/transactions")
    public ResponseEntity createTransaction(@PathVariable("accountId") Long accountId,
                                    @RequestBody TransactionDTO transactionDTO,
                                    @RequestHeader(value = "Authorization") String jwtToken){
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User user = userService.findByUsername(usernameFromToken);
        Optional<Account> account = accountService.findById(accountId);
        if(!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account was not found");
        }
        if(!account.get().getAccountOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        Transaction transaction = mapper.mapTo(transactionDTO);
        transaction.setAccount(account.get());
        Transaction savedTransaction = null;
        try {
            savedTransaction = transactionService.save(transaction);
        } catch (NotEnoughFundsException | TransactionAmountLimit e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
        return new ResponseEntity<>(mapper.mapFrom(savedTransaction), HttpStatus.OK);
    }

    @GetMapping(path = "v1/accounts/{accountId}/transactions")
    public ResponseEntity getTransactions(@PathVariable("accountId") Long accountId,
                                           @RequestHeader(value = "Authorization") String jwtToken) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User user = userService.findByUsername(usernameFromToken);
        final Optional<Account> account = accountService.findById(accountId);
        if(!account.isPresent()) {
            //throw new RuntimeException("Account was not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account was not found");
        } else if(!account.get().getAccountOwner().getId().equals(user.getId())){
            //throw new RuntimeException("The user is not allowed to access the transaction");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        final List<Transaction> allTransaction = transactionService.findAllByAccountId(accountId);
        final List<TransactionDTO> collect = allTransaction.stream().map(mapper::mapFrom).collect(Collectors.toList());

        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @GetMapping(path = "v1/accounts/{accountId}/transactions/{transactionId}")
    public ResponseEntity<?> fetchTransactionById(@PathVariable("accountId") Long accountId,
                                                  @PathVariable("transactionId") Long transactionId,
                                                  @RequestHeader(value = "Authorization") String jwtToken ) {
        final String usernameFromToken = jwtUtils.getUsernameFromToken(jwtUtils.removeBearer(jwtToken));
        final User user = userService.findByUsername(usernameFromToken);
        final Optional<Account> account = accountService.findById(accountId);
        if(!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account was not found");
        } else if(!account.get().getAccountOwner().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not allowed to access the transaction");
        }
        Optional<Transaction> transaction = transactionService.findById(transactionId);
        if(!transaction.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction was not found");
        } else if(!transaction.get().getAccount().getId().equals(account.get().getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The transaction was not made against this account");
        }
        TransactionDTO dto = mapper.mapFrom(transaction.get());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
