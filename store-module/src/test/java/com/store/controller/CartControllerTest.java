package com.store.controller;

import com.store.dto.CartDto;
import com.store.dto.ProductDto;
import com.store.payment.client.model.Balance;
import com.store.service.CartService;
import com.store.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(CartController.class)
@AutoConfigureWebTestClient
public class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    @WithMockUser(username = "root")
    void getAllProducts() {
        List<CartDto> expected = List.of(
                new CartDto(
                        10L,
                        new ProductDto(100L, "product", "desc", 100.0, "url", 1, 0),
                        12,
                        1L
                )
        );

        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));
        when(cartService.findAll(anyString())).thenReturn(Flux.fromIterable(expected));

        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody().xpath("//div[2]/div[2]/h3[1]/a[1]", "product");

        verify(cartService).findAll("root");
    }

    @Test
    @WithMockUser(username = "root")
    void addToCart() {
        when(cartService.addToCart(anyLong(), anyString())).thenReturn(Mono.empty());
        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));

        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/add/1/products")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/products");

        verify(cartService).addToCart(1L, "root");
    }

    @Test
    @WithMockUser(username = "root")
    void removeFromCart() {
        when(cartService.removeFromCart(anyLong(), anyString())).thenReturn(Mono.empty());
        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));

        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/remove/1/products")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/products");

        verify(cartService).removeFromCart(1L, "root");
    }

    @Test
    @WithMockUser(username = "root")
    void removeProductFromCart() {
        when(cartService.removeProduct(anyLong(), anyString())).thenReturn(Mono.empty());
        when(paymentService.getBalance()).thenReturn(Mono.just(new Balance()));

        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/clear/1/products")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/products");

        verify(cartService).removeProduct(1L, "root");
    }
}
