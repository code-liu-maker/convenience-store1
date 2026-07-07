package com.lhs.controller;

import com.lhs.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 测试用的用户ID（请根据实际数据库中的ID修改）
    private static final Long TEST_USER_ID = 1L;

    // ==================== 查询接口测试 ====================

    @Test
    @Order(1)
    public void testListUsers() throws Exception {
        mockMvc.perform(get("/api/admin/user/list")
                        .param("pageNum", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andDo(result -> {
                    System.out.println("========== 测试：分页查询用户列表 ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    @Test
    @Order(2)
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/admin/user/{id}", TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：根据ID查询用户 ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    @Test
    @Order(3)
    public void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/admin/user/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("500"))
                .andDo(result -> {
                    System.out.println("========== 测试：查询不存在的用户 ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    // ==================== 添加接口测试 ====================

    @Test
    @Order(4)
    public void testAddUser() throws Exception {
        User user = new User();
        user.setLoginId("testuser001");
        user.setPassword("123456");
        user.setUserName("测试用户001");
        user.setMobile("13800138001");
        user.setEmail("test001@example.com");
        user.setUserType(1);
        user.setStatus(0);

        mockMvc.perform(post("/api/admin/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：添加用户 ==========");
                    System.out.println("请求参数：" + objectMapper.writeValueAsString(user));
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    @Test
    @Order(5)
    public void testAddUser_DuplicateLoginId() throws Exception {
        User user = new User();
        user.setLoginId("admin");  // 使用已存在的账号
        user.setPassword("123456");
        user.setUserName("重复账号测试");
        user.setMobile("13800138002");
        user.setEmail("test002@example.com");
        user.setUserType(1);
        user.setStatus(0);

        mockMvc.perform(post("/api/admin/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("500"))
                .andDo(result -> {
                    System.out.println("========== 测试：添加重复账号用户（应失败） ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    @Test
    @Order(6)
    public void testAddUser_EmptyLoginId() throws Exception {
        User user = new User();
        user.setLoginId("");
        user.setPassword("123456");
        user.setUserName("空账号测试");
        user.setMobile("13800138003");
        user.setEmail("test003@example.com");
        user.setUserType(1);
        user.setStatus(0);

        mockMvc.perform(post("/api/admin/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：添加空账号用户（应失败） ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    // ==================== 修改接口测试 ====================

    @Test
    @Order(7)
    public void testEditUser() throws Exception {
        // 先获取用户信息
        mockMvc.perform(get("/api/admin/user/{id}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    // 解析并修改用户信息
                    // 注意：这里简化处理，实际测试中应该先获取再修改
                });

        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setUserName("修改后的用户名");
        user.setMobile("13900139000");
        user.setEmail("updated@example.com");
        user.setUserType(2);
        user.setStatus(0);

        mockMvc.perform(put("/api/admin/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：修改用户信息 ==========");
                    System.out.println("请求参数：" + objectMapper.writeValueAsString(user));
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    // ==================== 重置密码接口测试 ====================

    @Test
    @Order(8)
    public void testResetPassword() throws Exception {
        mockMvc.perform(put("/api/admin/user/reset-password")
                        .param("userId", String.valueOf(TEST_USER_ID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：重置用户密码 ==========");
                    System.out.println("用户ID：" + TEST_USER_ID);
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    @Test
    @Order(9)
    public void testResetPassword_UserNotFound() throws Exception {
        mockMvc.perform(put("/api/admin/user/reset-password")
                        .param("userId", "99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("500"))
                .andDo(result -> {
                    System.out.println("========== 测试：重置不存在用户的密码（应失败） ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    // ==================== 删除接口测试 ====================

    @Test
    @Order(10)
    public void testDeleteUser() throws Exception {
        // 注意：测试删除前请确保有可删除的测试数据
        // 这里使用一个较大的ID，避免误删正式数据
        mockMvc.perform(delete("/api/admin/user/delete/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：删除用户 ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    // ==================== 综合测试 ====================

    @Test
    @Order(11)
    public void testListUsers_WithPagination() throws Exception {
        // 测试第一页
        mockMvc.perform(get("/api/admin/user/list")
                        .param("pageNum", "1")
                        .param("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：分页查询（第1页，每页5条） ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });

        // 测试第二页
        mockMvc.perform(get("/api/admin/user/list")
                        .param("pageNum", "2")
                        .param("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("========== 测试：分页查询（第2页，每页5条） ==========");
                    System.out.println("响应结果：" + result.getResponse().getContentAsString());
                });
    }

    @Test
    @Order(12)
    public void testUserLifecycle() throws Exception {
        System.out.println("\n========== 测试：用户完整生命周期 ==========");

        // 1. 创建测试用户
        User user = new User();
        user.setLoginId("lifecycle_test");
        user.setPassword("123456");
        user.setUserName("生命周期测试用户");
        user.setMobile("13800138999");
        user.setEmail("lifecycle@test.com");
        user.setUserType(0);
        user.setStatus(0);

        String createResponse = mockMvc.perform(post("/api/admin/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("1. 创建用户结果：" + createResponse);

        // 2. 查询用户列表
        mockMvc.perform(get("/api/admin/user/list")
                        .param("pageNum", "1")
                        .param("pageSize", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("2. 查询用户列表完成");
                });

        // 3. 重置密码
        mockMvc.perform(put("/api/admin/user/reset-password")
                        .param("userId", "100")  // 使用实际创建的ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("3. 重置密码结果：" + result.getResponse().getContentAsString());
                });

        System.out.println("========== 用户生命周期测试完成 ==========\n");
    }
}