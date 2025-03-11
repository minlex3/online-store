package com.store.controller;

import com.store.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @PostMapping
    public Mono<String> makePurchase() {
        return purchaseService.makePurchase()
                .map(orderId -> "redirect:/orders/" + orderId)
                .onErrorResume(e -> Mono.just("redirect:/cart"));
    }
}
