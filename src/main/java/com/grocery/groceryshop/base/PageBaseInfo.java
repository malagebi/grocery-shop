package com.grocery.groceryshop.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class PageBaseInfo<T> {
    private List<T> data;
    //当前页
    private long page;


    //总页数
    private long total;

    public PageBaseInfo(List<T> data) {
        this.data = data;
    }

    public <T> PageBaseInfo<T> success(List<T> data) {
        PageBaseInfo result = new PageBaseInfo(data);
        result.setTotal(0L);
        return result;
    }


    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
