package com.lhs.mapper;

import com.lhs.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testFindById() {
        Order order = orderMapper.findById(1L);
        if (order != null) {
            System.out.println("订单号：" + order.getOrderNo());
            System.out.println("订单金额：" + order.getTotalAmount());
            System.out.println("订单状态：" + order.getOrderStatus());
        }
    }

    @Test
    public void testInsert() {
        Order order = new Order();
        order.setOrderNo("TEST202412250001");
        order.setUserId(1L);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setOrderStatus(0);
        order.setRemark("测试订单");

        int result = orderMapper.insert(order);
        System.out.println("插入结果：" + (result > 0 ? "成功" : "失败"));
        if (result > 0) {
            System.out.println("订单ID：" + order.getOrderId());
        }
    }

    @Test
    public void testUpdateStatus() {
        int result = orderMapper.updateStatus(1L, 1);
        System.out.println("状态更新结果：" + (result > 0 ? "成功" : "失败"));
    }

    @Test
    public void testFindPage() {
        List<Order> orders = orderMapper.findPage(0, 5, null, null, null);
        System.out.println("订单总数：" + orders.size());
    }

    @Test
    public void testFindByUserId() {
        List<Order> orders = orderMapper.findByUserId(3L);
        System.out.println("用户订单数：" + orders.size());
    }
}