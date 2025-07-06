package com.example.barclays.repositories;

import com.example.barclays.domain.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    List<Account> findAccountsByAccountOwner_Id(String userId);
}
