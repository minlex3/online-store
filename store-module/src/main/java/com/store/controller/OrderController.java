package com.store.controller;

import com.store.dto.OrderDto;
import com.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Mono<Rendering> findAll() {
        Flux<OrderDto> orderDtoList = orderService.findAll();
        Rendering r = Rendering.view("orders-list")
                .modelAttribute("orders", orderDtoList)
                .build();

        return Mono.just(r);
    }

    @GetMapping("/{id}")
    public Mono<Rendering> findById(@PathVariable("id") Long id) {
        Mono<OrderDto> order = orderService.findById(id);
        Rendering r = Rendering.view("order-description")
                .modelAttribute("order", order)
                .build();

        return Mono.just(r);
    }
}
