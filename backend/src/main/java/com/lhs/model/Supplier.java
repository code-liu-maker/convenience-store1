package com.lhs.model;

import java.time.LocalDateTime;

public class Supplier {
    private Long supplierId;
    private String supplierName;
    private String contactPerson;
    private String contactPhone;
    private String supplyCategory;
    private Integer cooperationStatus;
    private String address;
    private String remark;
    private LocalDateTime createTime;

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getSupplyCategory() { return supplyCategory; }
    public void setSupplyCategory(String supplyCategory) { this.supplyCategory = supplyCategory; }
    public Integer getCooperationStatus() { return cooperationStatus; }
    public void setCooperationStatus(Integer cooperationStatus) { this.cooperationStatus = cooperationStatus; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}