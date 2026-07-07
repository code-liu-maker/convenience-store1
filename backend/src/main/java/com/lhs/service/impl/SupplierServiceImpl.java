package com.lhs.service.impl;

import com.lhs.common.PageResult;
import com.lhs.mapper.SupplierMapper;
import com.lhs.model.Supplier;
import com.lhs.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public List<Supplier> findAll() {
        return supplierMapper.findAll();
    }

    @Override
    public Supplier findById(Long supplierId) {
        return supplierMapper.findById(supplierId);
    }

    @Override
    public boolean add(Supplier supplier) {
        return supplierMapper.insert(supplier) > 0;
    }

    @Override
    public boolean update(Supplier supplier) {
        return supplierMapper.update(supplier) > 0;
    }

    @Override
    public boolean delete(Long supplierId) {
        return supplierMapper.deleteById(supplierId) > 0;
    }

    @Override
    public PageResult<Supplier> search(String supplierName, String supplyCategory, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Supplier> list = supplierMapper.search(supplierName, supplyCategory, offset, pageSize);
        int total = supplierMapper.countSearch(supplierName, supplyCategory);
        return new PageResult<>(pageNum, pageSize, total, list);
    }
}