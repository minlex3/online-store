package com.store.controller;

import com.store.entity.Order;
import com.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String findAll(Model model) {
        List<Order> all = orderService.findAll();
        model.addAttribute("orders", all);

        return "orders-list";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id){
        Order order = orderService.findById(id);
        model.addAttribute("order", order);

        return "order-description";
    }
}
