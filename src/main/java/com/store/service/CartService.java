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
        Optional<Product> rawProduct = productRepository.findById(productId);
        if (rawProduct.isEmpty() || rawProduct.get().getStock() == 0) {
            return;
        }
        Product product = rawProduct.get();

        Cart cart;
        Optional<Cart> cartProduct = cartRepository.findCartByProductId(productId);

        if (cartProduct.isPresent()) {
            cart = cartProduct.get();
            cart.setQuantity(cart.getQuantity() + 1);
        } else {
            cart = new Cart();
            cart.setProduct(product);
            cart.setQuantity(1);
        }

        product.setStock(product.getStock() - 1);
        productRepository.save(product);

        cartRepository.save(cart);
    }

    public void removeFromCart(Long productId) {
        Optional<Product> rawProduct = productRepository.findById(productId);
        Optional<Cart> rawCart = cartRepository.findCartByProductId(productId);
        if (rawProduct.isEmpty() || rawCart.isEmpty()) {
            return;
        }
        Product product = rawProduct.get();
        Cart cart = rawCart.get();

        if (cart.getQuantity() == 1) {
            product.setCart(null);
            cartRepository.delete(cart);
        } else {
            cart.setQuantity(cart.getQuantity() - 1);
            cartRepository.save(cart);
        }

        product.setStock(product.getStock() + 1);
        productRepository.save(product);
    }
}
