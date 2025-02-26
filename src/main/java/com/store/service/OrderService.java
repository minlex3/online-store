package com.store.service;

import com.store.dto.OrderDto;
import com.store.entity.Order;
import com.store.mapper.OrderMapper;
import com.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDto findById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> orderMapper.toOrderDto(value)).orElse(null);
    }
}
