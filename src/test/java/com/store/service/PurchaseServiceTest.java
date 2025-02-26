package com.store.service;

import com.store.OnlineStoreApplication;
import com.store.entity.Cart;
import com.store.entity.Order;
import com.store.entity.OrderItem;
import com.store.entity.Product;
import com.store.repository.CartRepository;
import com.store.repository.OrderItemRepository;
import com.store.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = OnlineStoreApplication.class)
public class PurchaseServiceTest {

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private CartRepository cartRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Test
    void allowedPurchaseTest() {
        Cart cart = new Cart(10L, new Product(), 1);
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        Boolean result = purchaseService.isPurchaseAllowed();
        Assertions.assertTrue(result);
    }

    @Test
    void notAllowedPurchaseTest() {
        when(cartRepository.findAll()).thenReturn(List.of());

        Boolean result = purchaseService.isPurchaseAllowed();
        Assertions.assertFalse(result);
    }

    @Test
    void makePurchase() {
        Product product = new Product(1L, "product", "desc", 12.34, "url", 15, null);
        Product product2 = new Product(2L, "product", "desc", 12.34, "url", 15, null);
        Cart cart = new Cart(10L, product, 1);
        Cart cart2 = new Cart(11L, product2, 2);

        when(cartRepository.findAll()).thenReturn(List.of(cart, cart2));
        when(orderRepository.save(any())).thenReturn(null);
        when(orderItemRepository.save(any())).thenReturn(null);
        doNothing().when(cartRepository).deleteAll();

        purchaseService.makePurchase();

        verify(cartRepository, times(1)).findAll();
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
        verify(cartRepository, times(1)).deleteAll();
    }
}
