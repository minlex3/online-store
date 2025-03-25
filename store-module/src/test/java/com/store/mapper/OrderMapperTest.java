package com.store.mapper;


import com.store.dto.OrderDto;
import com.store.dto.OrderItemDto;
import com.store.dto.ProductDto;
import com.store.entity.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    @InjectMocks
    private OrderMapper orderMapper;

    @Test
    public void toOrderDto() {
        ProductDto productDto = new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0);
        OrderItemDto orderItemDto = new OrderItemDto(10L, productDto, 2, 12.34);

        Order order = new Order(10L, 12.34, "Paid");

        OrderDto orderDto = orderMapper.toOrderDto(order, List.of(orderItemDto));

        Assertions.assertNotNull(orderDto);
        Assertions.assertEquals(order.getId(), orderDto.id());
        Assertions.assertEquals(order.getTotalAmount(), orderDto.totalAmount());
        Assertions.assertEquals(order.getStatus(), orderDto.status());
        Assertions.assertEquals(10L, orderDto.orderItems().getFirst().id());
        Assertions.assertEquals(2, orderDto.orderItems().getFirst().quantity());
    }

    @Test
    public void toOrder() {
        ProductDto productDto = new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0);
        OrderItemDto orderItemDto = new OrderItemDto(10L, productDto, 2, 12.34);
        OrderDto orderDto = new OrderDto(10L, 12.34, "Paid", List.of(orderItemDto));

        Order order = orderMapper.toOrder(orderDto);

        Assertions.assertNotNull(order);
        Assertions.assertEquals(orderDto.id(), order.getId());
        Assertions.assertEquals(orderDto.totalAmount(), order.getTotalAmount());
        Assertions.assertEquals(orderDto.status(), order.getStatus());
    }
}
