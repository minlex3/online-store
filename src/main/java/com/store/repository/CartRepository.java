package com.store.repository;

import com.store.entity.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    @NonNull
    List<Cart> findAll();
}
