package com.store.repository;

import com.store.entity.OrderItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {
    @Query("SELECT * FROM order_items WHERE order_items.order_id = :orderId")
    Flux<OrderItem> findAllByOrderId(Long orderId);
}
