package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.Order;
import com.lhs.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testFindById() {
        Order order = orderService.findById(1L);
        if (order != null) {
            System.out.println("订单号：" + order.getOrderNo());
            System.out.println("订单金额：" + order.getTotalAmount());
            if (order.getItems() != null) {
                System.out.println("订单明细数：" + order.getItems().size());
            }
        }
    }

    @Test
    public void testAdd() {
        Order order = new Order();
        order.setUserId(3L);

        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("统一冰红茶");
        item.setPrice(new BigDecimal("3.50"));
        item.setQuantity(2);
        items.add(item);
        order.setItems(items);

        boolean result = orderService.add(order);
        System.out.println("下单结果：" + (result ? "成功" : "失败"));
    }

    @Test
    public void testUpdateStatus() {
        boolean result = orderService.updateStatus(1L, 1);
        System.out.println("更新订单状态：" + (result ? "成功" : "失败"));
    }

    @Test
    public void testFindPage() {
        PageResult<Order> pageResult = orderService.findPage(1, 10, null, null, null);
        System.out.println("订单总数：" + pageResult.getTotalSize());
    }

    @Test
    public void testFindByUserId() {
        List<Order> orders = orderService.findByUserId(3L);
        System.out.println("用户订单数：" + orders.size());
    }
}