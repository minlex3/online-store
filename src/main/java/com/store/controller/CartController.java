package com.store.controller;

import com.store.dto.CartDto;
import com.store.entity.Cart;
import com.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String findAll(Model model) {
        List<CartDto> all = cartService.findAll();
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        all.forEach(cart -> {
            total.set(total.get() + cart.quantity() * cart.product().price());
        });

        model.addAttribute("cart", all);
        model.addAttribute("total", total.get());
        return "cart-list";
    }

    @PostMapping("/add/{id}")
    public String addToCart(Model model, @PathVariable("id") Long id) {
        cartService.addToCart(id);

        return "redirect:/products";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(Model model, @PathVariable("id") Long id) {
        cartService.removeFromCart(id);

        return "redirect:/products";
    }

    @PostMapping("/add2/{id}")
    public String addToCart2(Model model, @PathVariable("id") Long id) {
        cartService.addToCart(id);

        return "redirect:/products/" + id;
    }

    @PostMapping("/remove2/{id}")
    public String removeFromCart2(Model model, @PathVariable("id") Long id) {
        cartService.removeFromCart(id);

        return "redirect:/products/" + id;
    }
}
