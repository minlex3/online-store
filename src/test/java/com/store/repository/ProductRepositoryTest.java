package com.store.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void firstTest() {
        productRepository.save("name", "desc", 12.34, "url", 10)
                .then(productRepository.findByName("name"))
                .as(StepVerifier::create)
                .expectNextMatches(p -> p.getPrice().equals(12.34))
                .verifyComplete();
    }
}
