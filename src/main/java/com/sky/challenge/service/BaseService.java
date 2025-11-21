package com.sky.challenge.service;

import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorMessage;
import com.sky.challenge.repository.UserRepositoryInterface;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseService {

    protected final UserRepositoryInterface userRepository;

    public BaseService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    protected User checkIfUserExists(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND + id);
        }

        return user.get();
    }
}
