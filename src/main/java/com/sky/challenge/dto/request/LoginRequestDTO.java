package com.sky.challenge.dto.request;

import com.sky.challenge.error.ErrorMessage;
import jakarta.validation.constraints.*;
import lombok.Setter;

@Setter
public class LoginRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String password;

    public String getEmail() {
        return email.trim();
    }
    public String getPassword() {
        return password.trim();
    }
}
