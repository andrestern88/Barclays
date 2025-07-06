package com.example.barclays.services.impl;

import com.example.barclays.domain.entities.Account;
import com.example.barclays.domain.enums.AccountType;
import com.example.barclays.domain.enums.Currency;
import com.example.barclays.domain.enums.SortCode;
import com.example.barclays.repositories.AccountRepository;
import com.example.barclays.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }
    @Override
    public Account save(Account account) {
        Random gen = new Random();
        long randomInt = gen.nextInt(100000, 999999);
        String formattedId = "01" + randomInt;
        account.setAccountNumber(formattedId);
        account.setCreatedTimestamp(new Date());
        account.setUpdatedTimestamp(new Date());
        account.setCurrency(Currency.GBP);
        account.setBalance(0.0);
        account.setSortCode(SortCode.CENTRAL_LONDON.getLabel());
        return repository.save(account);
    }

    @Override
    public List<Account> findAllByUserId(String userId) {
        return repository.findAccountsByAccountOwner_Id(userId);
    }

    @Override
    public Optional<Account> findById(Long accoundId) {
        return repository.findById(accoundId);
    }

    @Override
    public Account update(Long accountId, Account account) {
        account.setId(accountId);

        return this.repository.findById(accountId).map(existingAccount -> {
            Optional.ofNullable(account.getAccountType()).ifPresent(existingAccount::setAccountType);
            Optional.ofNullable(account.getName()).ifPresent(existingAccount::setName);
            existingAccount.setUpdatedTimestamp(new Date());
            return repository.save(existingAccount);
        }).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    @Override
    public void delete(Long accountId) {
        repository.deleteById(accountId);
    }
}
