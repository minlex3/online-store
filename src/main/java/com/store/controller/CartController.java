package com.store.controller;

import com.store.entity.Cart;
import com.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String findAll(Model model) {
        List<Cart> all = cartService.findAll();

        model.addAttribute("cart", all);
        return "cart-list";
    }
}
