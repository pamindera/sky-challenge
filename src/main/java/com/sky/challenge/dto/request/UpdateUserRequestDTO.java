package com.sky.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Setter;

@Setter
public class UpdateUserRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String name;

    public String getName() {
        return name == null ? null : name.trim();
    }
}
