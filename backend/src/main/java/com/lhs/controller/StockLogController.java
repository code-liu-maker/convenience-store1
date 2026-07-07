package com.lhs.controller;

import com.lhs.common.PageResult;
import com.lhs.common.ResultBean;
import com.lhs.model.StockLog;
import com.lhs.service.StockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-log")
public class StockLogController {

    @Autowired
    private StockLogService stockLogService;

    @GetMapping("/list")
    public ResultBean list(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<StockLog> pageResult = stockLogService.findPage(pageNum, pageSize);
        return ResultBean.ok().data(pageResult);
    }

    @GetMapping("/product/{productId}")
    public ResultBean findByProductId(@PathVariable Long productId) {
        return ResultBean.ok().data(stockLogService.findByProductId(productId));
    }
}