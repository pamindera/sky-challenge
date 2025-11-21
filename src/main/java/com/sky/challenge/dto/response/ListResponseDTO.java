package com.sky.challenge.dto.response;

import com.sky.challenge.entity.BaseEntity;
import java.util.List;
import java.util.function.Function;

public record ListResponseDTO<T extends BaseEntity, S extends BaseResponseDTO>(long count, int page, List<S> items) {

    public ListResponseDTO(long count, int page, List<T> results, Function<T, S> mapper) {
        this(count, page, results.stream().map(mapper).toList());
    }
}
