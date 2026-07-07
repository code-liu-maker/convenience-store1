package com.lhs.mapper;

import com.lhs.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testFindAll() {
        List<Product> products = productMapper.findAll();
        System.out.println("商品总数：" + products.size());
        for (Product product : products) {
            System.out.println("商品：" + product.getProductName() + "，价格：" + product.getPrice());
        }
    }

    @Test
    public void testFindById() {
        Product product = productMapper.findById(1L);
        if (product != null) {
            System.out.println("商品：" + product.getProductName());
            System.out.println("库存：" + product.getStockQuantity());
            System.out.println("阈值：" + product.getStockThreshold());
        }
    }

    @Test
    public void testInsert() {
        Product product = new Product();
        product.setProductName("测试可乐");
        product.setCategoryId(2L);
        product.setPrice(new BigDecimal("3.00"));
        product.setSpecification("500ml");
        product.setStockQuantity(100);
        product.setStockThreshold(20);
        product.setStatus(0);
        product.setSupplierId(1L);

        int result = productMapper.insert(product);
        System.out.println("插入结果：" + (result > 0 ? "成功" : "失败"));
    }

    @Test
    public void testUpdate() {
        Product product = productMapper.findById(1L);
        if (product != null) {
            product.setPrice(new BigDecimal("3.50"));
            int result = productMapper.update(product);
            System.out.println("更新结果：" + (result > 0 ? "成功" : "失败"));
        }
    }

    @Test
    public void testUpdateStock() {
        int result = productMapper.updateStock(1L, 10);
        System.out.println("库存更新结果：" + (result > 0 ? "成功" : "失败"));
    }

    @Test
    public void testDeleteById() {
        int result = productMapper.deleteById(100L);
        System.out.println("删除结果：" + (result > 0 ? "成功" : "失败（商品不存在）"));
    }

    @Test
    public void testFindPage() {
        List<Product> products = productMapper.findPage(0, 5);
        System.out.println("分页查询结果：" + products.size() + "条");
    }

    @Test
    public void testSearch() {
        List<Product> products = productMapper.search("茶", null, null, 0, 10);
        System.out.println("搜索商品结果：" + products.size() + "条");
    }

    @Test
    public void testFindLowStock() {
        List<Product> products = productMapper.findLowStock();
        System.out.println("库存预警商品数：" + products.size());
        for (Product product : products) {
            System.out.println("预警商品：" + product.getProductName() + "，库存：" + product.getStockQuantity());
        }
    }
}