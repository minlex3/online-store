package com.store.service;

import com.store.entity.Cart;
import com.store.entity.Order;
import com.store.entity.OrderItem;
import com.store.entity.Product;
import com.store.repository.CartRepository;
import com.store.repository.OrderItemRepository;
import com.store.repository.OrderRepository;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PurchaseService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Long makePurchase() {
        List<Cart> allCartItems = cartRepository.findAll();
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        allCartItems.forEach(cart -> {
            total.set(total.get() + cart.getQuantity() * cart.getProduct().getPrice());
        });

        Order order = new Order();
        order.setTotalAmount(total.get());
        order.setStatus("Prepare");
        orderRepository.save(order);

        allCartItems.forEach(cart -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cart.getProduct());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setPrice(cart.getProduct().getPrice());
            orderItemRepository.save(orderItem);
        });

        List<Product> allProducts = productRepository.findAll();
        allProducts.forEach(product -> {
            product.setCart(null);
        });

        cartRepository.deleteAll();

        return order.getId();
    }
}
