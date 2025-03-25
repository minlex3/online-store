package com.store.service;

import com.store.dto.PageResponse;
import com.store.dto.ProductDto;
import com.store.entity.Product;
import com.store.mapper.ProductMapper;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ReactiveRedisTemplate<String, ProductDto> redisProductTemplate;

    @Autowired
    private ReactiveRedisTemplate<String, PageResponse> redisPageTemplate;

    public Mono<PageResponse> searchProductsInDB(int page, int size, String name, String sortField) {
        int offset = page * size;

        Flux<ProductDto> products;

        if (Objects.equals(sortField, "name")) {
            products = productRepository.findByNameContainingOrderByName("%" + name + "%", size, offset)
                    .map(productMapper::toProductDto);
        } else {
            products = productRepository.findByNameContainingOrderByPrice("%" + name + "%", size, offset)
                    .map(productMapper::toProductDto);
        }

        Mono<Long> totalCount = this.countProductsByName(name);

        return Mono.zip(products.collectList(), totalCount)
                .map(tuple -> new PageResponse(tuple.getT1(), tuple.getT2()));
    }

    public Mono<Long> countProductsByName(String name) {
        return productRepository.countByNameContaining("%" + name + "%");
    }

    public Mono<PageResponse> searchProducts(int page, int size, String name, String sortField) {
        String cacheName = "store:products:" + page + ":" + size + ":" + name + ":" + sortField;

        return redisPageTemplate.opsForValue().get(cacheName)
                .switchIfEmpty(Mono.defer(() -> searchProductsInDB(page, size, name, sortField)
                        .flatMap(response ->
                                redisPageTemplate.opsForValue()
                                        .set(cacheName, response, Duration.ofMinutes(1))
                                        .thenReturn(response)
                        ))

                );
    }

    public Mono<ProductDto> findById(Long id) {
        String cacheName = "store:product:";

        return redisProductTemplate.opsForValue().get(cacheName + id)
                .switchIfEmpty(Mono.defer(() -> productRepository.findById(id)
                                .map(productMapper::toProductDto)
                                .flatMap(productDto ->
                                        redisProductTemplate.opsForValue()
                                                .set(cacheName + id, productDto, Duration.ofMinutes(1))
                                                .thenReturn(productDto)
                                )
                        )

                );
    }

    @Transactional
    public Mono<Void> save(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);

        return productRepository.save(product.getName(), product.getDescription(), product.getPrice(), product.getImageUrl(), product.getStock())
                .then(evictAllProductsCache())
                .then();
    }

    public Mono<Void> evictAllProductsCache() {
        return redisPageTemplate.keys("store:products:*")
                .flatMap(redisPageTemplate::delete)
                .then();
    }
}
