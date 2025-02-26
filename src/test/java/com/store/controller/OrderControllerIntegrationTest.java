package com.store.controller;

import com.store.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void getAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("orders-list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(xpath("//div[2]/div").nodeCount(3))
                .andExpect(xpath("//div[2]/div[1]/h3[1]/a[1]").string("Заказ #1"))
                .andExpect(xpath("//div[2]/div[2]/h3[1]/a[1]").string("Заказ #2"));
    }

    @Test
    void getOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("order-description"))
                .andExpect(model().attributeExists("order"))
                .andExpect(xpath("//div[2]/div").nodeCount(1))
                .andExpect(xpath("//div[2]/div[1]/a[1]").string("Молоток-гвоздодер GROSS"))
                .andExpect(xpath("//div[2]/div[1]/p[2]").string("Стоимость: 699.99"));
    }
}
