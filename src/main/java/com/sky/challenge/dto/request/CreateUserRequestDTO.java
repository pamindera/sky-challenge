package com.sky.challenge.dto.request;

import com.sky.challenge.error.ErrorMessage;
import jakarta.validation.constraints.*;
import lombok.Setter;

@Setter
public class CreateUserRequestDTO {

    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$", message = ErrorMessage.INVALID_PASSWORD)
    private String password;

    @Size(max = 255)
    private String name;

    public String getEmail() {
        return email.trim();
    }

    public String getPassword() {
        return password.trim();
    }

    public String getName() {
        return name == null ? null : name.trim();
    }
}
