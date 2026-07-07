package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testFindAll() {
        List<Product> products = productService.findAll();
        System.out.println("商品总数：" + products.size());
    }

    @Test
    public void testFindById() {
        Product product = productService.findById(1L);
        if (product != null) {
            System.out.println("商品：" + product.getProductName());
            System.out.println("分类：" + product.getCategoryName());
            System.out.println("供应商：" + product.getSupplierName());
        }
    }

    @Test
    public void testAdd() {
        Product product = new Product();
        product.setProductName("测试商品");
        product.setCategoryId(1L);
        product.setPrice(new BigDecimal("9.90"));
        product.setSpecification("标准");
        product.setStockQuantity(50);
        product.setStockThreshold(10);
        product.setStatus(0);
        product.setSupplierId(1L);

        boolean result = productService.add(product);
        System.out.println("添加商品：" + (result ? "成功" : "失败"));
    }

    @Test
    public void testUpdate() {
        Product product = productService.findById(1L);
        if (product != null) {
            product.setPrice(new BigDecimal("4.50"));
            boolean result = productService.update(product);
            System.out.println("更新商品：" + (result ? "成功" : "失败"));
        }
    }

    @Test
    public void testUpdateStock() {
        boolean result = productService.updateStock(1L, 20);
        System.out.println("更新库存：" + (result ? "成功" : "失败"));
    }

    @Test
    public void testSearch() {
        PageResult<Product> pageResult = productService.search("茶", null, 0, 1, 10);
        System.out.println("搜索到商品：" + pageResult.getTotalSize() + "条");
    }

    @Test
    public void testFindLowStock() {
        List<Product> products = productService.findLowStock();
        System.out.println("库存预警商品数：" + products.size());
    }
}