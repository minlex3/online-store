package com.store.service;

import com.store.entity.Cart;
import com.store.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }
}
