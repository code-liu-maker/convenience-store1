package com.lhs.controller;

import com.lhs.model.User;
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
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginSuccess() throws Exception {
        User user = new User();
        user.setLoginId("admin");
        user.setPassword("123456");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andDo(result -> System.out.println("登录成功：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testLoginFailure() throws Exception {
        User user = new User();
        user.setLoginId("admin");
        user.setPassword("wrongpassword");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("500"))
                .andDo(result -> System.out.println("登录失败：" + result.getResponse().getContentAsString()));
    }

    @Test
    public void testRegister() throws Exception {
        User user = new User();
        user.setLoginId("newuser");
        user.setPassword("123456");
        user.setUserName("新用户");
        user.setMobile("13800138999");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("注册结果：" + result.getResponse().getContentAsString()));
    }
}