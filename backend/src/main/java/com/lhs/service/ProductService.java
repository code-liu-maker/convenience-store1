package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long productId);
    boolean add(Product product);
    boolean update(Product product);

    /**
     * 逻辑删除商品（将 is_deleted 设为 1，数据保留）
     * @param productId 商品ID
     * @return 是否删除成功
     */
    boolean delete(Long productId);

    boolean updateStock(Long productId, int quantity);
    PageResult<Product> findPage(int pageNum, int pageSize);
    PageResult<Product> search(String productName, Long categoryId, Integer status, int pageNum, int pageSize);
    List<Product> findLowStock();
}