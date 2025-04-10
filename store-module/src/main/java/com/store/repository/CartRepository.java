package com.store.repository;

import com.store.entity.Cart;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveCrudRepository<Cart, Long> {
    Mono<Cart> findCartByProductId(Long productId);

    Mono<Cart> findCartByProductIdAndUserId(Long productId, Long userId);

    @Query("DELETE FROM cart WHERE product_id = :productId AND user_id = :userId")
    Mono<Void> deleteByProductIdAndUserId(Long productId, Long userId);

    Flux<Cart> findAllByUserId(Long userId);
}
