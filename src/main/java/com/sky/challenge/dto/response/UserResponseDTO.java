package com.sky.challenge.dto.response;

import com.sky.challenge.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class UserResponseDTO extends BaseResponseDTO {

    private final String name;
    private final String email;

    public UserResponseDTO(User user) {
        super(user);
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
