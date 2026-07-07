package com.lhs.mapper;

import com.lhs.model.Supplier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SupplierMapperTest {

    @Autowired
    private SupplierMapper supplierMapper;

    @Test
    public void testFindAll() {
        List<Supplier> suppliers = supplierMapper.findAll();
        System.out.println("供应商总数：" + suppliers.size());
        for (Supplier supplier : suppliers) {
            System.out.println("供应商：" + supplier.getSupplierName() + "，品类：" + supplier.getSupplyCategory());
        }
    }

    @Test
    public void testFindById() {
        Supplier supplier = supplierMapper.findById(1L);
        if (supplier != null) {
            System.out.println("供应商：" + supplier.getSupplierName());
            System.out.println("联系人：" + supplier.getContactPerson());
        }
    }

    @Test
    public void testInsert() {
        Supplier supplier = new Supplier();
        supplier.setSupplierName("测试供应商");
        supplier.setContactPerson("张经理");
        supplier.setContactPhone("13900139000");
        supplier.setSupplyCategory("测试品类");
        supplier.setCooperationStatus(0);

        int result = supplierMapper.insert(supplier);
        System.out.println("插入结果：" + (result > 0 ? "成功" : "失败"));
    }

    @Test
    public void testUpdate() {
        Supplier supplier = supplierMapper.findById(1L);
        if (supplier != null) {
            supplier.setContactPerson("李经理");
            int result = supplierMapper.update(supplier);
            System.out.println("更新结果：" + (result > 0 ? "成功" : "失败"));
        }
    }

    @Test
    public void testDeleteById() {
        int result = supplierMapper.deleteById(100L);
        System.out.println("删除结果：" + (result > 0 ? "成功" : "失败（供应商不存在）"));
    }

    @Test
    public void testSearch() {
        List<Supplier> suppliers = supplierMapper.search("统一", null, 0, 10);
        System.out.println("搜索供应商结果：" + suppliers.size() + "条");
    }
}