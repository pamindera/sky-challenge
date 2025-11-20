package com.sky.challenge.dto.response;

import com.sky.challenge.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class BaseResponseDTO {

    private final String id;
    private final String createdAt;
    private final String updatedAt;

    public BaseResponseDTO(BaseEntity entity) {
        this.id = entity.getId().toString();
        this.createdAt = entity.getCreatedAt().toString();
        this.updatedAt = entity.getUpdatedAt().toString();
    }
}
