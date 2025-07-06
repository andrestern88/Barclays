package com.example.barclays.services;

import com.example.barclays.domain.entities.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    Account save(Account account);

    List<Account> findAllByUserId(String accountId);

    Optional<Account> findById(Long accoundId);

    Account update(Long accountId, Account account);

    void delete(Long accountId);
}
