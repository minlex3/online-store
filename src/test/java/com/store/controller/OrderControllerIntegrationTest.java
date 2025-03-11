package com.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getAllOrders() {
        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .xpath("//div[2]/div").nodeCount(2)
                .xpath("//div[2]/div[1]/h3[1]/a[1]", "Заказ #1");
    }

    @Test
    void getOrderById() {
        webTestClient.get()
                .uri("/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .xpath("//div[2]/div").nodeCount(1)
                .xpath("//div[2]/div[1]/a[1]", "Молоток-гвоздодер GROSS");
    }
}
