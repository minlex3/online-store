//package com.store.controller;
//
//import com.store.service.PurchaseService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(PurchaseController.class)
//public class PurchaseControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private PurchaseService purchaseService;
//
//    @Test
//    void makeAllowedPurchase() throws Exception {
//        when(purchaseService.isPurchaseAllowed()).thenReturn(true);
//        when(purchaseService.makePurchase()).thenReturn(10L);
//
//        mockMvc.perform(post("/purchase"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/orders/10"));
//
//        verify(purchaseService).isPurchaseAllowed();
//        verify(purchaseService).makePurchase();
//    }
//
//    @Test
//    void makeNotAllowedPurchase() throws Exception {
//        when(purchaseService.isPurchaseAllowed()).thenReturn(false);
//        when(purchaseService.makePurchase()).thenReturn(10L);
//
//        mockMvc.perform(post("/purchase"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/cart"));
//
//        verify(purchaseService).isPurchaseAllowed();
//        verify(purchaseService, never()).makePurchase();
//    }
//}
