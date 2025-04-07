package com.store.controller;

import com.store.configuration.EmbeddedRedisConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(EmbeddedRedisConfiguration.class)
public class CartControllerIntegrationTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser(username = "root")
    void getAllProducts() {
        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .xpath("//div[2]/div[2]/h3[1]/a[1]", "Нагель КМП 7,5x212 100шт");
    }

    @Test
    @WithMockUser(username = "root")
    void addToCartRedirectProducts() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/add/1/products")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "root")
    void addToCartRedirectProductId() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/add/1/product")
                .exchange()
                .expectHeader().location("/products/1")
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "root")
    void addToCartRedirectCart() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/add/1/cart")
                .exchange()
                .expectHeader().location("/cart")
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "root")
    void addToCartRedirectUnexpected() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/add/1/some")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "root")
    void removeFromCartRedirectProducts() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/remove/1/products")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "root")
    void removeFromCartRedirectProductId() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/remove/1/product")
                .exchange()
                .expectHeader().location("/products/1")
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "root")
    void removeFromCartRedirectCart() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/remove/1/cart")
                .exchange()
                .expectHeader().location("/cart")
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "root")
    void removeFromCartRedirectUnexpected() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri("/cart/remove/1/some")
                .exchange()
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }
}
