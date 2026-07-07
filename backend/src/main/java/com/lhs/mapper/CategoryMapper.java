package com.lhs.mapper;

import com.lhs.model.Category;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CategoryMapper {
    List<Category> findAll();
    Category findById(@Param("categoryId") Long categoryId);
    int insert(Category category);
    int update(Category category);
    int deleteById(@Param("categoryId") Long categoryId);
    List<Category> findEnabled();
}