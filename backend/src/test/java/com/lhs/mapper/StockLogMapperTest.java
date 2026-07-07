package com.lhs.mapper;

import com.lhs.model.StockLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class StockLogMapperTest {

    @Autowired
    private StockLogMapper stockLogMapper;

    @Test
    public void testInsert() {
        StockLog log = new StockLog();
        log.setProductId(1L);
        log.setProductName("测试商品");
        log.setOperationType(0);
        log.setQuantity(10);
        log.setOperator("测试员");
        log.setRemark("测试入库");
        log.setOperationTime(LocalDateTime.now());

        int result = stockLogMapper.insert(log);
        System.out.println("插入结果：" + (result > 0 ? "成功" : "失败"));
    }

    @Test
    public void testFindByProductId() {
        List<StockLog> logs = stockLogMapper.findByProductId(1L);
        System.out.println("商品库存日志条数：" + logs.size());
        for (StockLog log : logs) {
            System.out.println("操作类型：" + (log.getOperationType() == 0 ? "入库" : "出库") + "，数量：" + log.getQuantity());
        }
    }

    @Test
    public void testFindPage() {
        List<StockLog> logs = stockLogMapper.findPage(0, 10);
        System.out.println("日志总数：" + logs.size());
    }
}