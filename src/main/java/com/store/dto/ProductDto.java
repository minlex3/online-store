package com.store.dto;

public record ProductDto(
        Long id,
        String name,
        String description,
        Double price,
        String imageUrl,
        int stock
) {
}
