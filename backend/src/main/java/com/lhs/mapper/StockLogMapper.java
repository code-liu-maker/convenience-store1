package com.lhs.mapper;

import com.lhs.model.StockLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface StockLogMapper {
    int insert(StockLog log);
    List<StockLog> findByProductId(@Param("productId") Long productId);
    List<StockLog> findPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    int countTotal();
}