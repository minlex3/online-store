package com.store.service;

import com.store.dto.ProductDto;
import com.store.entity.Product;
import com.store.mapper.ProductMapper;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public Page<ProductDto> findAll(int page, int size, String name, String sortBy) {
        Page<Product> productPage;
        if (Objects.equals(sortBy, "name")) {
            productPage = productRepository.findByNameContainsIgnoreCaseOrderByNameAsc(name, PageRequest.of(page, size, Sort.by("name").ascending()));
        } else {
            productPage = productRepository.findByNameContainsIgnoreCaseOrderByPriceAsc(name, PageRequest.of(page, size, Sort.by("price").ascending()));
        }

        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(productMapper::toProductDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, PageRequest.of(page, size), productPage.getTotalPages());
    }

    public ProductDto findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> productMapper.toProductDto(value)).orElse(null);
    }

    public void save(ProductDto productDto) {
        productRepository.save(productMapper.toProduct(productDto));
    }
}
