package com.store.repository;

import com.store.entity.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    @NonNull
    List<Cart> findAll();

    Optional<Cart> findCartByProductId(Long productId);
}
