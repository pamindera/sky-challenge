package com.sky.challenge.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import java.util.Optional;
import lombok.Setter;

@Setter
public class ListRequestDTO {

    @PositiveOrZero
    private Integer page;

    public int getPage() {
        return Optional.ofNullable(page).orElse(0);
    }
}
