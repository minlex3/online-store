package com.store.service;

import com.store.OnlineStoreApplication;
import com.store.dto.OrderDto;
import com.store.entity.Order;
import com.store.entity.OrderItem;
import com.store.entity.Product;
import com.store.repository.OrderItemRepository;
import com.store.repository.OrderRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OnlineStoreApplication.class)
public class OrderServiceTest {

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Test
    void findAllOrders() {
        Order order = new Order(10L, 12.34, "Paid");
        OrderItem orderItem = new OrderItem(20L, 30L, 10L, 5, 50.5);
        Product product = new Product(30L, "name", "desc", 10.1, "url", 10, 0);

        when(orderRepository.findAll()).thenReturn(Flux.just(order));
        when(orderItemRepository.findAllByOrderId(any())).thenReturn(Flux.just(orderItem));
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        Flux<OrderDto> result = orderService.findAll();

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.id().equals(order.getId())
                                && el.totalAmount().equals(order.getTotalAmount())
                                && el.orderItems().size() == 1
                                && el.orderItems().getFirst().id().equals(orderItem.getId())
                                && el.orderItems().getFirst().product().id().equals(product.getId())
                )
                .expectComplete()
                .verify();

        verify(orderRepository).findAll();
        verify(orderItemRepository).findAllByOrderId(10L);
        verify(productRepository).findById(30L);
    }

    @Test
    void findOrderById() {
        Order order = new Order(10L, 12.34, "Paid");
        OrderItem orderItem = new OrderItem(20L, 30L, 10L, 5, 50.5);
        Product product = new Product(30L, "name", "desc", 10.1, "url", 10, 0);

        when(orderRepository.findById(anyLong())).thenReturn(Mono.just(order));
        when(orderItemRepository.findAllByOrderId(any())).thenReturn(Flux.just(orderItem));
        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        Mono<OrderDto> result = orderService.findById(10L);

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.id().equals(order.getId())
                                && el.totalAmount().equals(order.getTotalAmount())
                                && el.orderItems().size() == 1
                                && el.orderItems().getFirst().id().equals(orderItem.getId())
                                && el.orderItems().getFirst().product().id().equals(product.getId())
                )
                .expectComplete()
                .verify();

        verify(orderRepository).findById(10L);
        verify(orderItemRepository).findAllByOrderId(10L);
        verify(productRepository).findById(30L);
    }
}
