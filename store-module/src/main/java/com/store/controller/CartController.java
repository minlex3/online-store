package com.store.controller;

import com.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Mono<String> findAll(Model model) {
        return cartService.findAll()
                .collectList()
                .map(cartList -> {
                    double total = cartList.stream()
                            .mapToDouble(cartDto -> cartDto.quantity() * cartDto.product().price())
                            .sum();

                    model.addAttribute("cart", cartList);
                    model.addAttribute("total", total);

                    return "cart-list";
                });
    }

    @PostMapping("/add/{id}/{redirect}")
    public Mono<String> addToCart(
            @PathVariable("id") Long id,
            @PathVariable("redirect") String redirect
    ) {
        return cartService.addToCart(id)
                .thenReturn(switch (redirect) {
                    case "products" -> "redirect:/products";
                    case "product" -> "redirect:/products/" + id;
                    case "cart" -> "redirect:/cart";
                    default -> "redirect:/products";
                });
    }

    @PostMapping("/remove/{id}/{redirect}")
    public Mono<String> removeFromCart(
            @PathVariable("id") Long id,
            @PathVariable("redirect") String redirect
    ) {
        return cartService.removeFromCart(id)
                .thenReturn(switch (redirect) {
                    case "products" -> "redirect:/products";
                    case "product" -> "redirect:/products/" + id;
                    case "cart" -> "redirect:/cart";
                    default -> "redirect:/products";
                });
    }

    @PostMapping("/clear/{id}/{redirect}")
    public Mono<String> removeProduct(
            @PathVariable("id") Long id,
            @PathVariable("redirect") String redirect
    ) {
        return cartService.removeProduct(id)
                .thenReturn(switch (redirect) {
                    case "products" -> "redirect:/products";
                    case "product" -> "redirect:/products/" + id;
                    case "cart" -> "redirect:/cart";
                    default -> "redirect:/products";
                });
    }

    @PostMapping("/clear")
    public Mono<String> clearCart() {
        return cartService.clearCart()
                .thenReturn("redirect:/cart");
    }
}
