package com.store.mapper;

import com.store.dto.CartDto;
import com.store.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    @Autowired
    ProductMapper productMapper;

    public CartDto toCartDto(Cart cart) {
        return new CartDto(
                cart.getId(),
                productMapper.toProductDto(cart.getProduct()),
                cart.getQuantity()
        );
    }

    public Cart toCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setId(cartDto.id());
        cart.setProduct(productMapper.toProduct(cartDto.product()));
        cart.setQuantity(cartDto.quantity());
        return cart;
    }
}
