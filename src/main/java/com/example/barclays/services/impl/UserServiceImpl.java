package com.example.barclays.services.impl;

import com.example.barclays.domain.entities.User;
import com.example.barclays.repositories.AccountRepository;
import com.example.barclays.repositories.UserRepository;
import com.example.barclays.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final AccountRepository accountRepository;

    public UserServiceImpl(UserRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(this.repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String userId) {
        return this.repository.findById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    @Override
    public boolean isExists(String userId) {
        return this.repository.existsById(userId);
    }

    @Override
    public User update(String userId, User user) {
        user.setId(userId);

        return this.repository.findById(userId).map(existingUser -> {
            Optional.ofNullable(user.getFirstnames()).ifPresent(existingUser::setFirstnames);
            Optional.ofNullable(user.getLastnames()).ifPresent(existingUser::setLastnames);
            Optional.ofNullable(user.getAddress()).ifPresent(existingUser::setAddress);
            Optional.ofNullable(user.getEmail()).ifPresent(existingUser::setEmail);
            Optional.ofNullable(user.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);
            existingUser.setUpdatedTimestamp(new Date());
            return repository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    @Override
    public void delete(String userId) {
        final Optional<User> user = this.repository.findById(userId);
        if(user.isPresent()) {
            if (accountRepository.findAccountsByAccountOwner_Id(userId).isEmpty()){
                this.repository.deleteById(userId);
            } else {
                throw new RuntimeException("A user cannot be deleted when they are associated with a bank account");
            }
        }
    }

}
