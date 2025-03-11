package com.store.repository;

import com.store.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    @Query("SELECT p.*, COALESCE(SUM(c.quantity), 0) as cart_quantity FROM products p " +
            "LEFT JOIN cart c on c.product_id = p.id " +
            "WHERE UPPER(p.name) LIKE UPPER(:name) " +
            "GROUP BY p.id " +
            "ORDER BY p.name ASC LIMIT :limit OFFSET :offset")
    Flux<Product> findByNameContainingOrderByName(String name, int limit, int offset);

    @Query("SELECT p.*, COALESCE(SUM(c.quantity), 0) as cart_quantity FROM products p " +
            "LEFT JOIN cart c on c.product_id = p.id " +
            "WHERE UPPER(p.name) LIKE UPPER(:name) " +
            "GROUP BY p.id " +
            "ORDER BY p.price ASC LIMIT :limit OFFSET :offset")
    Flux<Product> findByNameContainingOrderByPrice(String name, int limit, int offset);

    @Query("SELECT COUNT(*) FROM products WHERE UPPER(name) LIKE UPPER(:name)")
    Mono<Long> countByNameContaining(String name);

    @Override
    @NonNull
    @Query("SELECT p.*, COALESCE(SUM(c.quantity), 0) as cart_quantity FROM products p " +
            "LEFT JOIN cart c on c.product_id = p.id " +
            "WHERE p.id = :id " +
            "GROUP BY p.id ")
    Mono<Product> findById(Long id);

    @Query("UPDATE products SET stock = stock + :quantity WHERE id = :id")
    Mono<Long> increaseById(Long id, int quantity);

    @Query("UPDATE products SET stock = stock - :quantity WHERE id = :id")
    Mono<Long> decreaseById(Long id, int quantity);

    @NonNull
    @Query("INSERT INTO products (NAME, DESCRIPTION, PRICE, IMAGE_URL, STOCK) " +
            "VALUES (:name, :description, :price, :url, :stock)")
    Mono<Product> save(String name, String description, double price, String url, int stock);

    @NonNull
    @Query("INSERT INTO products (ID, NAME, DESCRIPTION, PRICE, IMAGE_URL, STOCK) " +
            "VALUES (:id, :name, :description, :price, :url, :stock)")
    Mono<Product> save(Long id, String name, String description, double price, String url, int stock);

    @Query("SELECT p.* FROM products p WHERE p.name = :name")
    Mono<Product> findByName(String name);
}
