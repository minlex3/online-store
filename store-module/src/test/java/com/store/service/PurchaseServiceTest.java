package com.store.service;

import com.store.OnlineStoreApplication;
import com.store.entity.Cart;
import com.store.entity.Order;
import com.store.entity.Product;
import com.store.entity.UserData;
import com.store.payment.client.model.Balance;
import com.store.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = OnlineStoreApplication.class)
public class PurchaseServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private PurchaseService purchaseService;

    @Test
    void makePurchase() {
        Product product = new Product(1L, "product", "desc", 12.34, "url", 15, 0);
        Product product2 = new Product(2L, "product", "desc", 12.34, "url", 15, 0);
        Cart cart = new Cart(10L, 1L, 1, 1L);
        Cart cart2 = new Cart(11L, 2L, 2, 1L);

        when(cartRepository.findAllByUserId(anyLong())).thenReturn(Flux.just(cart, cart2));
        when(productRepository.findById(1L)).thenReturn(Mono.just(product));
        when(productRepository.findById(2L)).thenReturn(Mono.just(product2));

        when(orderRepository.save(any())).thenReturn(Mono.just(new Order(20L, 123.4, "paid", 1L)));
        when(orderItemRepository.saveAll(anyList())).thenReturn(Flux.empty());
        when(cartRepository.deleteAll()).thenReturn(Mono.empty());
        when(userRepository.findByUsername(any())).thenReturn(Mono.just(new UserData(1L, "root", "pass")));

        when(paymentService.makePurchase(any())).thenReturn(Mono.just(new Balance()));

        Mono<Long> result = purchaseService.makePurchase("root");

        StepVerifier.create(result)
                .expectNext(20L)
                .expectComplete()
                .verify();

        verify(cartRepository, times(1)).findAllByUserId(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
        verify(cartRepository, times(1)).deleteAll();
    }
}
