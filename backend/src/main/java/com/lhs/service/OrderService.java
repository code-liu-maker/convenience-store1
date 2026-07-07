package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.Order;
import java.util.List;

public interface OrderService {
    Order findById(Long orderId);
    boolean add(Order order);
    boolean updateStatus(Long orderId, Integer orderStatus);
    PageResult<Order> findPage(int pageNum, int pageSize, Integer orderStatus, String startTime, String endTime);
    List<Order> findByUserId(Long userId);
}