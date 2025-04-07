package com.store.service;

import com.store.dto.OrderDto;
import com.store.dto.ProductDto;
import com.store.mapper.OrderItemMapper;
import com.store.mapper.OrderMapper;
import com.store.mapper.ProductMapper;
import com.store.repository.OrderItemRepository;
import com.store.repository.OrderRepository;
import com.store.repository.ProductRepository;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    private Flux<OrderDto> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .flatMap(order -> orderItemRepository.findAllByOrderId(order.getId())
                        .flatMap(orderItem -> productRepository.findById(orderItem.getProductId())
                                .map(product -> {
                                    ProductDto productDto = productMapper.toProductDto(product);

                                    return orderItemMapper.toOrderItemDto(orderItem, productDto);
                                }))
                        .collectList()
                        .map(orderItems -> orderMapper.toOrderDto(order, orderItems)));
    }

    public Flux<OrderDto> findAll(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMapMany(user -> findAllByUserId(user.getId()));
    }

    public Mono<OrderDto> findById(Long orderId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> orderItemRepository.findAllByOrderId(order.getId())
                        .flatMap(orderItem -> productRepository.findById(orderItem.getProductId())
                                .map(product -> {
                                    ProductDto productDto = productMapper.toProductDto(product);

                                    return orderItemMapper.toOrderItemDto(orderItem, productDto);
                                }))
                        .collectList()
                        .map(orderItems -> orderMapper.toOrderDto(order, orderItems)));
    }
}
