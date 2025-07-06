package com.example.barclays.repositories;

import com.example.barclays.domain.entities.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AddressRepository extends CrudRepository<Address, Long> {

    Optional<Address> findAddressByUser_Id(String userId);
}
