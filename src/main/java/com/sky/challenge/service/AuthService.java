package com.sky.challenge.service;

import com.sky.challenge.dto.request.LoginRequestDTO;
import com.sky.challenge.dto.response.LoginResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorMessage;
import com.sky.challenge.repository.UserRepositoryInterface;
// import com.sky.challenge.security.JWTService;
import com.sky.challenge.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryInterface userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtTokenService;

    public LoginResponseDTO login(LoginRequestDTO request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_CREDENTIALS);
        }

        return new LoginResponseDTO(jwtTokenService.generateToken(user.get()));
    }
}
