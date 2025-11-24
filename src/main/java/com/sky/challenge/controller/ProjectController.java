package com.sky.challenge.controller;

import com.sky.challenge.dto.request.CreateProjectRequestDTO;
import com.sky.challenge.dto.request.ListRequestDTO;
import com.sky.challenge.dto.response.ListResponseDTO;
import com.sky.challenge.dto.response.ProjectResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.service.ProjectService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/{id}/project")
public class ProjectController {

    private final ProjectService service;

    @PostMapping("")
    @PreAuthorize("#id == #user.id")
    public ResponseEntity<ProjectResponseDTO> executeCreate(
            @Parameter(description = "Authorization token", example = "Bearer <token>")
                    @RequestHeader(value = "Authorization", required = true)
                    String authorization,
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Validated @RequestBody CreateProjectRequestDTO request) {
        var response = service.create(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    @PreAuthorize("#id == #user.id")
    public ResponseEntity<ListResponseDTO<?, ?>> executeGet(
            @Parameter(description = "Authorization token", example = "Bearer <token>")
                    @RequestHeader(value = "Authorization", required = true)
                    String authorization,
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Validated @ModelAttribute ListRequestDTO request) {
        var response = service.list(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
