package com.lhs.mapper;

import com.lhs.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void testFindAll() {
        List<Category> categories = categoryMapper.findAll();
        System.out.println("分类总数：" + categories.size());
        for (Category category : categories) {
            System.out.println("分类：" + category.getCategoryName() + "，排序：" + category.getSortOrder());
        }
    }

    @Test
    public void testFindEnabled() {
        List<Category> categories = categoryMapper.findEnabled();
        System.out.println("启用的分类数：" + categories.size());
    }

    @Test
    public void testFindById() {
        Category category = categoryMapper.findById(1L);
        if (category != null) {
            System.out.println("找到分类：" + category.getCategoryName());
        }
    }

    @Test
    public void testInsert() {
        Category category = new Category();
        category.setCategoryName("饮料");
        category.setSortOrder(5);
        category.setStatus(0);

        int result = categoryMapper.insert(category);
        System.out.println("插入结果：" + (result > 0 ? "成功" : "失败"));
    }

    @Test
    public void testUpdate() {
        Category category = categoryMapper.findById(1L);
        if (category != null) {
            category.setCategoryName("休闲零食");
            int result = categoryMapper.update(category);
            System.out.println("更新结果：" + (result > 0 ? "成功" : "失败"));
        }
    }

    @Test
    public void testDeleteById() {
        int result = categoryMapper.deleteById(100L);
        System.out.println("删除结果：" + (result > 0 ? "成功" : "失败（分类不存在）"));
    }
}