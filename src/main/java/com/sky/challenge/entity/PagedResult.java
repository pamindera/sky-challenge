package com.sky.challenge.entity;

import java.util.List;

public record PagedResult<T>(long count, int page, List<T> items) {}
