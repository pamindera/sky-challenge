package com.sky.challenge.dto.response;


import com.sky.challenge.entity.BaseEntity;
import com.sky.challenge.entity.PagedResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ListResponseDTO<T extends BaseEntity, S extends BaseResponseDTO> {

    private final long count;
    private final int page;
    private final List<S> items;

    public ListResponseDTO(PagedResult<T> pagedResult, Function<T, S> mapper) {
        this.count = pagedResult.count();
        this.page = pagedResult.page();
        this.items = pagedResult.items().stream().map(mapper).toList();
    }
}
