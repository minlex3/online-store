package com.store.service;

import com.store.entity.Cart;
import com.store.entity.Order;
import com.store.entity.OrderItem;
import com.store.repository.CartRepository;
import com.store.repository.OrderItemRepository;
import com.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Boolean isPurchaseAllowed() {
        List<Cart> allCartItems = cartRepository.findAll();
        return !allCartItems.isEmpty();
    }

    public Long makePurchase() {
        List<Cart> allCartItems = cartRepository.findAll();
        if (allCartItems.isEmpty()) {
            return 0L;
        }

        double total = allCartItems.stream()
                .mapToDouble(cart -> cart.getQuantity() * cart.getProduct().getPrice())
                .sum();

        Order order = new Order();
        order.setTotalAmount(total);
        order.setStatus("Оплачен");
        orderRepository.save(order);

        allCartItems.forEach(cart -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cart.getProduct());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setPrice(cart.getProduct().getPrice());
            orderItemRepository.save(orderItem);

            cart.getProduct().setCarts(null);
        });

        cartRepository.deleteAll();

        return order.getId();
    }
}
