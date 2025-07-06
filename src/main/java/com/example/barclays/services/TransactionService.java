package com.example.barclays.services;

import com.example.barclays.domain.entities.Account;
import com.example.barclays.domain.entities.Transaction;
import com.example.barclays.exceptions.NotEnoughFundsException;
import com.example.barclays.exceptions.TransactionAmountLimit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {
    Transaction save(Transaction transaction) throws NotEnoughFundsException, TransactionAmountLimit;

    List<Transaction> findAllByAccountId(Long accountId);

    Optional<Transaction> findById(Long accoundId);
}
