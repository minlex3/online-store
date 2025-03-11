//package com.store.controller;
//
//import com.store.entity.Order;
//import com.store.repository.OrderRepository;
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
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//public class PurchaseControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Test
//    void makePurchase() throws Exception {
//        mockMvc.perform(post("/purchase"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/orders/3"));
//
//        Optional<Order> rawOrder = orderRepository.findById(3L);
//        Assertions.assertNotNull(rawOrder);
//        Assertions.assertTrue(rawOrder.isPresent());
//
//        Order order = rawOrder.get();
//        Assertions.assertEquals("Оплачен", order.getStatus());
//    }
//}
