package com.store.service;

import com.store.dto.CartDto;
import com.store.entity.Cart;
import com.store.entity.Product;
import com.store.mapper.CartMapper;
import com.store.repository.CartRepository;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartMapper cartMapper;

    public List<CartDto> findAll() {
        return cartRepository.findAll().stream()
                .map(cartMapper::toCartDto)
                .collect(Collectors.toList());
    }

    public void addToCart(Long productId) {
        Optional<Cart> cartProduct = cartRepository.findCartByProductId(productId);
        if (cartProduct.isPresent()) {
            Cart c = cartProduct.get();
            c.setQuantity(c.getQuantity() + 1);
            cartRepository.save(c);
        } else {
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent()) {
                Cart c = new Cart();
                c.setProduct(product.get());
                c.setQuantity(1);
                cartRepository.save(c);
            }
        }
    }

    public void removeFromCart(Long productId) {
        Optional<Cart> cartProduct = cartRepository.findCartByProductId(productId);
        Optional<Product> product = productRepository.findById(productId);
        if (cartProduct.isPresent()) {
            Cart c = cartProduct.get();
            if (c.getQuantity() == 1) {
                Product p = product.get();
                p.setCart(null);
                cartRepository.delete(c);
            } else {
                c.setQuantity(c.getQuantity() - 1);
                cartRepository.save(c);
            }
        }
    }
}
