package com.lhs.controller;

import com.lhs.common.PageResult;
import com.lhs.common.ResultBean;
import com.lhs.model.Supplier;
import com.lhs.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/list")
    public ResultBean list(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize,
                           @RequestParam(required = false) String supplierName,
                           @RequestParam(required = false) String supplyCategory) {
        PageResult<Supplier> pageResult = supplierService.search(supplierName, supplyCategory, pageNum, pageSize);
        return ResultBean.ok().data(pageResult);
    }

    @GetMapping("/all")
    public ResultBean all() {
        return ResultBean.ok().data(supplierService.findAll());
    }

    @GetMapping("/{id}")
    public ResultBean getById(@PathVariable Long id) {
        Supplier supplier = supplierService.findById(id);
        if (supplier != null) {
            return ResultBean.ok().data(supplier);
        }
        return ResultBean.error().message("供应商不存在");
    }

    @PostMapping("/add")
    public ResultBean add(@RequestBody Supplier supplier) {
        if (supplierService.add(supplier)) {
            return ResultBean.ok().message("添加成功");
        }
        return ResultBean.error().message("添加失败");
    }

    @PutMapping("/edit")
    public ResultBean edit(@RequestBody Supplier supplier) {
        if (supplierService.update(supplier)) {
            return ResultBean.ok().message("修改成功");
        }
        return ResultBean.error().message("修改失败");
    }

    @DeleteMapping("/delete/{id}")
    public ResultBean delete(@PathVariable Long id) {
        if (supplierService.delete(id)) {
            return ResultBean.ok().message("删除成功");
        }
        return ResultBean.error().message("删除失败");
    }
}