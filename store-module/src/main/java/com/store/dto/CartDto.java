package com.store.dto;

public record CartDto(
        Long id,
        ProductDto product,
        int quantity,
        Long userId
) {
}
