package com.store.dto;

import java.util.List;

public record PageResponse(
        List<ProductDto> data,
        Long totalElements
) {
}
