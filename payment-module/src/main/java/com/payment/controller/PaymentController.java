package com.payment.controller;

import com.payment.server.api.DefaultApi;
import com.payment.server.domain.Balance;
import com.payment.server.domain.PurchaseInfo;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class PaymentController implements DefaultApi {

    private final Balance balance = new Balance(1000.0);

    @Value("${purchase.initial-balance}")
    public void setInitialBalance(double initialBalance) {
        this.balance.setBalance(initialBalance);
    }

    @Override
    public Mono<ResponseEntity<Balance>> apiBalanceGet(
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        return Mono.just(ResponseEntity.ok(balance));
    }

    @Override
    public Mono<ResponseEntity<Balance>> apiPurchasePost(
            @Parameter(name = "PurchaseInfo", description = "", required = true) @Valid @RequestBody Mono<PurchaseInfo> purchaseInfo,
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        return purchaseInfo.flatMap(info -> {
            double totalAmount = info.getTotalAmount();

            if (balance.getBalance() >= totalAmount) {
                balance.setBalance(balance.getBalance() - totalAmount);

                return Mono.just(ResponseEntity.ok(balance));
            } else {
                return Mono.just(ResponseEntity.badRequest().body(balance));
            }
        });
    }
}
