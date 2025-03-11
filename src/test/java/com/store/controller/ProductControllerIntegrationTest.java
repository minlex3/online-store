//package com.store.controller;
//
//import com.store.entity.Product;
//import com.store.repository.ProductRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Optional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//public class ProductControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Test
//    void getAllProducts() throws Exception {
//        mockMvc.perform(get("/products"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("products-list"))
//                .andExpect(model().attributeExists("products"))
//                .andExpect(xpath("//div[5]/div").nodeCount(6))
//                .andExpect(xpath("//div[5]/div[1]/h3[1]/a[1]").string("Дрель-шуруповерт Makita CXT DF333DWAE"))
//                .andExpect(xpath("//div[5]/div[2]/h3[1]/a[1]").string("Молоток-гвоздодер GROSS"));
//    }
//
//    @Test
//    void getProductById() throws Exception {
//        mockMvc.perform(get("/products/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("product-description"))
//                .andExpect(model().attributeExists("product"))
//                .andExpect(xpath("//h2[1]").string("Молоток-гвоздодер GROSS"));
//    }
//
//    @Test
//    void saveProduct() throws Exception {
//        mockMvc.perform(post("/products")
//                        .param("name", "productName")
//                        .param("description", "desc")
//                        .param("price", "10.1")
//                        .param("imageUrl", "url")
//                        .param("stock", "10")
//                        .param("cartQuantity", "0")
//                )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/products"));
//
//        Optional<Product> rawProduct = productRepository.findByName("productName");
//        Assertions.assertNotNull(rawProduct);
//        Assertions.assertTrue(rawProduct.isPresent());
//
//        Product product = rawProduct.get();
//        Assertions.assertEquals(10.1, product.getPrice());
//        Assertions.assertEquals(10, product.getStock());
//    }
//}
