package com.store.mapper;

import com.store.dto.OrderDto;
import com.store.dto.OrderItemDto;
import com.store.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderDto toOrderDto(Order order, List<OrderItemDto> orderItemDtoList) {
        return new OrderDto(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getUserId(),
                orderItemDtoList
        );
    }

    public Order toOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.id());
        order.setTotalAmount(orderDto.totalAmount());
        order.setStatus(orderDto.status());
        order.setUserId(orderDto.userId());
        return order;
    }
}
