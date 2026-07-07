package com.lhs.service;

import com.lhs.common.PageResult;
import com.lhs.model.Supplier;
import java.util.List;

public interface SupplierService {
    List<Supplier> findAll();
    Supplier findById(Long supplierId);
    boolean add(Supplier supplier);
    boolean update(Supplier supplier);
    boolean delete(Long supplierId);
    PageResult<Supplier> search(String supplierName, String supplyCategory, int pageNum, int pageSize);
}