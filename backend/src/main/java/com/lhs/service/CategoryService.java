package com.lhs.service;

import com.lhs.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    boolean add(Category category);
    boolean update(Category category);
    boolean delete(Long id);
    List<Category> findEnabled();
}