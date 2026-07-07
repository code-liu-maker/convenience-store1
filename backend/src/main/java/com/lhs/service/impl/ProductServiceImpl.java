package com.lhs.service.impl;

import com.lhs.common.PageResult;
import com.lhs.mapper.CategoryMapper;
import com.lhs.mapper.ProductMapper;
import com.lhs.mapper.SupplierMapper;
import com.lhs.model.Category;
import com.lhs.model.Product;
import com.lhs.model.Supplier;
import com.lhs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public List<Product> findAll() {
        List<Product> list = productMapper.findAll();
        enrichProductInfo(list);
        return list;
    }

    @Override
    public Product findById(Long productId) {
        Product product = productMapper.findById(productId);
        enrichProductInfo(product);
        return product;
    }

    @Override
    public boolean add(Product product) {
        return productMapper.insert(product) > 0;
    }

    @Override
    public boolean update(Product product) {
        return productMapper.update(product) > 0;
    }

    /**
     * 逻辑删除商品
     * 将 is_deleted 字段设为 1，数据保留在数据库中，但查询时被过滤（is_deleted = 0）
     */
    @Override
    public boolean delete(Long productId) {
        return productMapper.deleteById(productId) > 0;
    }

    @Override
    public boolean updateStock(Long productId, int quantity) {
        return productMapper.updateStock(productId, quantity) > 0;
    }

    @Override
    public PageResult<Product> findPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Product> list = productMapper.findPage(offset, pageSize);
        int total = productMapper.countTotal();
        enrichProductInfo(list);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    @Override
    public PageResult<Product> search(String productName, Long categoryId, Integer status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Product> list = productMapper.search(productName, categoryId, status, offset, pageSize);
        int total = productMapper.countSearch(productName, categoryId, status);
        enrichProductInfo(list);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    @Override
    public List<Product> findLowStock() {
        List<Product> list = productMapper.findLowStock();
        enrichProductInfo(list);
        return list;
    }

    private void enrichProductInfo(List<Product> products) {
        if (products == null || products.isEmpty()) return;

        Map<Long, String> categoryMap = new HashMap<>();
        Map<Long, String> supplierMap = new HashMap<>();

        for (Category c : categoryMapper.findAll()) {
            categoryMap.put(c.getCategoryId(), c.getCategoryName());
        }
        for (Supplier s : supplierMapper.findAll()) {
            supplierMap.put(s.getSupplierId(), s.getSupplierName());
        }

        for (Product p : products) {
            p.setCategoryName(categoryMap.get(p.getCategoryId()));
            p.setSupplierName(supplierMap.get(p.getSupplierId()));
        }
    }

    private void enrichProductInfo(Product product) {
        if (product == null) return;
        if (product.getCategoryId() != null) {
            Category c = categoryMapper.findById(product.getCategoryId());
            if (c != null) product.setCategoryName(c.getCategoryName());
        }
        if (product.getSupplierId() != null) {
            Supplier s = supplierMapper.findById(product.getSupplierId());
            if (s != null) product.setSupplierName(s.getSupplierName());
        }
    }
}