package com.store.service;

import com.store.dto.CartDto;
import com.store.dto.PageResponse;
import com.store.dto.ProductDto;
import com.store.entity.Cart;
import com.store.mapper.CartMapper;
import com.store.mapper.ProductMapper;
import com.store.repository.CartRepository;
import com.store.repository.ProductRepository;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    private ReactiveRedisTemplate<String, PageResponse> redisPageTemplate;

    public Flux<CartDto> findAll(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMapMany(user -> findAllByUserId(user.getId()));
    }

    private Flux<CartDto> findAllByUserId(Long userId) {
        return cartRepository.findAllByUserId(userId)
                .flatMap(cart -> productRepository.findById(cart.getProductId())
                        .map(product -> {
                            ProductDto productDto = productMapper.toProductDto(product);

                            return cartMapper.toCartDto(cart, productDto);
                        }));
    }

    @Transactional
    public Mono<Void> clearCart(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> clearCartByUserId(user.getId()));
    }

    private Mono<Void> clearCartByUserId(Long userId) {
        return cartRepository.findAllByUserId(userId)
                .map(cart -> productRepository.increaseById(cart.getProductId(), cart.getQuantity()).subscribe())
                .then(cartRepository.deleteAll())
                .then(evictAllProductsCache());
    }

    @Transactional
    public Mono<Void> removeProduct(Long productId, String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> removeProductByUserId(productId, user.getId()));
    }

    @Transactional
    private Mono<Void> removeProductByUserId(Long productId, Long userId) {
        return cartRepository.findCartByProductIdAndUserId(productId, userId)
                .map(cart -> productRepository.increaseById(productId, cart.getQuantity()).subscribe())
                .then(cartRepository.deleteByProductId(productId))
                .then(evictAllProductsCache())
                .then();
    }

    @Transactional
    public Mono<Void> addToCart(Long productId, String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> addToCartByUserId(productId, user.getId()));
    }

    @Transactional
    private Mono<Void> addToCartByUserId(Long productId, Long userId) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    if (product.getStock() <= 0) {
                        return Mono.empty();
                    }

                    return productRepository.decreaseById(productId, 1)
                            .then(cartRepository.findCartByProductIdAndUserId(productId, userId))
                            .flatMap(existingCart -> {
                                existingCart.setQuantity(existingCart.getQuantity() + 1);
                                return cartRepository.save(existingCart);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                Cart cart = new Cart();
                                cart.setProductId(productId);
                                cart.setQuantity(1);
                                cart.setUserId(userId);
                                return cartRepository.save(cart);
                            }));
                })
                .then(evictAllProductsCache())
                .then();
    }

    @Transactional
    public Mono<Void> removeFromCart(Long productId, String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> removeFromCartByUserId(productId, user.getId()));
    }

    @Transactional
    private Mono<Void> removeFromCartByUserId(Long productId, Long userId) {
        return cartRepository.findCartByProductIdAndUserId(productId, userId)
                .flatMap(cart -> {
                    if (cart.getQuantity() == 1) {
                        return cartRepository.delete(cart)
                                .then(productRepository.increaseById(productId, 1));
                    } else {
                        cart.setQuantity(cart.getQuantity() - 1);
                        return cartRepository.save(cart)
                                .then(productRepository.increaseById(productId, 1));
                    }
                })
                .then(evictAllProductsCache())
                .then();
    }

    public Mono<Void> evictAllProductsCache() {
        return redisPageTemplate.keys("store:products:*")
                .flatMap(redisPageTemplate::delete)
                .then();
    }
}
