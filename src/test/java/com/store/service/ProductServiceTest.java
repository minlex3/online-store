//package com.store.service;
//
//import com.store.OnlineStoreApplication;
//import com.store.dto.ProductDto;
//import com.store.entity.Product;
//import com.store.repository.ProductRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = OnlineStoreApplication.class)
//public class ProductServiceTest {
//
//    @MockitoBean
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductService productService;
//
//    @Test
//    void getAllProductsSortingByName() {
//        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);
//        when(productRepository.findByNameContainsIgnoreCaseOrderByNameAsc(any(), any())).thenReturn(
//                new PageImpl<>(List.of(product))
//        );
//
//        Page<ProductDto> page = productService.findAll(0, 10, "", "name");
//        Assertions.assertEquals(1, page.getTotalElements());
//
//        ProductDto productDto = page.getContent().getFirst();
//        Assertions.assertEquals(product.getId(), productDto.id());
//        Assertions.assertEquals(product.getName(), productDto.name());
//        Assertions.assertEquals(product.getDescription(), productDto.description());
//        Assertions.assertEquals(product.getPrice(), productDto.price());
//        Assertions.assertEquals(product.getImageUrl(), productDto.imageUrl());
//        Assertions.assertEquals(product.getStock(), productDto.stock());
//    }
//
//    @Test
//    void getAllProductsSortingByPrice() {
//        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, null);
//        when(productRepository.findByNameContainsIgnoreCaseOrderByPriceAsc(any(), any())).thenReturn(
//                new PageImpl<>(List.of(product))
//        );
//
//        Page<ProductDto> page = productService.findAll(0, 10, "", "price");
//        Assertions.assertEquals(1, page.getTotalElements());
//
//        ProductDto productDto = page.getContent().getFirst();
//        Assertions.assertEquals(product.getId(), productDto.id());
//        Assertions.assertEquals(product.getName(), productDto.name());
//        Assertions.assertEquals(product.getDescription(), productDto.description());
//        Assertions.assertEquals(product.getPrice(), productDto.price());
//        Assertions.assertEquals(product.getImageUrl(), productDto.imageUrl());
//        Assertions.assertEquals(product.getStock(), productDto.stock());
//    }
//
//    @Test
//    void findProductById() {
//        Product product = new Product(10L, "product", "desc", 10.12, "url", 3, null);
//
//        when(productRepository.findById(any())).thenReturn(Optional.of(product));
//
//        ProductDto productDto = productService.findById(10L);
//
//        Assertions.assertNotNull(productDto);
//        Assertions.assertEquals(product.getId(), productDto.id());
//        Assertions.assertEquals(product.getName(), productDto.name());
//        Assertions.assertEquals(product.getDescription(), productDto.description());
//        Assertions.assertEquals(product.getPrice(), productDto.price());
//        Assertions.assertEquals(product.getImageUrl(), productDto.imageUrl());
//        Assertions.assertEquals(product.getStock(), productDto.stock());
//        Assertions.assertEquals(product.getCartQuantity(), productDto.cartQuantity());
//
//        verify(productRepository).findById(10L);
//    }
//
//    @Test
//    void findProductNull() {
//        when(productRepository.findById(any())).thenReturn(Optional.empty());
//
//        ProductDto productDto = productService.findById(10L);
//
//        Assertions.assertNull(productDto);
//    }
//
//    @Test
//    void saveProduct() {
//        when(productRepository.save(any())).thenReturn(null);
//
//        ProductDto productDto = new ProductDto(100L, "product1", "desc", 10.56, "url", 8, 0);
//
//        productService.save(productDto);
//
//        verify(productRepository).save(any(Product.class));
//    }
//}
