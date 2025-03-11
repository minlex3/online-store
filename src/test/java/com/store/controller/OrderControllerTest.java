//package com.store.controller;
//
//import com.store.dto.OrderDto;
//import com.store.dto.OrderItemDto;
//import com.store.dto.ProductDto;
//import com.store.service.OrderService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrderController.class)
//public class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private OrderService orderService;
//
//    @Test
//    void getAllOrders() throws Exception {
//        List<OrderDto> expected = List.of(
//                new OrderDto(40L, 123.45, "Paid", List.of()),
//                new OrderDto(50L, 321.45, "Prepared", List.of())
//        );
//
//        when(orderService.findAll()).thenReturn(expected);
//
//        mockMvc.perform(get("/orders"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("orders-list"))
//                .andExpect(model().attributeExists("orders"))
//                .andExpect(xpath("//div[2]/div").nodeCount(2))
//                .andExpect(xpath("//div[2]/div[1]/h3[1]/a[1]").string("Заказ #40"))
//                .andExpect(xpath("//div[2]/div[2]/h3[1]/a[1]").string("Заказ #50"));
//
//        verify(orderService).findAll();
//    }
//
//    @Test
//    void getOrderById() throws Exception {
//        OrderDto expected = new OrderDto(40L, 123.45, "Paid", List.of(
//                new OrderItemDto(15L,
//                        new ProductDto(100L, "product", "desc", 100.0, "url", 1, 0),
//                        1, 112.43)
//        ));
//
//        when(orderService.findById(any())).thenReturn(expected);
//
//        mockMvc.perform(get("/orders/40"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("order-description"))
//                .andExpect(model().attributeExists("order"))
//                .andExpect(xpath("//div[2]/div").nodeCount(1))
//                .andExpect(xpath("//div[2]/div[1]/a[1]").string("product"))
//                .andExpect(xpath("//div[2]/div[1]/p[2]").string("Стоимость: 112.43"));
//
//        verify(orderService).findById(40L);
//    }
//}
