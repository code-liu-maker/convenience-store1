package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.StockLog;
import java.util.List;

public interface StockLogService {
    boolean add(StockLog log);
    List<StockLog> findByProductId(Long productId);
    PageResult<StockLog> findPage(int pageNum, int pageSize);
}