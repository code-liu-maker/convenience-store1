package com.lhs.controller;

import com.lhs.model.Supplier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListSuppliers() throws Exception {
        mockMvc.perform(get("/api/supplier/list")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("供应商列表：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testAllSuppliers() throws Exception {
        mockMvc.perform(get("/api/supplier/all"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("所有供应商：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testAddSupplier() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setSupplierName("测试供应商API");
        supplier.setContactPerson("李经理");
        supplier.setContactPhone("13800138000");
        supplier.setSupplyCategory("测试");
        supplier.setCooperationStatus(0);

        mockMvc.perform(post("/api/supplier/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("添加供应商：" + result.getResponse().getContentAsString()));
    }
}