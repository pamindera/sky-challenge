package com.sky.challenge.controller;

import com.sky.challenge.dto.request.CreateUserRequestDTO;
import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;

    @PostMapping()
    public ResponseEntity<UserResponseDTO> register(@Validated @RequestBody CreateUserRequestDTO request) {
        var response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}