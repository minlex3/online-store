package com.store.controller;

import com.store.dto.OrderDto;
import com.store.dto.OrderItemDto;
import com.store.dto.ProductDto;
import com.store.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(OrderController.class)
@AutoConfigureWebTestClient
public class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private OrderService orderService;

    @Test
    @WithMockUser(username = "root")
    void getAllOrders() {
        List<OrderDto> expected = List.of(
                new OrderDto(40L, 123.45, "Paid", 1L, List.of()),
                new OrderDto(50L, 321.45, "Prepared", 1L, List.of())
        );

        when(orderService.findAll(anyString())).thenReturn(Flux.fromIterable(expected));

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody().xpath("//div[2]/div").nodeCount(2)
                .xpath("//div[2]/div[1]/h3[1]/a[1]", "Заказ #40");

        verify(orderService).findAll("root");
    }

    @Test
    @WithMockUser(username = "root")
    void getOrderById() {
        OrderDto expected = new OrderDto(40L, 123.45, "Paid", 1L, List.of(
                new OrderItemDto(15L,
                        new ProductDto(100L, "product", "desc", 100.0, "url", 1, 0),
                        1, 112.43)
        ));

        when(orderService.findById(any())).thenReturn(Mono.just(expected));

        webTestClient.get()
                .uri("/orders/40")
                .exchange()
                .expectStatus().isOk()
                .expectBody().xpath("//div[2]/div").nodeCount(1)
                .xpath("//div[2]/div[1]/a[1]", "product");

        verify(orderService).findById(40L);
    }
}
