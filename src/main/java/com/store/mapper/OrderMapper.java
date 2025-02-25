package com.store.mapper;

import com.store.dto.OrderDto;
import com.store.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    OrderItemMapper orderItemMapper;

    public OrderDto toOrderDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderItems()
                        .stream()
                        .map(orderItemMapper::toOrderItemDto)
                        .collect(Collectors.toList())
        );
    }

    public Order toOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.id());
        order.setTotalAmount(orderDto.totalAmount());
        order.setStatus(orderDto.status());
        order.setOrderItems(
                orderDto.orderItems()
                        .stream()
                        .map(orderItemMapper::toOrderItem)
                        .collect(Collectors.toList())
        );
        return order;
    }
}
