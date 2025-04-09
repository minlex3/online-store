package com.store.service;

import com.store.payment.client.model.Balance;
import com.store.payment.client.model.PurchaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {
    private final WebClient webClient;

    @Autowired
    private final ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager;

    @Autowired
    public PaymentService(@Value("${payment-module.url}") String paymentUrl,
                          ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
        System.out.println("PAYMENT URL : " + paymentUrl);

        webClient = WebClient.create(paymentUrl);

        this.auth2AuthorizedClientManager = auth2AuthorizedClientManager;
    }

    public Mono<Balance> getBalance() {
        return auth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("yandex")
                        .principal("system")
                        .build())
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue)
                .flatMap(accessToken -> webClient.get()
                        .uri("/api/balance")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(Balance.class)
                )
                .onErrorResume(e -> Mono.just(new Balance()));
    }

    public Mono<Balance> makePurchase(Double totalAmount) {
        PurchaseInfo purchaseInfo = new PurchaseInfo();
        purchaseInfo.setTotalAmount(totalAmount);
        purchaseInfo.setDiscountAmount(0.0);

        return auth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("yandex")
                        .principal("system")
                        .build())
                .flatMap(authorizedClient -> {
                    String accessToken = authorizedClient.getAccessToken().getTokenValue();
                    return webClient.post()
                            .uri("/api/purchase")
                            .headers(headers -> {
                                headers.setBearerAuth(accessToken);
                                headers.setContentType(MediaType.APPLICATION_JSON);
                            })
                            .bodyValue(purchaseInfo)
                            .retrieve()
                            .bodyToMono(Balance.class);
                });
    }
}
