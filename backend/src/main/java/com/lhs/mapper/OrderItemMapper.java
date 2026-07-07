package com.lhs.mapper;

import com.lhs.model.OrderItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OrderItemMapper {
    int insert(OrderItem item);
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);
}