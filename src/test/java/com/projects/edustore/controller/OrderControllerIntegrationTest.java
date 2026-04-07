package com.projects.edustore.controller;

import com.projects.edustore.model.product.Order;
import com.projects.edustore.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @WithMockUser(username = "Alice", roles = {"CUSTOMER"})
    void shouldWriteCartToRightOrder() throws Exception {

        mockMvc.perform(post("/orders/customer/{customerId}", 4L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))

                .andExpect(jsonPath("$.items[0].productId").value(1))
                .andExpect(jsonPath("$.items[0].productName").value("Vogelhuisje"))
                .andExpect(jsonPath("$.items[0].price").value(1.00))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].subtotal").value(2.00))

                .andExpect(jsonPath("$.items[1].productId").value(2))
                .andExpect(jsonPath("$.items[1].productName").value("Schilderij"))
                .andExpect(jsonPath("$.items[1].price").value(5.00))
                .andExpect(jsonPath("$.items[1].quantity").value(1))
                .andExpect(jsonPath("$.items[1].subtotal").value(5.00))
                .andReturn();

        List<Order> orders = orderRepository.findAll();
        System.out.println("number of orders: " + orders.size());
        assertEquals(1, orders.size());
        assertEquals(4L, orders.get(0).getCustomer().getId());
        assertEquals(new BigDecimal("7.00"), orders.get(0).getTotalPrice());
    }

}