package com.lhs.controller;

import com.lhs.common.ResultBean;
import com.lhs.model.Category;
import com.lhs.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultBean list() {
        List<Category> list = categoryService.findAll();
        return ResultBean.ok().data(list);
    }

    @GetMapping("/enabled")
    public ResultBean enabled() {
        List<Category> list = categoryService.findEnabled();
        return ResultBean.ok().data(list);
    }

    @GetMapping("/{id}")
    public ResultBean getById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category != null) {
            return ResultBean.ok().data(category);
        }
        return ResultBean.error().message("分类不存在");
    }

    @PostMapping("/add")
    public ResultBean add(@RequestBody Category category) {
        if (categoryService.add(category)) {
            return ResultBean.ok().message("添加成功");
        }
        return ResultBean.error().message("添加失败");
    }

    @PutMapping("/edit")
    public ResultBean edit(@RequestBody Category category) {
        if (categoryService.update(category)) {
            return ResultBean.ok().message("修改成功");
        }
        return ResultBean.error().message("修改失败");
    }

    @DeleteMapping("/delete/{id}")
    public ResultBean delete(@PathVariable Long id) {
        if (categoryService.delete(id)) {
            return ResultBean.ok().message("删除成功");
        }
        return ResultBean.error().message("删除失败");
    }
}