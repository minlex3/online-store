package com.store.controller;

import com.store.dto.CartDto;
import com.store.dto.ProductDto;
import com.store.payment.client.model.Balance;
import com.store.service.CartService;
import com.store.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void getAllProducts() {
        List<CartDto> expected = List.of(
                new CartDto(
                        10L,
                        new ProductDto(100L, "product", "desc", 100.0, "url", 1, 0),
                        12
                )
        );

        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));
        when(cartService.findAll()).thenReturn(Flux.fromIterable(expected));

        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody().xpath("//div[2]/div[2]/h3[1]/a[1]", "product");

        verify(cartService).findAll();
    }

    @Test
    void addToCart() {
        when(cartService.addToCart(anyLong())).thenReturn(Mono.empty());
        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));

        webTestClient.post()
                .uri("/cart/add/1/products")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/products");

        verify(cartService).addToCart(1L);
    }

    @Test
    void removeFromCart() {
        when(cartService.removeFromCart(anyLong())).thenReturn(Mono.empty());
        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));

        webTestClient.post()
                .uri("/cart/remove/1/products")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/products");

        verify(cartService).removeFromCart(1L);
    }

    @Test
    void removeProductFromCart() {
        when(cartService.removeProduct(anyLong())).thenReturn(Mono.empty());
        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));

        webTestClient.post()
                .uri("/cart/clear/1/products")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/products");

        verify(cartService).removeProduct(1L);
    }
}
