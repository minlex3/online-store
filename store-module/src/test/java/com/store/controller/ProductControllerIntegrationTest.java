package com.store.controller;

import com.store.configuration.EmbeddedRedisConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(EmbeddedRedisConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerIntegrationTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getAllProducts() {
        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .xpath("//div[5]/div").nodeCount(5)
                .xpath("//div[5]/div[1]/h3[1]/a[1]", "Дрель-шуруповерт Makita CXT DF333DWAE");
    }

    @Test
    void getProductById() {
        webTestClient.get()
                .uri("/products/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .xpath("//h2[1]", "Молоток-гвоздодер GROSS");
    }

    @Test
    void saveProduct() {
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
                .expectHeader().location("/products")
                .expectStatus().is3xxRedirection();
    }
}
