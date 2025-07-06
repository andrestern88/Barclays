package com.example.barclays.services;

import com.example.barclays.domain.entities.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    Address save(Address address);

    Address update(Address address);
}
