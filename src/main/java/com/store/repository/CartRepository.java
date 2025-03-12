package com.store.repository;

import com.store.entity.Cart;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveCrudRepository<Cart, Long> {
    Mono<Cart> findCartByProductId(Long productId);

    @Query("DELETE FROM cart WHERE product_id = :productId")
    Mono<Void> deleteByProductId(Long productId);
}
