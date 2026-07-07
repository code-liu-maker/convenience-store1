package com.lhs.service.impl;

import com.lhs.mapper.CategoryMapper;
import com.lhs.model.Category;
import com.lhs.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryMapper.findById(id);
    }

    @Override
    public boolean add(Category category) {
        return categoryMapper.insert(category) > 0;
    }

    @Override
    public boolean update(Category category) {
        return categoryMapper.update(category) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }

    @Override
    public List<Category> findEnabled() {
        return categoryMapper.findEnabled();
    }
}