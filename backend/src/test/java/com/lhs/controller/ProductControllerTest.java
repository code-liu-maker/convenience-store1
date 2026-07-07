package com.lhs.controller;

import com.lhs.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListProducts() throws Exception {
        mockMvc.perform(get("/api/product/list")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("商品列表：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testGetProductById() throws Exception {
        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("商品详情：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setProductName("测试商品API");
        product.setCategoryId(1L);
        product.setPrice(new BigDecimal("10.00"));
        product.setSpecification("测试规格");
        product.setStockQuantity(100);
        product.setStockThreshold(20);
        product.setStatus(0);
        product.setSupplierId(1L);

        mockMvc.perform(post("/api/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("添加商品：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testLowStock() throws Exception {
        mockMvc.perform(get("/api/product/low-stock"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("库存预警：" + result.getResponse().getContentAsString()));
    }
}