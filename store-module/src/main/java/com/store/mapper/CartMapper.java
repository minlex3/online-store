package com.store.mapper;

import com.store.dto.CartDto;
import com.store.dto.ProductDto;
import com.store.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartDto toCartDto(Cart cart, ProductDto productDto) {
        return new CartDto(
                cart.getId(),
                productDto,
                cart.getQuantity()
        );
    }

    public Cart toCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setId(cartDto.id());
        cart.setProductId(cartDto.product().id());
        cart.setQuantity(cartDto.quantity());
        return cart;
    }
}
