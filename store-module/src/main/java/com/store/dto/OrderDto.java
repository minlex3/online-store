package com.store.dto;

import java.util.List;

public record OrderDto(
        Long id,
        Double totalAmount,
        String status,
        List<OrderItemDto> orderItems
) {
}
