package com.sky.challenge.controller;

import com.sky.challenge.dto.request.LoginRequestDTO;
import com.sky.challenge.dto.response.LoginResponseDTO;
import com.sky.challenge.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class AuthController {

    private final AuthService service;

    @PostMapping()
    public ResponseEntity<LoginResponseDTO> register(@Validated @RequestBody LoginRequestDTO request) {
        var response = service.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
