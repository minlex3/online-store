package com.store.controller;

import com.store.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(PurchaseController.class)
public class PurchaseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PurchaseService purchaseService;

    @Test
    void makeAllowedPurchase() {
        when(purchaseService.makePurchase()).thenReturn(Mono.just(10L));

        webTestClient.post()
                .uri("/purchase")
                .exchange()
                .expectHeader().location("/orders/10")
                .expectStatus().is3xxRedirection();

        verify(purchaseService).makePurchase();
    }

    @Test
    void makeNotAllowedPurchase() {
        when(purchaseService.makePurchase()).thenReturn(Mono.error(new RuntimeException("exception")));

        webTestClient.post()
                .uri("/purchase")
                .exchange()
                .expectHeader().location("/cart")
                .expectStatus().is3xxRedirection();

        verify(purchaseService).makePurchase();
    }
}
