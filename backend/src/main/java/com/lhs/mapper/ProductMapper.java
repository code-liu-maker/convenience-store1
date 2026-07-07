package com.lhs.mapper;

import com.lhs.model.Product;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProductMapper {
    List<Product> findAll();
    Product findById(@Param("productId") Long productId);
    int insert(Product product);
    int update(Product product);
    int deleteById(@Param("productId") Long productId);
    int updateStock(@Param("productId") Long productId, @Param("quantity") int quantity);
    List<Product> findPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
    int countTotal();
    List<Product> search(@Param("productName") String productName,
                         @Param("categoryId") Long categoryId,
                         @Param("status") Integer status,
                         @Param("offset") int offset,
                         @Param("pageSize") int pageSize);
    int countSearch(@Param("productName") String productName,
                    @Param("categoryId") Long categoryId,
                    @Param("status") Integer status);
    List<Product> findLowStock();
}