package com.lhs.controller;

import com.lhs.model.Order;
import com.lhs.model.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAdminListOrders() throws Exception {
        mockMvc.perform(get("/api/order/admin/list")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("订单列表：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testAddOrder() throws Exception {
        Order order = new Order();
        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("统一冰红茶");
        item.setPrice(new BigDecimal("3.50"));
        item.setQuantity(2);
        items.add(item);
        order.setItems(items);

        mockMvc.perform(post("/api/order/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("下单结果：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        mockMvc.perform(put("/api/order/status")
                        .param("orderId", "1")
                        .param("orderStatus", "1"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("更新状态：" + result.getResponse().getContentAsString()));
    }
}