package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.StockLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class StockLogServiceTest {

    @Autowired
    private StockLogService stockLogService;

    @Test
    public void testAdd() {
        StockLog log = new StockLog();
        log.setProductId(1L);
        log.setOperationType(0);
        log.setQuantity(10);
        log.setOperator("测试员");
        log.setRemark("测试入库");

        boolean result = stockLogService.add(log);
        System.out.println("添加日志：" + (result ? "成功" : "失败"));
    }

    @Test
    public void testFindByProductId() {
        List<StockLog> logs = stockLogService.findByProductId(1L);
        System.out.println("商品日志数：" + logs.size());
    }

    @Test
    public void testFindPage() {
        PageResult<StockLog> pageResult = stockLogService.findPage(1, 10);
        System.out.println("日志总数：" + pageResult.getTotalSize());
    }
}