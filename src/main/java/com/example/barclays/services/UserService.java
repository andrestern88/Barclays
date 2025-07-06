package com.example.barclays.services;

import com.example.barclays.domain.entities.User;
import com.example.barclays.exceptions.UserHasAccountException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(String userId);

    User findByUsername(String username);

    boolean isExists(String userId);

    User update(String userId, User user);

    void delete(String userId) throws UserHasAccountException;
}
