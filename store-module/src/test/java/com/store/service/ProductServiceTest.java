package com.store.service;

import com.store.OnlineStoreApplication;
import com.store.dto.PageResponse;
import com.store.dto.ProductDto;
import com.store.entity.Product;
import com.store.mapper.ProductMapper;
import com.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = OnlineStoreApplication.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ReactiveRedisTemplate<String, PageResponse> redisPageTemplate;

    @Mock
    private ReactiveValueOperations<String, PageResponse> valueOperations;

    @Mock
    private ReactiveRedisTemplate<String, ProductDto> redisProductTemplate;

    @Mock
    private ReactiveValueOperations<String, ProductDto> productValueOperations;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProductsSortingByName() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        ProductDto productDto = new ProductDto(10L, "product", "desc", 12.34, "url", 15, 0);

        when(redisPageTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(Mono.empty());
        when(valueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        when(productMapper.toProductDto(any())).thenReturn(productDto);

        when(productRepository.findByNameContainingOrderByName(any(), anyInt(), anyInt())).thenReturn(Flux.just(product));
        when(productRepository.countByNameContaining(any())).thenReturn(Mono.just(7L));

        Mono<PageResponse> result = productService.searchProducts(0, 10, "", "name");

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.totalElements() == 7
                                && el.data().size() == 1
                                && el.data().getFirst().id().equals(product.getId())
                                && el.data().getFirst().name().equals(product.getName())
                                && el.data().getFirst().price().equals(product.getPrice())
                )
                .expectComplete()
                .verify();

        verify(productRepository).findByNameContainingOrderByName("%%", 10, 0);
        verify(productRepository).countByNameContaining("%%");
    }

    @Test
    void getAllProductsSortingByNameFromCache() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        ProductDto productDto = new ProductDto(10L, "product", "desc", 12.34, "url", 15, 0);

        when(redisPageTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(Mono.just(new PageResponse(List.of(productDto), 7L)));
        when(valueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        when(productMapper.toProductDto(any())).thenReturn(productDto);

        when(productRepository.findByNameContainingOrderByName(any(), anyInt(), anyInt())).thenReturn(Flux.just(product));
        when(productRepository.countByNameContaining(any())).thenReturn(Mono.just(7L));

        Mono<PageResponse> result = productService.searchProducts(0, 10, "", "name");

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.totalElements() == 7
                                && el.data().size() == 1
                                && el.data().getFirst().id().equals(product.getId())
                                && el.data().getFirst().name().equals(product.getName())
                                && el.data().getFirst().price().equals(product.getPrice())
                )
                .expectComplete()
                .verify();

        verify(productRepository, never()).findByNameContainingOrderByName("%%", 10, 0);
        verify(productRepository, never()).countByNameContaining("%%");
        verify(valueOperations, never()).set(any(), any(), any());
    }

    @Test
    void getAllProductsSortingByPrice() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        ProductDto productDto = new ProductDto(10L, "product", "desc", 12.34, "url", 15, 0);

        when(redisPageTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(Mono.empty());
        when(valueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        when(productMapper.toProductDto(any())).thenReturn(productDto);

        when(productRepository.findByNameContainingOrderByPrice(any(), anyInt(), anyInt())).thenReturn(Flux.just(product));
        when(productRepository.countByNameContaining(any())).thenReturn(Mono.just(7L));

        Mono<PageResponse> result = productService.searchProducts(0, 10, "", "price");

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.totalElements() == 7
                                && el.data().size() == 1
                                && el.data().getFirst().id().equals(product.getId())
                                && el.data().getFirst().name().equals(product.getName())
                                && el.data().getFirst().price().equals(product.getPrice())
                )
                .expectComplete()
                .verify();

        verify(productRepository).findByNameContainingOrderByPrice("%%", 10, 0);
        verify(productRepository).countByNameContaining("%%");
    }

    @Test
    void getAllProductsSortingByPriceFromCache() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        ProductDto productDto = new ProductDto(10L, "product", "desc", 12.34, "url", 15, 0);

        when(redisPageTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(Mono.just(new PageResponse(List.of(productDto), 7L)));
        when(valueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        when(productMapper.toProductDto(any())).thenReturn(productDto);

        when(productRepository.findByNameContainingOrderByPrice(any(), anyInt(), anyInt())).thenReturn(Flux.just(product));
        when(productRepository.countByNameContaining(any())).thenReturn(Mono.just(7L));

        Mono<PageResponse> result = productService.searchProducts(0, 10, "", "price");

        StepVerifier.create(result)
                .expectNextMatches(
                        el -> el.totalElements() == 7
                                && el.data().size() == 1
                                && el.data().getFirst().id().equals(product.getId())
                                && el.data().getFirst().name().equals(product.getName())
                                && el.data().getFirst().price().equals(product.getPrice())
                )
                .expectComplete()
                .verify();

        verify(productRepository, never()).findByNameContainingOrderByPrice("%%", 10, 0);
        verify(productRepository, never()).countByNameContaining("%%");
        verify(valueOperations, never()).set(any(), any(), any());
    }

    @Test
    void findProductById() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        ProductDto productDto = new ProductDto(10L, "product", "desc", 12.34, "url", 15, 0);

        when(redisProductTemplate.opsForValue()).thenReturn(productValueOperations);
        when(productValueOperations.get(any())).thenReturn(Mono.empty());
        when(productValueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        when(productMapper.toProductDto(any())).thenReturn(productDto);

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
    void findProductByIdFromCache() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        ProductDto productDto = new ProductDto(10L, "product", "desc", 12.34, "url", 15, 0);

        when(redisProductTemplate.opsForValue()).thenReturn(productValueOperations);
        when(productValueOperations.get(any())).thenReturn(Mono.just(productDto));
        when(productValueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        when(productMapper.toProductDto(any())).thenReturn(productDto);

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

        verify(productRepository, never()).findById(10L);
        verify(productValueOperations, never()).set(any(), any(), any());
    }

    @Test
    void saveProduct() {
        Product product = new Product(10L, "product", "desc", 12.34, "url", 15, 0);
        ProductDto productDto = new ProductDto(10L, "product", "desc", 12.34, "url", 15, 0);

        when(productRepository.save(anyString(), anyString(), anyDouble(), anyString(), anyInt())).thenReturn(Mono.just(new Product()));
        when(redisPageTemplate.keys(anyString())).thenReturn(Flux.fromIterable(List.of("key1", "key2")));
        when(redisPageTemplate.delete(anyString())).thenReturn(Mono.just(1L));

        when(productMapper.toProduct(any())).thenReturn(product);


        Mono<Void> result = productService.save(productDto);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).save("product", "desc", 12.34d, "url", 15);
        verify(redisPageTemplate).keys("store:products:*");
        verify(redisPageTemplate).delete("key1");
        verify(redisPageTemplate).delete("key2");
    }
}
