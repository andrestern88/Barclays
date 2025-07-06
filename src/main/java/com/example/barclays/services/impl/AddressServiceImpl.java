package com.example.barclays.services.impl;

import com.example.barclays.domain.entities.Address;
import com.example.barclays.repositories.AddressRepository;
import com.example.barclays.services.AddressService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address save(Address address) {
        return repository.save(address);
    }

    @Override
    public Address update(Address address) {

        return this.repository.findAddressByUser_Id(address.getUser().getId()).map(existingUser -> {
            Optional.ofNullable(address.getLine1()).ifPresent(existingUser::setLine1);
            Optional.ofNullable(address.getLine2()).ifPresent(existingUser::setLine2);
            Optional.ofNullable(address.getLine3()).ifPresent(existingUser::setLine3);
            Optional.ofNullable(address.getTown()).ifPresent(existingUser::setTown);
            Optional.ofNullable(address.getCountry()).ifPresent(existingUser::setCountry);
            Optional.ofNullable(address.getPostcode()).ifPresent(existingUser::setPostcode);
            return repository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("Address does not exist"));
    }
}
