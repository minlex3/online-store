package com.store.dto;

import java.util.List;

public class PageResponse<T> {
    private List<T> data;
    private long totalElements;

    public PageResponse(List<T> data, long totalElements) {
        this.data = data;
        this.totalElements = totalElements;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
