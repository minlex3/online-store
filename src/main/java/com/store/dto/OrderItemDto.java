package com.store.dto;

public record OrderItemDto(
        Long id,
        ProductDto product,
        int quantity,
        Double price
) {
}
