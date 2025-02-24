package com.store.repository;

import com.store.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Override
    @NonNull
    List<Product> findAll();
}
