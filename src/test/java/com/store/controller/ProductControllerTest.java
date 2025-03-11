//package com.store.controller;
//
//import com.store.dto.ProductDto;
//import com.store.service.ProductService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ProductController.class)
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private ProductService productService;
//
//    @Test
//    void getAllProducts() throws Exception {
//        Page<ProductDto> expected = new PageImpl<>(List.of(
//                new ProductDto(100L, "product1", "desc", 10.56, "url", 8, 0),
//                new ProductDto(200L, "product2", "desc2", 10.57, "url2", 9, 0)
//        ), PageRequest.of(0, 10), 1);
//
//        when(productService.findAll(anyInt(), anyInt(), any(), any())).thenReturn(expected);
//
//        mockMvc.perform(get("/products"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("products-list"))
//                .andExpect(model().attributeExists("products"))
//                .andExpect(xpath("//div[5]/div").nodeCount(2))
//                .andExpect(xpath("//div[5]/div[1]/h3[1]/a[1]").string("product1"))
//                .andExpect(xpath("//div[5]/div[2]/h3[1]/a[1]").string("product2"));
//
//        verify(productService).findAll(0, 10, "", "name");
//    }
//
//    @Test
//    void getProductById() throws Exception {
//        ProductDto expected = new ProductDto(100L, "product1", "desc", 10.56, "url", 8, 0);
//
//        when(productService.findById(any())).thenReturn(expected);
//
//        mockMvc.perform(get("/products/100"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("product-description"))
//                .andExpect(model().attributeExists("product"))
//                .andExpect(xpath("//h2[1]").string("product1"));
//
//        verify(productService).findById(100L);
//    }
//
//    @Test
//    void saveProduct() throws Exception {
//        doNothing().when(productService).save(any());
//
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
//        verify(productService).save(
//                new ProductDto(null, "productName", "desc", 10.1, "url", 10, 0)
//        );
//    }
//}
