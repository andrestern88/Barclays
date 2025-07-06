package com.example.barclays.services.impl;

import com.example.barclays.domain.entities.Account;
import com.example.barclays.domain.entities.Transaction;
import com.example.barclays.domain.enums.TransactionType;
import com.example.barclays.exceptions.NotEnoughFundsException;
import com.example.barclays.exceptions.TransactionAmountLimit;
import com.example.barclays.repositories.AccountRepository;
import com.example.barclays.repositories.TransactionRepository;
import com.example.barclays.services.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }
    @Override
    public Transaction save(Transaction transaction) throws NotEnoughFundsException, TransactionAmountLimit {
        final Account account = transaction.getAccount();
        if(transaction.getAmount() > 1000.0) {
            throw new TransactionAmountLimit("There is a limit of 1000.0 per transaction");
        }
        if(TransactionType.WITHDRAWAL.equals(transaction.getType())) {
            if(transaction.getAmount() > account.getBalance()) {
                throw new NotEnoughFundsException("There are no sufficient funds in the account for this withdrawal");
            }
        }
        account.setBalance(TransactionType.DEPOSIT.equals(transaction.getType()) ? account.getBalance() + transaction.getAmount() :
                account.getBalance() - transaction.getAmount());
        transaction.setCreatedTimestamp(new Date());
        accountRepository.save(account);
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        return repository.findTransactionByAccount_Id(accountId);
    }

    @Override
    public Optional<Transaction> findById(Long accoundId) {
        return repository.findById(accoundId);
    }


}
