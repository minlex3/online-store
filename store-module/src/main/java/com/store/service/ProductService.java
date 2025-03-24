package com.store.service;

import com.store.dto.PageResponse;
import com.store.dto.ProductDto;
import com.store.entity.Product;
import com.store.mapper.ProductMapper;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public Mono<PageResponse<ProductDto>> searchProducts(int page, int size, String name, String sortField) {
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
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2()));
    }

    public Mono<Long> countProductsByName(String name) {
        return productRepository.countByNameContaining("%" + name + "%");
    }

    public Mono<ProductDto> findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductDto);
    }

    @Transactional
    public Mono<Void> save(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        return productRepository.save(product.getName(), product.getDescription(), product.getPrice(), product.getImageUrl(), product.getStock()).then();
    }
}
