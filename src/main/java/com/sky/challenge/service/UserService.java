package com.sky.challenge.service;

import com.sky.challenge.dto.request.CreateUserRequestDTO;
import com.sky.challenge.dto.request.UpdateUserRequestDTO;
import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorMessage;
import com.sky.challenge.repository.UserRepositoryInterface;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService extends BaseService {

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryInterface userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO create(CreateUserRequestDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.USER_FOUND + request.getEmail());
        }

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName());

        this.userRepository.save(user);

        return new UserResponseDTO(user);
    }

    public UserResponseDTO get(UUID id) {
        User user = checkIfUserExists(id);

        return new UserResponseDTO(user);
    }

    public UserResponseDTO update(UUID id, UpdateUserRequestDTO request) {
        User user = checkIfUserExists(id);

        user.updateDetails(request.getName());
        userRepository.save(user);

        return new UserResponseDTO(user);
    }

    public UserResponseDTO delete(UUID id) {
        User user = checkIfUserExists(id);

        this.userRepository.deleteById(id);

        return new UserResponseDTO(user);
    }
}
