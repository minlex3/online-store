package com.store.mapper;

import com.store.dto.OrderItemDto;
import com.store.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    @Autowired
    ProductMapper productMapper;

    public OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                productMapper.toProductDto(orderItem.getProduct()),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    public OrderItem toOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDto.id());
        orderItem.setProduct(productMapper.toProduct(orderItemDto.product()));
        orderItem.setQuantity(orderItemDto.quantity());
        orderItem.setPrice(orderItemDto.price());
        return orderItem;
    }
}
