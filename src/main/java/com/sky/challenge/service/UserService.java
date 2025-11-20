package com.sky.challenge.service;

import com.sky.challenge.dto.request.CreateUserRequestDTO;
import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorMessage;
import com.sky.challenge.repository.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryInterface userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO create(CreateUserRequestDTO request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.USER_FOUND);
        }

        User user = new User(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getName()
        );

        this.userRepository.save(user);

        return new UserResponseDTO(user);
    }


}
