package com.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PurchaseControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void makePurchaseNotAllowed() {
        webTestClient.post()
                .uri("/purchase")
                .exchange()
                .expectHeader().location("/cart")
                .expectStatus().is3xxRedirection();
    }
}
