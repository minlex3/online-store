package com.store.controller;

import com.store.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @PostMapping
    public Mono<String> makePurchase(@AuthenticationPrincipal Principal auth) {
        return purchaseService.makePurchase(auth.getName())
                .map(orderId -> "redirect:/orders/" + orderId)
                .onErrorResume(e -> Mono.just("redirect:/cart"));
    }
}
