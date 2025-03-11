package com.store.repository;

import com.store.entity.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void firstTest() {
//        Cart cart = new Cart();
//        cart.setId(100L);
//        cart.setProductId(300L);
//        cart.setQuantity(10);
//
//        productRepository.save(300L, "name", "desc", 12.34, "url", 10).block();
//        cartRepository.save(cart)
//                .then(cartRepository.findById(100L))
//                .as(StepVerifier::create)
//                .expectNextMatches(c -> c.getQuantity() == 10)
//                .verifyComplete();
    }
}
