package com.store.controller;

import com.store.dto.CartDto;
import com.store.payment.client.model.Balance;
import com.store.service.CartService;
import com.store.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public Mono<String> findAll(Model model, @AuthenticationPrincipal UserDetails user) {
        return Mono.zip(
                        cartService.findAll(user.getUsername()).collectList(),
                        paymentService.getBalance()
                )
                .map(tuple -> {
                    List<CartDto> cartList = tuple.getT1();
                    Balance balance = tuple.getT2();

                    double total = cartList.stream()
                            .mapToDouble(cartDto -> cartDto.quantity() * cartDto.product().price())
                            .sum();

                    model.addAttribute("cart", cartList);
                    model.addAttribute("total", total);
                    model.addAttribute("balance", balance.getBalance());

                    return "cart-list";
                });
    }

    @PostMapping("/add/{id}/{redirect}")
    public Mono<String> addToCart(
            @PathVariable("id") Long id,
            @PathVariable("redirect") String redirect,
            @AuthenticationPrincipal UserDetails user
    ) {
        return cartService.addToCart(id, user.getUsername())
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
            @PathVariable("redirect") String redirect,
            @AuthenticationPrincipal UserDetails user
    ) {
        return cartService.removeFromCart(id, user.getUsername())
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
            @PathVariable("redirect") String redirect,
            @AuthenticationPrincipal UserDetails user
    ) {
        return cartService.removeProduct(id, user.getUsername())
                .thenReturn(switch (redirect) {
                    case "products" -> "redirect:/products";
                    case "product" -> "redirect:/products/" + id;
                    case "cart" -> "redirect:/cart";
                    default -> "redirect:/products";
                });
    }

    @PostMapping("/clear")
    public Mono<String> clearCart(@AuthenticationPrincipal UserDetails user) {
        return cartService.clearCart(user.getUsername())
                .thenReturn("redirect:/cart");
    }
}
