package com.store.mapper;

import com.store.dto.ProductDto;
import com.store.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDto toProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImage_url(),
                product.getStock()
        );
    }

    public Product toProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.id());
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setImage_url(productDto.image_url());
        product.setStock(productDto.stock());
        return product;
    }
}
