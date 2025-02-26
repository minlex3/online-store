package com.store.repository;

import com.store.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Override
    @NonNull
    List<Order> findAll();
}
