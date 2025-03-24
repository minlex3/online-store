package com.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getAllProducts() {
        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .xpath("//div[2]/div[2]/h3[1]/a[1]", "Нагель КМП 7,5x212 100шт");
    }

    @Test
    void addToCartRedirectProducts() {
        webTestClient.post()
                .uri("/cart/add/1/products")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void addToCartRedirectProductId() {
        webTestClient.post()
                .uri("/cart/add/1/product")
                .exchange()
                .expectHeader().location("/products/1")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void addToCartRedirectCart() {
        webTestClient.post()
                .uri("/cart/add/1/cart")
                .exchange()
                .expectHeader().location("/cart")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void addToCartRedirectUnexpected() {
        webTestClient.post()
                .uri("/cart/add/1/some")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void removeFromCartRedirectProducts() {
        webTestClient.post()
                .uri("/cart/remove/1/products")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void removeFromCartRedirectProductId() {
        webTestClient.post()
                .uri("/cart/remove/1/product")
                .exchange()
                .expectHeader().location("/products/1")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void removeFromCartRedirectCart() {
        webTestClient.post()
                .uri("/cart/remove/1/cart")
                .exchange()
                .expectHeader().location("/cart")
                .expectStatus().is3xxRedirection();
    }

    @Test
    void removeFromCartRedirectUnexpected() {
        webTestClient.post()
                .uri("/cart/remove/1/some")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }
}
