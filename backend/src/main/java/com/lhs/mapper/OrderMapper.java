package com.lhs.mapper;

import com.lhs.model.Order;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OrderMapper {
    Order findById(@Param("orderId") Long orderId);
    int insert(Order order);
    int updateStatus(@Param("orderId") Long orderId, @Param("orderStatus") Integer orderStatus);
    List<Order> findPage(@Param("offset") int offset,
                         @Param("pageSize") int pageSize,
                         @Param("orderStatus") Integer orderStatus,
                         @Param("startTime") String startTime,
                         @Param("endTime") String endTime);
    int countTotal(@Param("orderStatus") Integer orderStatus,
                   @Param("startTime") String startTime,
                   @Param("endTime") String endTime);
    List<Order> findByUserId(@Param("userId") Long userId);
}