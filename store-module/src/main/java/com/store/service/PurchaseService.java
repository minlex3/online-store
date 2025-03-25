package com.store.service;

import com.store.entity.Order;
import com.store.entity.OrderItem;
import com.store.mapper.CartMapper;
import com.store.mapper.ProductMapper;
import com.store.repository.CartRepository;
import com.store.repository.OrderItemRepository;
import com.store.repository.OrderRepository;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PaymentService paymentService;

    @Transactional
    public Mono<Long> makePurchase() {
        return cartRepository.findAll()
                .collectList()
                .flatMap(cartItems -> {
                    if (cartItems.isEmpty()) {
                        return Mono.error(new RuntimeException("Cart is empty"));
                    }


                    return Flux.fromIterable(cartItems)
                            .flatMap(cartItem -> productRepository.findById(cartItem.getProductId())
                                    .map(product -> cartMapper.toCartDto(cartItem, productMapper.toProductDto(product))))
                            .collectList()
                            .flatMap(cartDtos -> {
                                double total = cartDtos.stream()
                                        .mapToDouble(cartDto -> cartDto.quantity() * cartDto.product().price())
                                        .sum();

                                return paymentService.makePurchase(total)
                                        .flatMap(result -> {
                                            Order order = new Order();
                                            order.setTotalAmount(total);
                                            order.setStatus("Оплачен");

                                            return orderRepository.save(order)
                                                    .doOnNext(System.out::println)
                                                    .flatMap(savedOrder -> {
                                                        List<OrderItem> orderItems = cartDtos.stream()
                                                                .map(cartDto -> {
                                                                    OrderItem orderItem = new OrderItem();
                                                                    orderItem.setOrderId(savedOrder.getId());
                                                                    orderItem.setProductId(cartDto.product().id());
                                                                    orderItem.setQuantity(cartDto.quantity());
                                                                    orderItem.setPrice(cartDto.product().price());
                                                                    return orderItem;
                                                                })
                                                                .toList();

                                                        return orderItemRepository.saveAll(orderItems)
                                                                .then(cartRepository.deleteAll())
                                                                .then(Mono.just(savedOrder.getId()));
                                                    });
                                        });
                            });
                });
    }
}
