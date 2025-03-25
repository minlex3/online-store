package com.store.controller;

import com.store.dto.PageResponse;
import com.store.dto.ProductDto;
import com.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductService productService;

    @Test
    void getAllProducts() {
        PageResponse expected = new PageResponse(List.of(
                new ProductDto(100L, "product1", "desc", 10.56, "url", 8, 0),
                new ProductDto(200L, "product2", "desc2", 10.57, "url2", 9, 0)
        ), 2L);

        when(productService.searchProducts(anyInt(), anyInt(), any(), any())).thenReturn(Mono.just(expected));

        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody().xpath("//div[5]/div").nodeCount(2)
                .xpath("//div[5]/div[1]/h3[1]/a[1]", "product1");

        verify(productService).searchProducts(0, 10, "", "name");
    }

    @Test
    void getProductById() {
        ProductDto expected = new ProductDto(100L, "product1", "desc", 10.56, "url", 8, 0);

        when(productService.findById(any())).thenReturn(Mono.just(expected));

        webTestClient.get()
                .uri("/products/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody().xpath("//h2[1]", "product1");

        verify(productService).findById(100L);
    }

    @Test
    void saveProduct() {
        when(productService.save(any())).thenReturn(Mono.empty());

        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", "name");
        formData.add("description", "desc");
        formData.add("price", "12.34");
        formData.add("imageUrl", "url");
        formData.add("stock", "10");
        formData.add("cartQuantity", "0");

        webTestClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/products");

        verify(productService).save(
                new ProductDto(null, "name", "desc", 12.34, "url", 10, 0)
        );
    }
}
