package com.lhs.mapper;

import com.lhs.model.Supplier;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SupplierMapper {
    List<Supplier> findAll();
    Supplier findById(@Param("supplierId") Long supplierId);
    int insert(Supplier supplier);
    int update(Supplier supplier);
    int deleteById(@Param("supplierId") Long supplierId);
    List<Supplier> search(@Param("supplierName") String supplierName,
                          @Param("supplyCategory") String supplyCategory,
                          @Param("offset") int offset,
                          @Param("pageSize") int pageSize);
    int countSearch(@Param("supplierName") String supplierName,
                    @Param("supplyCategory") String supplyCategory);
}