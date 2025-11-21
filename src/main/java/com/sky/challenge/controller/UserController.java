package com.sky.challenge.controller;

import com.sky.challenge.dto.request.CreateUserRequestDTO;
import com.sky.challenge.dto.request.UpdateUserRequestDTO;
import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;

    @PostMapping("")
    public ResponseEntity<UserResponseDTO> executeCreate(@Validated @RequestBody CreateUserRequestDTO request) {
        var response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @PreAuthorize("#user.getId().toString().equals(#id.toString())")
    public ResponseEntity<UserResponseDTO> executeGet(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        var response = service.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> executeUpdate(
            @PathVariable UUID id, @Validated @RequestBody UpdateUserRequestDTO request) {
        var response = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> executeDelete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
