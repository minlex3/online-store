package com.store.mapper;

import com.store.dto.CartDto;
import com.store.dto.ProductDto;
import com.store.entity.Cart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CartMapperTest {

    @InjectMocks
    private CartMapper cartMapper;

    @Test
    void getCartDto() {
        ProductDto productDto = new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0);
        Cart cart = new Cart(
                10L,
                20L,
                2,
                1L
        );
        CartDto cartDto = cartMapper.toCartDto(cart, productDto);

        Assertions.assertNotNull(cartDto);
        Assertions.assertEquals(10L, cartDto.id());
        Assertions.assertEquals(2, cartDto.quantity());
        Assertions.assertEquals(20L, cartDto.product().id());
        Assertions.assertEquals("product", cartDto.product().name());
        Assertions.assertEquals("desc", cartDto.product().description());
        Assertions.assertEquals(10.12, cartDto.product().price());
        Assertions.assertEquals("url", cartDto.product().imageUrl());
        Assertions.assertEquals(3, cartDto.product().stock());
    }

    @Test
    void getCart() {
        CartDto cartDto = new CartDto(
                10L,
                new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0),
                2,
                1L);

        Cart cart = cartMapper.toCart(cartDto);

        Assertions.assertNotNull(cart);
        Assertions.assertEquals(10L, cart.getId());
        Assertions.assertEquals(2, cart.getQuantity());
        Assertions.assertEquals(20L, cart.getProductId());
    }
}
