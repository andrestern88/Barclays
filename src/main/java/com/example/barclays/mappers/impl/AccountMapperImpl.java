package com.example.barclays.mappers.impl;

import com.example.barclays.domain.dto.AccountDTO;
import com.example.barclays.domain.entities.Account;
import com.example.barclays.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements Mapper<Account, AccountDTO> {

    private ModelMapper modelMapper;

    public AccountMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Account mapTo(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }

    @Override
    public AccountDTO mapFrom(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }
}
