package com.store.mapper;

import com.store.dto.OrderItemDto;
import com.store.dto.ProductDto;
import com.store.entity.OrderItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderItemMapperTest {

    @InjectMocks
    private OrderItemMapper orderItemMapper;

    @Test
    public void toOrderItemDto() {
        ProductDto productDto = new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0);
        OrderItem orderItem = new OrderItem(10L, 20L, 30L, 2, 12.34);

        OrderItemDto orderItemDto = orderItemMapper.toOrderItemDto(orderItem, productDto);

        Assertions.assertNotNull(orderItemDto);
        Assertions.assertEquals(orderItem.getId(), orderItemDto.id());
        Assertions.assertEquals(orderItem.getQuantity(), orderItemDto.quantity());
        Assertions.assertEquals(orderItem.getPrice(), orderItemDto.price());
        Assertions.assertEquals(20L, orderItemDto.product().id());
    }

    @Test
    public void toOrderItem() {
        ProductDto productDto = new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0);
        OrderItemDto orderItemDto = new OrderItemDto(10L, productDto, 2, 12.34);

        OrderItem orderItem = orderItemMapper.toOrderItem(orderItemDto);

        Assertions.assertNotNull(orderItem);
        Assertions.assertEquals(orderItemDto.id(), orderItem.getId());
        Assertions.assertEquals(orderItemDto.quantity(), orderItem.getQuantity());
        Assertions.assertEquals(orderItemDto.price(), orderItem.getPrice());
        Assertions.assertEquals(productDto.id(), orderItem.getProductId());
    }
}
