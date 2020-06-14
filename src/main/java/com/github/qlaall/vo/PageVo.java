package com.github.qlaall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author: qlaall
 * @Date:2018/8/24
 * @Time:0:14
 */
@ApiModel
public class PageVo<T> {
    @ApiModelProperty(notes = "请求的页数，从1开始，查询必填项", dataType = "Integer")
    private int pageNum;
    @ApiModelProperty(notes = "每页的容量，查询必填项", dataType = "Integer")
    private int pageSize;
    @ApiModelProperty(notes = "以当前pageSize计算的总页数", dataType = "Integer")
    private int pages;
    @ApiModelProperty(notes = "查询结果总数", dataType = "Long")
    private long total;
    private List<T> data;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
