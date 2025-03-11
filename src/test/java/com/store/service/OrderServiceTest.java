//package com.store.service;
//
//import com.store.OnlineStoreApplication;
//import com.store.dto.OrderDto;
//import com.store.entity.Order;
//import com.store.repository.OrderRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = OnlineStoreApplication.class)
//public class OrderServiceTest {
//
//    @MockitoBean
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Test
//    void findAllOrders() {
//        Order order = new Order(10L, 12.34, "Paid", List.of());
//        when(orderRepository.findAll()).thenReturn(List.of(order));
//
//        List<OrderDto> orderDtoList = orderService.findAll();
//        Assertions.assertEquals(1, orderDtoList.size());
//
//        OrderDto orderDto = orderDtoList.getFirst();
//        Assertions.assertEquals(order.getId(), orderDto.id());
//        Assertions.assertEquals(order.getTotalAmount(), orderDto.totalAmount());
//        Assertions.assertEquals(order.getStatus(), orderDto.status());
//        Assertions.assertEquals(0, orderDto.orderItems().size());
//    }
//
//    @Test
//    void findOrderById() {
//        Order order = new Order(10L, 12.34, "Paid", List.of());
//        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
//
//        OrderDto orderDto = orderService.findById(10L);
//
//        Assertions.assertNotNull(orderDto);
//        Assertions.assertEquals(order.getId(), orderDto.id());
//        Assertions.assertEquals(order.getTotalAmount(), orderDto.totalAmount());
//        Assertions.assertEquals(order.getStatus(), orderDto.status());
//        Assertions.assertEquals(0, orderDto.orderItems().size());
//    }
//
//    @Test
//    void findEmptyOrderById() {
//        when(orderRepository.findById(any())).thenReturn(Optional.empty());
//
//        OrderDto orderDto = orderService.findById(10L);
//
//        Assertions.assertNull(orderDto);
//    }
//}
