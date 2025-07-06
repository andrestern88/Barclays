package com.example.barclays.mappers.impl;

import com.example.barclays.domain.dto.AccountDTO;
import com.example.barclays.domain.dto.TransactionDTO;
import com.example.barclays.domain.entities.Account;
import com.example.barclays.domain.entities.Transaction;
import com.example.barclays.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapperImpl implements Mapper<Transaction, TransactionDTO> {

    private ModelMapper modelMapper;

    public TransactionMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Transaction mapTo(TransactionDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);
    }

    @Override
    public TransactionDTO mapFrom(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }
}
