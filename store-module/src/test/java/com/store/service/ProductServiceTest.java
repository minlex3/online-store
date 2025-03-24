package com.store.service;

import com.store.OnlineStoreApplication;
import com.store.dto.PageResponse;
import com.store.dto.ProductDto;
import com.store.entity.Product;
import com.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OnlineStoreApplication.class)
public class ProductServiceTest {

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void getAllProductsSortingByName() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);

        when(productRepository.findByNameContainingOrderByName(any(), anyInt(), anyInt())).thenReturn(Flux.just(product));
        when(productRepository.countByNameContaining(any())).thenReturn(Mono.just(7L));

        Mono<PageResponse<ProductDto>> result = productService.searchProducts(0, 10, "", "name");

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.getTotalElements() == 7
                                && el.getData().size() == 1
                                && el.getData().getFirst().id().equals(product.getId())
                                && el.getData().getFirst().name().equals(product.getName())
                                && el.getData().getFirst().price().equals(product.getPrice())
                )
                .expectComplete()
                .verify();

        verify(productRepository).findByNameContainingOrderByName("%%", 10, 0);
        verify(productRepository).countByNameContaining("%%");
    }

    @Test
    void getAllProductsSortingByPrice() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);

        when(productRepository.findByNameContainingOrderByPrice(any(), anyInt(), anyInt())).thenReturn(Flux.just(product));
        when(productRepository.countByNameContaining(any())).thenReturn(Mono.just(7L));

        Mono<PageResponse<ProductDto>> result = productService.searchProducts(0, 10, "", "price");

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.getTotalElements() == 7
                                && el.getData().size() == 1
                                && el.getData().getFirst().id().equals(product.getId())
                                && el.getData().getFirst().name().equals(product.getName())
                                && el.getData().getFirst().price().equals(product.getPrice())
                )
                .expectComplete()
                .verify();

        verify(productRepository).findByNameContainingOrderByPrice("%%", 10, 0);
        verify(productRepository).countByNameContaining("%%");
    }

    @Test
    void findProductById() {
        Product product = new Product(10L, "product", "desc", 10.12, "url", 3, 0);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(product));

        Mono<ProductDto> result = productService.findById(10L);

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.id().equals(product.getId())
                                && el.name().equals(product.getName())
                                && el.price().equals(product.getPrice())
                )
                .expectComplete()
                .verify();

        verify(productRepository).findById(10L);
    }

    @Test
    void saveProduct() {
        when(productRepository.save(anyString(), anyString(), anyDouble(), anyString(), anyInt())).thenReturn(Mono.just(new Product()));

        ProductDto productDto = new ProductDto(100L, "product1", "desc", 10.56, "url", 8, 0);

        Mono<Void> result = productService.save(productDto);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).save("product1", "desc", 10.56d, "url", 8);
    }
}
