package com.store.repository;

import com.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    Page<Product> findByNameContainsIgnoreCaseOrderByNameAsc(@Param("name") String name, Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseOrderByPriceAsc(@Param("name") String name, Pageable pageable);
}
