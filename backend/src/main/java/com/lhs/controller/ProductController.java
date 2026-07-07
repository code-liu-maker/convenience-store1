package com.lhs.controller;

import com.lhs.common.PageResult;
import com.lhs.common.ResultBean;
import com.lhs.model.Product;
import com.lhs.model.StockLog;
import com.lhs.service.ProductService;
import com.lhs.service.StockLogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockLogService stockLogService;

    @GetMapping("/list")
    public ResultBean list(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize,
                           @RequestParam(required = false) String productName,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) Integer status) {
        PageResult<Product> pageResult = productService.search(productName, categoryId, status, pageNum, pageSize);
        return ResultBean.ok().data(pageResult);
    }

    @GetMapping("/{id}")
    public ResultBean getById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return ResultBean.ok().data(product);
        }
        return ResultBean.error().message("商品不存在");
    }

    @PostMapping("/add")
    public ResultBean add(@RequestBody Product product) {
        if (productService.add(product)) {
            return ResultBean.ok().message("添加成功");
        }
        return ResultBean.error().message("添加失败");
    }

    @PutMapping("/edit")
    public ResultBean edit(@RequestBody Product product) {
        if (productService.update(product)) {
            return ResultBean.ok().message("修改成功");
        }
        return ResultBean.error().message("修改失败");
    }

    /**
     * 逻辑删除商品（将 is_deleted 设为 1，数据保留但查询时过滤）
     * 不会触发外键约束异常，因为只是更新标记字段
     */
    @DeleteMapping("/delete/{id}")
    public ResultBean delete(@PathVariable Long id) {
        if (productService.delete(id)) {
            return ResultBean.ok().message("删除成功");
        }
        return ResultBean.error().message("删除失败");
    }


    @PostMapping("/stock/in")
    public ResultBean stockIn(@RequestBody StockLog log, HttpSession session) {
        com.lhs.model.User user = (com.lhs.model.User) session.getAttribute("userInfo");
        log.setOperationType(0);
        log.setOperator(user != null ? user.getUserName() : "系统");

        productService.updateStock(log.getProductId(), log.getQuantity());
        stockLogService.add(log);
        return ResultBean.ok().message("入库成功");
    }

    @PostMapping("/stock/out")
    public ResultBean stockOut(@RequestBody StockLog log, HttpSession session) {
        com.lhs.model.User user = (com.lhs.model.User) session.getAttribute("userInfo");
        log.setOperationType(1);
        log.setOperator(user != null ? user.getUserName() : "系统");

        productService.updateStock(log.getProductId(), -log.getQuantity());
        stockLogService.add(log);
        return ResultBean.ok().message("出库成功");
    }

    @GetMapping("/low-stock")
    public ResultBean lowStock() {
        return ResultBean.ok().data(productService.findLowStock());
    }
}