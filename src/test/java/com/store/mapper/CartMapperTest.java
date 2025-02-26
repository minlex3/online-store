package com.store.mapper;

import com.store.dto.CartDto;
import com.store.dto.ProductDto;
import com.store.entity.Cart;
import com.store.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CartMapperTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CartMapper cartMapper;

    @Test
    void getCartDto() {
        when(productMapper.toProductDto(any())).thenReturn(
                new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0)
        );

        Cart cart = new Cart(
                10L,
                new Product(),
                2
        );
        CartDto cartDto = cartMapper.toCartDto(cart);

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
        when(productMapper.toProduct(any())).thenReturn(
                new Product(20L, "product", "desc", 10.12, "url", 3, null)
        );

        CartDto cartDto = new CartDto(
                10L,
                new ProductDto(20L, "product", "desc", 10.12, "url", 3, 0),
                2);

        Cart cart = cartMapper.toCart(cartDto);

        Assertions.assertNotNull(cart);
        Assertions.assertEquals(10L, cart.getId());
        Assertions.assertEquals(2, cart.getQuantity());
        Assertions.assertEquals(20L, cart.getProduct().getId());
        Assertions.assertEquals("product", cart.getProduct().getName());
        Assertions.assertEquals("desc", cart.getProduct().getDescription());
        Assertions.assertEquals(10.12, cart.getProduct().getPrice());
        Assertions.assertEquals("url", cart.getProduct().getImageUrl());
        Assertions.assertEquals(3, cart.getProduct().getStock());
    }
}
