package com.lhs.model;

import java.time.LocalDateTime;

public class StockLog {
    private Long logId;
    private Long productId;
    private String productName;
    private Integer operationType;
    private Integer quantity;
    private String operator;
    private String remark;
    private LocalDateTime operationTime;

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getOperationType() { return operationType; }
    public void setOperationType(Integer operationType) { this.operationType = operationType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getOperationTime() { return operationTime; }
    public void setOperationTime(LocalDateTime operationTime) { this.operationTime = operationTime; }
}