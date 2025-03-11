//package com.store.controller;
//
//import com.store.dto.CartDto;
//import com.store.dto.ProductDto;
//import com.store.service.CartService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CartController.class)
//public class CartControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private CartService cartService;
//
//    @Test
//    void getAllProducts() throws Exception {
//        List<CartDto> expected = List.of(
//                new CartDto(
//                        10L,
//                        new ProductDto(100L, "product", "desc", 100.0, "url", 1, 0),
//                        12
//                )
//        );
//
//        when(cartService.findAll()).thenReturn(expected);
//
//        mockMvc.perform(get("/cart"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("cart-list"))
//                .andExpect(model().attributeExists("cart"))
//                .andExpect(xpath("//div[2]/div[2]/h3[1]/a[1]").string("product"));
//
//        verify(cartService).findAll();
//    }
//
//    @Test
//    void addToCart() throws Exception {
//        doNothing().when(cartService).addToCart(anyLong());
//
//        mockMvc.perform(post("/cart/add/1/products"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/products"));
//
//        verify(cartService).addToCart(1L);
//    }
//
//    @Test
//    void removeFromCart() throws Exception {
//        doNothing().when(cartService).removeFromCart(anyLong());
//
//        mockMvc.perform(post("/cart/remove/1/products"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/products"));
//
//        verify(cartService).removeFromCart(1L);
//    }
//}
