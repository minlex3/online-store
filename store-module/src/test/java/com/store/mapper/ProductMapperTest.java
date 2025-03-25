package com.store.mapper;

import com.store.dto.ProductDto;
import com.store.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductMapperTest {

    private final ProductMapper productMapper = new ProductMapper();

    @Test
    public void testToProductDto() {
        Product product = new Product(20L, "product", "desc", 10.12, "url", 3, 0);

        ProductDto productDto = productMapper.toProductDto(product);

        Assertions.assertEquals(product.getId(), productDto.id());
        Assertions.assertEquals(product.getName(), productDto.name());
        Assertions.assertEquals(product.getDescription(), productDto.description());
        Assertions.assertEquals(product.getPrice(), productDto.price());
        Assertions.assertEquals(product.getImageUrl(), productDto.imageUrl());
        Assertions.assertEquals(product.getStock(), productDto.stock());
        Assertions.assertEquals(product.getCartQuantity(), productDto.cartQuantity());
    }

    @Test
    public void testToProduct() {
        ProductDto productDto = new ProductDto(100L, "product1", "desc", 10.56, "url", 8, 0);

        Product product = productMapper.toProduct(productDto);

        Assertions.assertEquals(productDto.id(), product.getId());
        Assertions.assertEquals(productDto.name(), product.getName());
        Assertions.assertEquals(productDto.description(), product.getDescription());
        Assertions.assertEquals(productDto.price(), product.getPrice());
        Assertions.assertEquals(productDto.imageUrl(), product.getImageUrl());
        Assertions.assertEquals(productDto.stock(), product.getStock());
        Assertions.assertEquals(productDto.cartQuantity(), product.getCartQuantity());
    }
}
