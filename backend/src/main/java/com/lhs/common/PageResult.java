package com.lhs.common;

import java.util.List;

public class PageResult<T> {
    private int pageNum;
    private int pageSize;
    private long totalSize;
    private int totalPages;
    private List<T> list;

    public PageResult() {}

    public PageResult(int pageNum, int pageSize, long totalSize, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.list = list;
        this.totalPages = (int) Math.ceil((double) totalSize / pageSize);
    }

    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    public long getTotalSize() { return totalSize; }
    public void setTotalSize(long totalSize) { this.totalSize = totalSize; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }
}