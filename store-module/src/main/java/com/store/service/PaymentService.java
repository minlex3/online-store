package com.store.service;

import com.store.payment.client.ApiClient;
import com.store.payment.client.api.DefaultApi;
import com.store.payment.client.model.Balance;
import com.store.payment.client.model.PurchaseInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    private final DefaultApi clientApi;

    @Autowired
    public PaymentService(@Value("${payment-module.url}") String paymentUrl) {
        System.out.println("PAYMENT URL : " + paymentUrl);
        ApiClient client = new ApiClient();
        client.setBasePath(paymentUrl);

        this.clientApi = new DefaultApi(client);
    }

    public Mono<Balance> getBalance() {
        return clientApi.apiBalanceGet()
                .onErrorResume(e -> Mono.just(new Balance()));
    }

    public Mono<Balance> makePurchase(Double totalAmount) {
        PurchaseInfo purchaseInfo = new PurchaseInfo();
        purchaseInfo.setTotalAmount(totalAmount);
        purchaseInfo.setDiscountAmount(0.0);

        return clientApi.apiPurchasePost(purchaseInfo);
    }
}
