package com.example.barclays.services;

import com.example.barclays.domain.entities.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {

    Account save(Account user);

    List<Account> findAllByUserId(String userId);

    Optional<Account> findById(Long accoundId);

    Account update(Long accountId, Account user);

    void delete(Long accountId);
}
