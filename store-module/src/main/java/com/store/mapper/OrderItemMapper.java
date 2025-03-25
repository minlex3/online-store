package com.store.mapper;

import com.store.dto.OrderItemDto;
import com.store.dto.ProductDto;
import com.store.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDto toOrderItemDto(OrderItem orderItem, ProductDto productDto) {
        return new OrderItemDto(
                orderItem.getId(),
                productDto,
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    public OrderItem toOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDto.id());
        orderItem.setProductId(orderItemDto.product().id());
        orderItem.setQuantity(orderItemDto.quantity());
        orderItem.setPrice(orderItemDto.price());
        return orderItem;
    }
}
