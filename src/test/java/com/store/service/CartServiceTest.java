package com.store.service;

import com.store.OnlineStoreApplication;
import com.store.dto.CartDto;
import com.store.entity.Cart;
import com.store.entity.Product;
import com.store.repository.CartRepository;
import com.store.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = OnlineStoreApplication.class)
public class CartServiceTest {

    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Test
    void findAll() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);
        Cart cart = new Cart(10L, product, 2);
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        List<CartDto> cartDtos = cartService.findAll();
        Assertions.assertEquals(1, cartDtos.size());

        CartDto cartDto = cartDtos.getFirst();
        Assertions.assertEquals(cart.getId(), cartDto.id());
        Assertions.assertEquals(cart.getQuantity(), cartDto.quantity());
        Assertions.assertEquals(cart.getProduct().getId(), cartDto.product().id());
        Assertions.assertEquals(cart.getProduct().getName(), cartDto.product().name());
        Assertions.assertEquals(cart.getProduct().getDescription(), cartDto.product().description());
    }

    @Test
    void removeProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);
        Cart cart = new Cart(20L, product, 2);

        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Optional.of(cart));
        doNothing().when(cartRepository).delete(any());

        cartService.removeProduct(10L);

        cart.getProduct().setStock(17);
        cart.getProduct().setCart(null);
        verify(cartRepository).delete(cart);
    }

    @Test
    void addToCartExistsProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);
        Cart cart = new Cart(20L, product, 2);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Optional.of(cart));

        when(productRepository.save(any())).thenReturn(null);
        when(cartRepository.save(any())).thenReturn(null);

        cartService.addToCart(10L);

        verify(productRepository).findById(10L);
        verify(cartRepository).findCartByProductId(10L);

        product.setStock(product.getStock() - 1);
        verify(productRepository).save(product);

        cart.setQuantity(cart.getQuantity() + 1);
        verify(cartRepository).save(cart);
    }

    @Test
    void addToCartNonExistsProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Optional.empty());

        when(productRepository.save(any())).thenReturn(null);
        when(cartRepository.save(any())).thenReturn(null);

        cartService.addToCart(10L);

        verify(productRepository).findById(10L);
        verify(cartRepository).findCartByProductId(10L);

        product.setStock(product.getStock() - 1);
        verify(productRepository).save(product);

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addToCartOutOfStock() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 0, null);
        Cart cart = new Cart(20L, product, 2);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Optional.of(cart));

        when(productRepository.save(any())).thenReturn(null);
        when(cartRepository.save(any())).thenReturn(null);

        cartService.addToCart(10L);

        verify(productRepository).findById(10L);
        verify(cartRepository, never()).findCartByProductId(anyLong());

        verify(productRepository, never()).save(any());

        verify(cartRepository, never()).save(any());
    }

    @Test
    void removeFromCart() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);
        Cart cart = new Cart(20L, product, 2);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Optional.of(cart));

        when(productRepository.save(any())).thenReturn(null);
        when(cartRepository.save(any())).thenReturn(null);

        cartService.removeFromCart(10L);

        verify(productRepository).findById(10L);
        verify(cartRepository).findCartByProductId(10L);

        product.setStock(product.getStock() + 1);
        verify(productRepository).save(product);

        cart.setQuantity(cart.getQuantity() - 1);
        verify(cartRepository).save(cart);
    }

    @Test
    void removeFromCartLastProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);
        Cart cart = new Cart(20L, product, 1);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Optional.of(cart));

        when(productRepository.save(any())).thenReturn(null);
        when(cartRepository.save(any())).thenReturn(null);
        doNothing().when(cartRepository).delete(any());

        cartService.removeFromCart(10L);

        verify(productRepository).findById(10L);
        verify(cartRepository).findCartByProductId(10L);

        product.setStock(product.getStock() + 1);
        verify(productRepository).save(product);

        verify(cartRepository).delete(cart);
        verify(cartRepository, never()).save(any());
    }

    @Test
    void removeFromCartNonExistsProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Optional.empty());

        when(productRepository.save(any())).thenReturn(null);
        when(cartRepository.save(any())).thenReturn(null);
        doNothing().when(cartRepository).delete(any());

        cartService.removeFromCart(10L);

        verify(productRepository).findById(10L);
        verify(cartRepository).findCartByProductId(10L);

        verify(productRepository, never()).save(any());

        verify(cartRepository, never()).save(any());
    }
}
