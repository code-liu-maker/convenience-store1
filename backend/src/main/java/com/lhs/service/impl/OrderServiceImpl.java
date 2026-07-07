package com.lhs.service.impl;

import com.lhs.common.PageResult;
import com.lhs.mapper.OrderItemMapper;
import com.lhs.mapper.OrderMapper;
import com.lhs.mapper.ProductMapper;
import com.lhs.mapper.UserMapper;
import com.lhs.model.Order;
import com.lhs.model.OrderItem;
import com.lhs.model.User;
import com.lhs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Order findById(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order != null) {
            order.setItems(orderItemMapper.findByOrderId(orderId));
            User user = userMapper.findById(order.getUserId());
            if (user != null) order.setUserName(user.getUserName());
        }
        return order;
    }

    @Override
    @Transactional
    public boolean add(Order order) {
        order.setOrderNo(generateOrderNo());
        order.setOrderTime(LocalDateTime.now());
        order.setOrderStatus(0);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            total = total.add(item.getSubtotal());
        }
        order.setTotalAmount(total);

        int result = orderMapper.insert(order);
        if (result > 0 && order.getOrderId() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrderId(order.getOrderId());
                orderItemMapper.insert(item);
                productMapper.updateStock(item.getProductId(), -item.getQuantity());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStatus(Long orderId, Integer orderStatus) {
        return orderMapper.updateStatus(orderId, orderStatus) > 0;
    }

    @Override
    public PageResult<Order> findPage(int pageNum, int pageSize, Integer orderStatus, String startTime, String endTime) {
        int offset = (pageNum - 1) * pageSize;
        List<Order> list = orderMapper.findPage(offset, pageSize, orderStatus, startTime, endTime);
        int total = orderMapper.countTotal(orderStatus, startTime, endTime);

        for (Order order : list) {
            User user = userMapper.findById(order.getUserId());
            if (user != null) order.setUserName(user.getUserName());
        }
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        List<Order> list = orderMapper.findByUserId(userId);
        for (Order order : list) {
            order.setItems(orderItemMapper.findByOrderId(order.getOrderId()));
        }
        return list;
    }

    private String generateOrderNo() {
        return "DD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", new Random().nextInt(10000));
    }
}