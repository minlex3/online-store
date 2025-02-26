package com.store.controller;

import com.store.dto.CartDto;
import com.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String findAll(Model model) {
        List<CartDto> cartDtoList = cartService.findAll();

        double total = cartDtoList.stream()
                .mapToDouble(cartDto -> cartDto.quantity() * cartDto.product().price())
                .sum();

        model.addAttribute("cart", cartDtoList);
        model.addAttribute("total", total);
        return "cart-list";
    }

    @PostMapping("/add/{id}/{redirect}")
    public String addToCart(
            @PathVariable("id") Long id,
            @PathVariable("redirect") String redirect
    ) {
        cartService.addToCart(id);

        return switch (redirect) {
            case "products" -> "redirect:/products";
            case "product" -> "redirect:/products/" + id;
            case "cart" -> "redirect:/cart";
            default -> "redirect:/products";
        };
    }

    @PostMapping("/remove/{id}/{redirect}")
    public String removeFromCart(
            @PathVariable("id") Long id,
            @PathVariable("redirect") String redirect
    ) {
        cartService.removeFromCart(id);

        return switch (redirect) {
            case "products" -> "redirect:/products";
            case "product" -> "redirect:/products/" + id;
            case "cart" -> "redirect:/cart";
            default -> "redirect:/products";
        };
    }

    @PostMapping("/clear/{id}/{redirect}")
    public String removeProduct(
            @PathVariable("id") Long id,
            @PathVariable("redirect") String redirect
    ) {
        cartService.removeProduct(id);

        return switch (redirect) {
            case "products" -> "redirect:/products";
            case "product" -> "redirect:/products/" + id;
            case "cart" -> "redirect:/cart";
            default -> "redirect:/products";
        };
    }
}
