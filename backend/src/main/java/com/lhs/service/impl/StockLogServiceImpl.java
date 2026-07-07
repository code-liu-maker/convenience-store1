package com.lhs.service.impl;

import com.lhs.common.PageResult;
import com.lhs.mapper.ProductMapper;
import com.lhs.mapper.StockLogMapper;
import com.lhs.model.Product;
import com.lhs.model.StockLog;
import com.lhs.service.StockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockLogServiceImpl implements StockLogService {

    @Autowired
    private StockLogMapper stockLogMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public boolean add(StockLog log) {
        log.setOperationTime(LocalDateTime.now());
        Product product = productMapper.findById(log.getProductId());
        if (product != null) {
            log.setProductName(product.getProductName());
        }
        return stockLogMapper.insert(log) > 0;
    }

    @Override
    public List<StockLog> findByProductId(Long productId) {
        List<StockLog> list = stockLogMapper.findByProductId(productId);
        for (StockLog log : list) {
            Product product = productMapper.findById(log.getProductId());
            if (product != null) log.setProductName(product.getProductName());
        }
        return list;
    }

    @Override
    public PageResult<StockLog> findPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<StockLog> list = stockLogMapper.findPage(offset, pageSize);
        int total = stockLogMapper.countTotal();
        for (StockLog log : list) {
            Product product = productMapper.findById(log.getProductId());
            if (product != null) log.setProductName(product.getProductName());
        }
        return new PageResult<>(pageNum, pageSize, total, list);
    }
}