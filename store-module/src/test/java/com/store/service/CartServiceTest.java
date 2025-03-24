package com.store.service;

import com.store.OnlineStoreApplication;
import com.store.dto.CartDto;
import com.store.entity.Cart;
import com.store.entity.Product;
import com.store.repository.CartRepository;
import com.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        Cart cart = new Cart(10L, 10L, 2);

        when(cartRepository.findAll()).thenReturn(Flux.just(cart));
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        Flux<CartDto> cartDtos = cartService.findAll();

        StepVerifier.create(cartDtos)
                .expectNextMatches(
                        el -> el.id().equals(cart.getId())
                                && el.quantity() == cart.getQuantity()
                                && el.product().id().equals(cart.getProductId())
                                && el.product().name().equals(product.getName())
                )
                .expectComplete()
                .verify();
    }

    @Test
    void removeProduct() {
        Cart cart = new Cart(20L, 10L, 2);

        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Mono.just(cart));
        when(productRepository.increaseById(anyLong(), anyInt())).thenReturn(Mono.just(1L));
        when(cartRepository.deleteByProductId(anyLong())).thenReturn(Mono.empty());

        Mono<Void> result = cartService.removeProduct(10L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(cartRepository).findCartByProductId(10L);
        verify(productRepository).increaseById(10L, 2);
        verify(cartRepository).deleteByProductId(10L);
    }

    @Test
    void clearCart() {
        Cart cart = new Cart(20L, 10L, 2);

        when(cartRepository.findAll()).thenReturn(Flux.just(cart));
        when(productRepository.increaseById(anyLong(), anyInt())).thenReturn(Mono.just(1L));
        when(cartRepository.deleteAll()).thenReturn(Mono.empty());

        Mono<Void> result = cartService.clearCart();

        StepVerifier.create(result)
                .verifyComplete();

        verify(cartRepository).findAll();
        verify(productRepository).increaseById(10L, 2);
    }

    @Test
    void addToCartExistsProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        Cart cart = new Cart(20L, 10L, 2);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));
        when(productRepository.decreaseById(anyLong(), anyInt())).thenReturn(Mono.just(1L));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Mono.just(cart));

        when(cartRepository.save(any())).thenReturn(Mono.just(cart));

        Mono<Void> result = cartService.addToCart(10L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById(10L);
        verify(productRepository).decreaseById(10L, 1);
        verify(cartRepository).findCartByProductId(10L);

        cart.setQuantity(cart.getQuantity() + 1);
        verify(cartRepository).save(cart);
    }

    @Test
    void addToCartNonExistsProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));
        when(productRepository.decreaseById(anyLong(), anyInt())).thenReturn(Mono.just(1L));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Mono.empty());

        when(cartRepository.save(any())).thenReturn(Mono.just(new Cart()));

        Mono<Void> result = cartService.addToCart(10L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById(10L);
        verify(productRepository).decreaseById(10L, 1);
        verify(cartRepository).findCartByProductId(10L);

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addToCartOutOfStock() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 0, 0);
        Cart cart = new Cart(20L, 10L, 2);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Mono.just(cart));

        when(cartRepository.save(any())).thenReturn(Mono.just(cart));

        Mono<Void> result = cartService.addToCart(10L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).findById(10L);
        verify(productRepository, never()).decreaseById(anyLong(), anyInt());
        verify(cartRepository, never()).findCartByProductId(anyLong());

        verify(cartRepository, never()).save(any());
    }

    @Test
    void removeFromCart() {
        Cart cart = new Cart(20L, 10L, 2);

        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Mono.just(cart));
        when(productRepository.increaseById(anyLong(), anyInt())).thenReturn(Mono.just(1L));

        when(cartRepository.save(any())).thenReturn(Mono.just(cart));

        Mono<Void> result = cartService.removeFromCart(10L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(cartRepository).findCartByProductId(10L);

        cart.setQuantity(cart.getQuantity() - 1);
        verify(cartRepository).save(cart);
        verify(productRepository).increaseById(10L, 1);
    }

    @Test
    void removeFromCartLastProduct() {
        Cart cart = new Cart(20L, 10L, 1);

        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Mono.just(cart));
        when(productRepository.increaseById(anyLong(), anyInt())).thenReturn(Mono.just(1L));

        when(cartRepository.save(any())).thenReturn(Mono.just(cart));
        when(cartRepository.delete(any())).thenReturn(Mono.empty());

        Mono<Void> result = cartService.removeFromCart(10L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(cartRepository).findCartByProductId(10L);

        verify(cartRepository).delete(cart);
        verify(cartRepository, never()).save(any());
        verify(productRepository).increaseById(10L, 1);
    }

    @Test
    void removeFromCartNonExistsProduct() {
        when(cartRepository.findCartByProductId(anyLong())).thenReturn(Mono.empty());

        when(cartRepository.save(any())).thenReturn(Mono.just(new Cart()));

        Mono<Void> result = cartService.removeFromCart(10L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(cartRepository).findCartByProductId(10L);

        verify(cartRepository, never()).save(any());
        verify(productRepository, never()).increaseById(anyLong(), anyInt());
    }
}
