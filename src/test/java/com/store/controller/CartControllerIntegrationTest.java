package com.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cart-list"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(xpath("//div[2]/div[2]/h3[1]/a[1]").string("Нагель КМП 7,5x212 100шт"));
    }

    @Test
    void addToCartRedirectProducts() throws Exception {
        mockMvc.perform(post("/cart/add/1/products"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void addToCartRedirectProductId() throws Exception {
        mockMvc.perform(post("/cart/add/1/product"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/1"));
    }

    @Test
    void addToCartRedirectCart() throws Exception {
        mockMvc.perform(post("/cart/add/1/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    void addToCartRedirectUnexpected() throws Exception {
        mockMvc.perform(post("/cart/add/1/some"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void removeFromCartRedirectProducts() throws Exception {
        mockMvc.perform(post("/cart/remove/1/products"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void removeFromCartRedirectProductId() throws Exception {
        mockMvc.perform(post("/cart/remove/1/product"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/1"));
    }

    @Test
    void removeFromCartRedirectCart() throws Exception {
        mockMvc.perform(post("/cart/remove/1/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    void removeFromCartRedirectUnexpected() throws Exception {
        mockMvc.perform(post("/cart/remove/1/some"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }
}
